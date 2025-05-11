package com.common.extensions

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Default minimal [DiffUtil.ItemCallback] implementation.
 * Compares items in [areContentsTheSame] and returns null as change payload.
 */
fun <T> itemCallback(
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    },
    getChangePayload: (oldItem: T, newItem: T) -> Any? = { _, _ ->
        null
    }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
        areItemsTheSame(oldItem, newItem)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
        areContentsTheSame(oldItem, newItem)

    override fun getChangePayload(oldItem: T & Any, newItem: T & Any): Any? =
        getChangePayload(oldItem, newItem)
}
