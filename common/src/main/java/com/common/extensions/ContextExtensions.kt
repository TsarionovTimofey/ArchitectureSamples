package com.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.TypedArray
import android.database.Cursor
import android.graphics.Point
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings.Secure
import android.text.TextUtils
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


fun Context.getBackgroundTintList(drawableRes: Int): ColorStateList? =
    ContextCompat.getColorStateList(this, drawableRes)

fun Context.getCompatColor(colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Context.getScreenNaturalSize(): Point {
    val screenNaturalOrientation = this.getScreenNaturalOrientation()
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    wm.defaultDisplay.getRealSize(point)
    val currentOrientation = this.resources.configuration.orientation
    return if (currentOrientation == screenNaturalOrientation)
        point
    else Point(point.y, point.x)
}

fun Context.getScreenNaturalOrientation(): Int {
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val config = this.resources.configuration
    val rotation = windowManager.defaultDisplay.rotation
    return if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && config.orientation == Configuration.ORIENTATION_LANDSCAPE || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && config.orientation == Configuration.ORIENTATION_PORTRAIT)
        Configuration.ORIENTATION_LANDSCAPE
    else
        Configuration.ORIENTATION_PORTRAIT
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )

}

fun Context.getUriForFile(path: String): Uri? {
    val file = File(path)
    return getUriForFile(this, "com.getbits" + ".provider", file)
}

fun Context.getFilePathOfSelectedImage(fileUri: Uri): String {
    var filePath = ""
    val filePathColumn =
        arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor? = contentResolver.query(
        fileUri, filePathColumn, null, null, null
    )

    if (cursor != null && cursor.moveToFirst()) {
        val columnIndex = cursor
            .getColumnIndex(filePathColumn[0])
        filePath = cursor.getString(columnIndex)
        cursor.close()
    }
    return filePath
}

fun Context.getFilePathFromURI(
    contentUri: Uri?
): String? { //copy file and send new file path
    val fileName: String? = getFileName(contentUri)
    val directory = File(
        getExternalFilesDirs(null)[0], "/bits"
    )
    // have the object build the directory structure, if needed.
    if (!directory.exists()) {
        directory.mkdirs()
    }
    if (!TextUtils.isEmpty(fileName)) {
        val copyFile =
            File(directory.toString() + File.separator + fileName)
        // create folder if not exists
        contentUri?.let {
            copy(this, it, copyFile)
        }
        return copyFile.absolutePath
    }
    return null
}

fun getFileName(uri: Uri?): String? {
    if (uri == null) return null
    var fileName: String? = null
    val path = uri.path
    path?.let {
        val cut = it.lastIndexOf('/')
        if (cut != -1) {
            fileName = it.substring(cut + 1)
        }
    }
    return fileName
}

fun Uri.fileName(): String? {
    var fileName: String? = null
    this.path?.let {
        val cut = it.lastIndexOf('/')
        if (cut != -1) {
            fileName = it.substring(cut + 1)
        }
    }
    return fileName
}

fun Context.getFileExtensionFromUri(uri: Uri?): String {
    uri?.let {
        val fileType = contentResolver.getType(uri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
        return extension ?: ""
    }
    return ""
}

fun Context.getFileNameFromUri(uri: Uri?, name: String): String {
    uri?.let {
        val fileType = contentResolver.getType(uri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
        return "$name.${extension ?: ""}"
    }
    return name
}

fun copy(
    context: Context,
    srcUri: Uri,
    dstFile: File?
) {
    try {
        val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
        val outputStream: OutputStream = FileOutputStream(dstFile)
        copystream(inputStream, outputStream)
        inputStream.close()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Throws(Exception::class, IOException::class)
fun copystream(input: InputStream?, output: OutputStream?): Int {
    val buffer = ByteArray(1024 * 2)
    val inputStream = BufferedInputStream(input, 1024 * 2)
    val out = BufferedOutputStream(output, 1024 * 2)
    var count = 0
    var n = 0
    try {
        while (inputStream.read(buffer, 0, 1024 * 2).also({ n = it }) != -1) {
            out.write(buffer, 0, n)
            count += n
        }
        out.flush()
    } finally {
        try {
            out.close()
        } catch (e: IOException) {
            Log.e(e.message, java.lang.String.valueOf(e))
        }
        try {
            inputStream.close()
        } catch (e: IOException) {
            Log.e(e.message, java.lang.String.valueOf(e))
        }
    }
    return count
}

@SuppressLint("HardwareIds")
fun Context.getAndroidId(): String = Secure.getString(
    contentResolver,
    Secure.ANDROID_ID
)

fun Resources.getDrawableArray(resId: Int): IntArray {
    val array: TypedArray = obtainTypedArray(resId)
    val result = IntArray(array.length())
    for (i in result.indices) result[i] = array.getResourceId(i, 0)
    array.recycle()
    return result
}

fun Resources.getDrawableList(resId: Int): List<Int> {
    val array: TypedArray = obtainTypedArray(resId)
    val result = IntArray(array.length())
    for (i in result.indices) result[i] = array.getResourceId(i, 0)
    array.recycle()
    return result.asList()
}

fun Context.checkPermissionsAreGranted(array: Array<String>): Boolean {
    var result = true
    for (permission in array) {
        val permissionIsGranted = ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        result = result && permissionIsGranted
    }
    return result
}