package tsarionovtimofey.tests.architecturesamples.featurepackage.domain

import com.arkivanov.mvikotlin.core.store.Reducer
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Result
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.State

internal class PreviewTemplateFeatureReducer : Reducer<State, Result> {

    override fun State.reduce(result: Result): State {
        return when (result) {
            is Result.Loader -> copy(isLoading = result.isVisible)
            is Result.InfoUpdated -> copy(stub = result.stub)
        }
    }
}