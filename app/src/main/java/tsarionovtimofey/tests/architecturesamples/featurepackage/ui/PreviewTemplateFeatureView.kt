package tsarionovtimofey.tests.architecturesamples.featurepackage.ui

import com.mvi.BaseView
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.Event
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.Model

interface PreviewTemplateFeatureView : BaseView<Model, Event> {

    data class Model(
        val stub: String = "",
        val isLoading: Boolean
    )

    sealed class Event {
        object OnBackClicked : Event()
        object OnLogoClicked : Event()
    }

    sealed class UiLabel {
        data class ShowMessage(val message: CharSequence?) : UiLabel()
    }
}