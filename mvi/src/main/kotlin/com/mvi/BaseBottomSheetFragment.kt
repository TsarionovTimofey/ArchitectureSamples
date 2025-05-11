package com.mvi

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes

abstract class BaseBottomSheetFragment<V : BaseView<*, *>>(@LayoutRes override val layoutId: Int) :
    FixedBottomSheetFragment(layoutId) {
    abstract val binder: Binder<V>
    abstract val viewImpl: V

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MviLifecycleObserver(binder, viewLifecycleOwner)
        binder.onViewCreated(viewImpl)
    }
}
