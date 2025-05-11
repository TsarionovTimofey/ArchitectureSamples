package tsarionovtimofey.tests.architecturesamples.featurepackage.domain

import com.mvi.saving.SavedStateKeeper
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Action
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Intent
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Label
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.State
import javax.inject.Inject

internal class PreviewTemplateFeatureStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val stateKeeper: SavedStateKeeper,
    private val executorsFactory: PreviewTemplateFeatureExecutorsFactory,
    private val args: PreviewTemplateFeatureArgs
) {

    private val bootstrapper = SimpleBootstrapper(Action.Init)

    fun create(): PreviewTemplateFeatureStore {
        val storeDelegate = storeFactory.create(
            name = this::class.java.name,
            initialState = getInitialState(),
            executorFactory = executorsFactory.create(),
            reducer = PreviewTemplateFeatureReducer(),
            bootstrapper = bootstrapper
        )

        return object : PreviewTemplateFeatureStore, Store<Intent, State, Label> by storeDelegate {
            override fun dispose() {
                storeDelegate.dispose()
                stateKeeper.unregister()
            }
        }.also { registerStateKeeper(it) }
    }

    private fun getInitialState(): State {
        val savedState = stateKeeper.get<State>(PREVIEW_TEMPLATE_FEATURE_STORE_STATE)
        return savedState ?: State(stub = args.stub)
    }

    private fun registerStateKeeper(store: PreviewTemplateFeatureStore) {
        stateKeeper.register(store, PREVIEW_TEMPLATE_FEATURE_STORE_STATE) {
            this.state
        }
    }

    companion object {
        private const val PREVIEW_TEMPLATE_FEATURE_STORE_STATE = "preview_template_feature_store_state"
    }
}