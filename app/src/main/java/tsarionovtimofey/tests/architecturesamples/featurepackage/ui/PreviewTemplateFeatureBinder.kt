package tsarionovtimofey.tests.architecturesamples.featurepackage.ui

import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Intent
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Label
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.State
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.Event
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.Model
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.UiLabel
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.model.PreviewTemplateFeatureUiMapper
import com.common.coroutines.DispatchersProvider
import com.mvi.DefaultBinder
import com.mvi.store.UnicastSubject
import javax.inject.Inject

class PreviewTemplateFeatureBinder @Inject constructor(
    store: PreviewTemplateFeatureStore,
    dispatchersProvider: DispatchersProvider,
    private val uiMapper: PreviewTemplateFeatureUiMapper // + навигация
) : DefaultBinder<Intent, State, Label, Event, Model, PreviewTemplateFeatureView>(
    store,
    dispatchersProvider
) {

    val labelSubject = UnicastSubject<UiLabel>()

    override val stateToModel: suspend State.() -> Model = {
        uiMapper.invoke(this)
    }

    override val eventToIntent: suspend Event.() -> Intent = {
        uiMapper.map(this)
    }

    override fun handleLabel(label: Label) = when (label) {
        is Label.ProcessMessage -> {
            labelSubject.onNext(UiLabel.ShowMessage(label.message))
        }

        Label.Back -> {
            // навигация выход
        }
    }
}