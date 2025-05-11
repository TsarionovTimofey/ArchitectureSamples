package tsarionovtimofey.tests.architecturesamples.featurepackage.domain

import android.os.Parcelable
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.parcelize.Parcelize
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Intent
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Label
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.State

interface PreviewTemplateFeatureStore : Store<Intent, State, Label> {

    sealed class Intent {
        object OnBackClicked : Intent()
        object OnLogoClicked : Intent()
    }

    sealed class Action {
        object Init : Action()
    }

    @Parcelize
    data class State(
        val stub: String = "",
        val isLoading: Boolean = false
    ) : Parcelable

    sealed class Result {
        data class Loader(val isVisible: Boolean) : Result()
        data class InfoUpdated(val stub: String) : Result()
    }

    sealed class Label {
        data class ProcessMessage(val message: String) : Label()
        object Back : Label()
    }
}