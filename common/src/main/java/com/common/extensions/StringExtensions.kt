package com.common.extensions

import android.graphics.Typeface
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Base64
import android.util.Patterns
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.isValidEmail(): Boolean {
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isMatch(input: String?): Boolean {
    if (isEmpty() || input.isNullOrEmpty()) {
        return false
    }
    return this == input
}

fun String.encrypt(): String {
    return try {
        Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)
    } catch (e: Exception) {
        this
    }
}

fun String.decrypt(): String {
    return try {
        String(Base64.decode(this, Base64.NO_WRAP))
    } catch (e: Exception) {
        this
    }
}

fun String.toIntOrZero(): Int {
    return toIntOrNull() ?: 0
}

fun String.toLongOrZero(): Long {
    return toLongOrNull() ?: 0
}

fun String.toLongOrDefault(default: Long): Long {
    return toLongOrNull() ?: default
}

fun String.splitByHyphen(): Array<String> {
    return this.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
}

fun String.isValidPassword(): Boolean {
    val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,64}$"
    val regex = Regex(passwordPattern)
    return this.matches(regex)
}

fun String.strikeThrough(from: Int, to: Int): Spanned = SpannableString(this).apply {
    setSpan(StrikethroughSpan(), from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun String.parseDate(pattern: String): Date? {
    val sdf: DateFormat = SimpleDateFormat(pattern, Locale.UK)
    return try {
        sdf.parse(this)
    } catch (e: ParseException) {
        null
    }
}

infix fun String.default(value: String?): String? {
    return if (this.isEmpty()) {
        value
    } else {
        this
    }
}

fun String.getPhotoOrientation(): Int =
    try {
        ExifInterface(this).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
    } catch (e: IOException) {
        ExifInterface.ORIENTATION_NORMAL
    }

fun String.fromHtml(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }

fun String.makeTextBetweenAsterisksBold(): SpannableStringBuilder {
    val spannable = SpannableStringBuilder(this)

    val regex = Regex("\\*{1}(.*?)\\*{1}")
    regex.findAll(spannable).iterator().forEach { matchResult ->
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            matchResult.range.first,
            matchResult.range.last,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.replace(matchResult.range.first, matchResult.range.first + 1, "")
        spannable.replace(matchResult.range.last - 1, matchResult.range.last, "")
    }

    return spannable
}

fun String.normalizeTags(): String =
    this.replace("<strong>", "<b>").replace("</strong>", "</b>")

fun String.isImageUrl(): Boolean {
    Uri.parse(this).lastPathSegment?.let { segment ->
        return segment.contains(".png") || segment.contains(".jpg") || segment.contains(".jpeg")
    }
    return false
}

fun String.appVersionFromString(): Int {
    val versions = split(".").map { split -> split.filter { char -> char.isDigit() } }
    val major = versions.getOrNull(0)?.toIntOrZero()?.times(10000) ?: 0
    val minor = versions.getOrNull(1)?.toIntOrZero()?.times(1000) ?: 0
    val patch = versions.getOrNull(2)?.toIntOrZero()?.times(100) ?: 0
    return major + minor + patch
}

fun String.appendQueryParam(key: String, value: String) =
    this.plus("?").plus(key).plus("=").plus(value)

fun String.bold(start: Int = 0, end: Int = this.length) = this.toSpannable().apply {
    set(start, end, StyleSpan(Typeface.BOLD))
}

fun String.underline(start: Int = 0, end: Int = this.length) = this.toSpannable().apply {
    set(start, end, UnderlineSpan())
}

fun String.onlyDigit() = filter { it.isDigit() }

fun View.doOnFocus(onGain: () -> Unit, onLost: () -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            onLost()
        }
        if (hasFocus) {
            onGain()
        }
    }
}
