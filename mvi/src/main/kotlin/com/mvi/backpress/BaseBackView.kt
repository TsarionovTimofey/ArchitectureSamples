package com.mvi.backpress

import com.mvi.BaseView

interface BaseBackView<in Model : Any, out Event : Any> : BaseView<Model, Event> {

    fun dispatchBackPressed()
}
