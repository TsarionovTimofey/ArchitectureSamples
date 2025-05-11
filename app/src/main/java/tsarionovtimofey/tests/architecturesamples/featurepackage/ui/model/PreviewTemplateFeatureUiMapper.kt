package tsarionovtimofey.tests.architecturesamples.featurepackage.ui.model

import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Intent
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.State
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.Event
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.Model
import com.mvi.Mapper
import android.content.Context
import javax.inject.Inject

class PreviewTemplateFeatureUiMapper @Inject constructor(context: Context) : Mapper<State, Model> {

    fun map(event: Event) = event.toIntent()

    override fun invoke(state: State) = Model(
        stub = state.stub,
        isLoading = state.isLoading
    )

    private fun Event.toIntent() = when (this) {
        Event.OnBackClicked -> Intent.OnBackClicked
        Event.OnLogoClicked -> Intent.OnLogoClicked
    }
}