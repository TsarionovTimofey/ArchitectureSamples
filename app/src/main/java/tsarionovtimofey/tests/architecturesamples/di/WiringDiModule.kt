package tsarionovtimofey.tests.architecturesamples.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.common.coroutines.DispatchersProvider
import com.common.coroutines.DispatchersProviderImpl
import com.mvi.saving.SavedStateKeeper
import com.mvi.store.UnicastStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WiringDiModule {
    @Provides
    fun provideDispatchersProvider(): DispatchersProvider = DispatchersProviderImpl

    @Provides
    fun provideStoreFactory(): StoreFactory = UnicastStoreFactory
}