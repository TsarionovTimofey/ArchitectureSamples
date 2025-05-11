package com.mvi.binding

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

fun <T : ViewBinding> BottomSheetDialogFragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

@Deprecated(
    "bottom sheet dialog fragment should use viewbinding with ::bind",
    replaceWith = ReplaceWith(
        "viewBinding(T::bind)",
        "com.mvi.binding.viewBinding"
    ),
    level = DeprecationLevel.ERROR
)
fun <T : ViewBinding> BottomSheetDialogFragment.viewBinding(
    bindingInflater: (LayoutInflater) -> T
) = DialogFragmentViewBindingDelegate(this, bindingInflater)

@Deprecated(
    "dialog fragment should use viewbinding with ::inflate",
    replaceWith = ReplaceWith(
        "viewBinding(T::inflate)",
        "com.mvi.binding.viewBinding"
    ),
    level = DeprecationLevel.ERROR
)
fun <T : ViewBinding> DialogFragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

fun <T : ViewBinding> DialogFragment.viewBinding(
    bindingInflater: (LayoutInflater) -> T
) = DialogFragmentViewBindingDelegate(this, bindingInflater)
