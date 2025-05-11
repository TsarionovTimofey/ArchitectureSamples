package com.mvi

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseMviFragment<V : BaseView<*, *>>(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    protected abstract val binder: Binder<V>
    protected abstract val viewImpl: V

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MviLifecycleObserver(binder, viewLifecycleOwner)
        binder.onViewCreated(viewImpl)
    }
}
