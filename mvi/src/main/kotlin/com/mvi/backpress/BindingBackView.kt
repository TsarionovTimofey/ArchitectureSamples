package com.mvi.backpress

import androidx.viewbinding.ViewBinding
import com.mvi.binding.BindingView

abstract class BindingBackView<View : ViewBinding, in Model : Any, Event : Any>(
    binding: () -> View
) : BindingView<View, Model, Event>(binding), BaseBackView<Model, Event> {

    protected abstract val onBackPressedEvent: Event

    override fun dispatchBackPressed() {
        dispatch(onBackPressedEvent)
    }
}
