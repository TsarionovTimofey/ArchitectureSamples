package com.mvi

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewProperty<V : BaseView<*, *>>(
    fragment: Fragment,
    val viewFactory: () -> V
) : ReadOnlyProperty<Fragment, V> {
    private var view: V? = null

    init {
        fragment.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                        viewLifecycleOwner.lifecycle.addObserver(
                            object : DefaultLifecycleObserver {
                                override fun onDestroy(owner: LifecycleOwner) {
                                    view = null
                                }
                            }
                        )
                    }
                }
            }
        )
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        this.view?.let {
            return it
        }

        val lifecycle = thisRef.viewLifecycleOwner.lifecycle
        check(lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            "Should not attempt to get viewImpl when Fragment views are destroyed."
        }

        return viewFactory().also {
            this.view = it
        }
    }
}

fun <V : BaseView<*, *>> Fragment.viewFrom(viewFactory: () -> V) = ViewProperty(this, viewFactory)
