package com.mvi.saving

import android.os.Parcelable
import com.arkivanov.mvikotlin.core.store.Store

interface SavedStateKeeper {
    fun <P : Parcelable> get(key: String): P?
    fun <S : Store<*, *, *>, P : Parcelable> register(
        store: S,
        key: String,
        stateProvider: S.() -> P
    )
    fun unregister()
}
