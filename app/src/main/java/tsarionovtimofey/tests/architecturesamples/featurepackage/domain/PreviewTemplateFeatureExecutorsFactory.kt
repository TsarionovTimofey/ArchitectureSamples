package tsarionovtimofey.tests.architecturesamples.featurepackage.domain

import com.common.coroutines.DispatchersProvider
import com.mvi.common.IntentExecutorDelegate
import com.mvi.common.SuspendDelegationExecutor
import com.arkivanov.mvikotlin.core.store.Executor
import kotlinx.coroutines.delay
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Action
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Intent
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Label
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.Result
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureStore.State
import javax.inject.Inject

internal typealias PreviewTemplateFeatureExecutor = () -> Executor<Intent, Action, State, Result, Label>
internal typealias PreviewTemplateFeatureExecutorDelegate = IntentExecutorDelegate<Intent, State, Result, Label>

internal class PreviewTemplateFeatureExecutorsFactory @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
) {

    fun create(): PreviewTemplateFeatureExecutor = {
        object : SuspendDelegationExecutor<Intent, Action, State, Result, Label>(
            dispatchersProvider
        ) {

            override suspend fun executeIntent(intent: Intent, getState: () -> State) {
                when (intent) {
                    Intent.OnBackClicked -> publish(Label.Back)
                    Intent.OnLogoClicked -> {
                        dispatch(Result.Loader(true))
                        delay(1000)
//                        handleIntent() // можно запустить интент
//                        handleAction() // можно запустить экшн
                        publish(Label.ProcessMessage("Данные загружены"))
                        dispatch(Result.Loader(false))
                    }
                }
            }

            override suspend fun executeAction(action: Action, getState: () -> State) {
                when (action) {
                    Action.Init -> initData()
                }
            }

            override suspend fun processError(error: Throwable) {
                dispatch(Result.Loader(false))
//                val appError = errorHandler.processError(error)
                publish(Label.ProcessMessage("Error"))
            }

            private suspend fun initData() {
                delay(300)
                publish(Label.ProcessMessage("InitData отработал"))
            }
        }
    }
}