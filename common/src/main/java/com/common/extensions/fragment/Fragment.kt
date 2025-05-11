package com.common.extensions.fragment

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.arkivanov.mvikotlin.core.BuildConfig
import com.common.LCHandler
import com.common.extensions.view.hideKeyboard
import com.common.extensions.view.showKeyboard
import com.google.android.material.snackbar.Snackbar

val Fragment.handler
    get() = LCHandler(viewLifecycleOwner, true)

fun Fragment.showKeyboard() {
    requireView().showKeyboard()
}

fun Fragment.hideKeyboard() {
    requireView().hideKeyboard()
}

fun Fragment.showSnackbarMessage(message: CharSequence?) {
    if (isVisible && !message.isNullOrBlank()) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
            show()
        }
    }
}

fun Fragment.enableSecureModePerScreen() {
    if (!BuildConfig.DEBUG) {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }

            override fun onStop(owner: LifecycleOwner) {
                activity?.window?.clearFlags(
                    WindowManager.LayoutParams.FLAG_SECURE
                )
                super.onStop(owner)
            }
        })
    }
}

fun Fragment.showDialog(
    tag: String? = null,
    dialogFragment: () -> DialogFragment
) {
    val fragmentManager: FragmentManager = childFragmentManager
    val df = dialogFragment()
    df.show(fragmentManager, tag)
}

fun Fragment.setKeyResultListener(
    fragmentManager: FragmentManager,
    requiredKey: String,
    action: (Bundle) -> Unit
) = fragmentManager.setFragmentResultListener(
    requiredKey,
    viewLifecycleOwner
) { requestKey, bundle ->
    if (requestKey != requiredKey) {
        return@setFragmentResultListener
    }
    action(bundle)
}
