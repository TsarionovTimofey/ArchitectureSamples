package com.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import javax.inject.Provider

class ViewModelFactory<VM : ViewModel>(private val viewModelProvider: Provider<VM>) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelProvider.get() as T
}

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModelFrom(
    crossinline provider: () -> Provider<VM>
): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { this.viewModelStore },
        {
            ViewModelFactory(provider())
        }
    )
}
