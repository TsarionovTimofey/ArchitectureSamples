package com.apps65.ihelp.core.string

import android.content.res.Resources
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class ResourceString : Parcelable {

    @Parcelize
    data class Raw(val text: String) : ResourceString()

    @Parcelize
    data class Res(@StringRes val resId: Int) : ResourceString()

    @Parcelize
    data class Mask(
        @StringRes val resId: Int,
        val arg: @RawValue Any
    ) : ResourceString()

    @Parcelize
    data class MaskPlural(
        @StringRes val stringResId: Int,
        @PluralsRes val pluralsResId: Int,
        val count: Int
    ) : ResourceString()

    fun getString(resources: Resources) = when (this) {
        is Raw -> text
        is Res -> resources.getString(resId)
        is Mask -> resources.getString(resId, arg)
        is MaskPlural -> {
            val quantityString = resources.getQuantityString(pluralsResId, count, count)
            resources.getString(stringResId, quantityString)
        }
    }
}
