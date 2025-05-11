package com.mvi

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity

abstract class BaseMviActivity<V : BaseView<*, *>> : AppCompatActivity() {
    abstract val binder: Binder<V>
    abstract val viewImpl: V

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MviLifecycleObserver(binder, this)
        binder.onViewCreated(viewImpl)
    }
}
