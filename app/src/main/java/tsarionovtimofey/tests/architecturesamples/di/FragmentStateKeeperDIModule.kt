package tsarionovtimofey.tests.architecturesamples.di

import androidx.fragment.app.Fragment
import com.mvi.saving.CommonSavedStateKeeper
import com.mvi.saving.SavedStateKeeper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object FragmentStateKeeperDIModule {
    @Provides
    @FragmentScoped
    internal fun provideSavedStateKeeper(fragment: Fragment): SavedStateKeeper =
        CommonSavedStateKeeper(fragment)
}
