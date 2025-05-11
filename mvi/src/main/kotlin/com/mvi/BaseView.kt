package com.mvi

import com.arkivanov.mvikotlin.core.view.MviView

interface BaseView<in Model : Any, out Event : Any> : MviView<Model, Event>
