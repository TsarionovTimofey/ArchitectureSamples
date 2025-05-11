package com.mvi.binding

import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class DialogFragmentViewBindingDelegate<T : ViewBinding>(
    fragment: DialogFragment,
    val viewBindingFactory: (LayoutInflater) -> T
) : ReadOnlyProperty<DialogFragment, T> {
    private var binding: T? = null

    init {
        val callback = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentViewDestroyed(fm, f)
                if (f === fragment) {
                    binding = null
                }
            }
        }

        fragment.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    fragment.parentFragmentManager
                        .registerFragmentLifecycleCallbacks(callback, false)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    fragment.parentFragmentManager
                        .unregisterFragmentLifecycleCallbacks(callback)
                }
            }
        )
    }

    override fun getValue(thisRef: DialogFragment, property: KProperty<*>): T {
        binding?.let {
            return it
        }

        val lifecycle = thisRef.lifecycle
        check(lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            "Should not attempt to get bindings when DialogFragment views are destroyed."
        }

        return viewBindingFactory(thisRef.layoutInflater).also {
            this.binding = it
        }
    }
}
