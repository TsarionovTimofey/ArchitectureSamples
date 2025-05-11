package com.mvi

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<V : BaseView<*, *>>(@LayoutRes layoutId: Int) :
    DialogFragment(layoutId) {
    abstract val binder: Binder<V>
    abstract val viewImpl: V

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MviLifecycleObserver(binder, viewLifecycleOwner)
        binder.onViewCreated(viewImpl)
    }
}
