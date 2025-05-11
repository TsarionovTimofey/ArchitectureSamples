package com.common.extensions.fragment

import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

private const val BUNDLE_INITIAL_ARGS = "BUNDLE_INITIAL_ARGS"

/**
 * Pass parcelable arguments to [F] fragment with [BUNDLE_INITIAL_ARGS] key and return [F] fragment.
 *
 * @receiver target [Fragment].
 */
fun <F : Fragment> F.withInitialArguments(params: Parcelable) = apply {
    arguments = bundleOf(BUNDLE_INITIAL_ARGS to params)
}

/**
 * Return parcelable argument which was be passed by [withInitialArguments] method.
 *
 * @receiver target [Fragment].
 * @param [T] Argument type.
 * @throws IllegalArgumentException when parcelable argument with key [BUNDLE_INITIAL_ARGS] not found.
 * @return Argument as [T].
 */
fun <T : Parcelable> Fragment.initialArguments(): T {
    return requireArguments().getParcelable<T>(BUNDLE_INITIAL_ARGS)
        ?: throw IllegalArgumentException("Fragment doesn't contain initial args")
}
