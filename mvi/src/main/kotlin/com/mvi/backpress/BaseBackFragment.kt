package com.mvi.backpress

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import com.mvi.BaseMviFragment

abstract class BaseBackFragment<V : BaseBackView<*, *>>(@LayoutRes layoutId: Int) :
    BaseMviFragment<V>(layoutId) {

    protected open val isBackEnabled = true

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isBackEnabled) {
                viewImpl.dispatchBackPressed()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
}
