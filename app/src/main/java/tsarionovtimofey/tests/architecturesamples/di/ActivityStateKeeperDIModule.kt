package tsarionovtimofey.tests.architecturesamples.di

import android.app.Activity
import androidx.savedstate.SavedStateRegistryOwner
import com.mvi.saving.ActivityStateKeeper
import com.mvi.saving.CommonSavedStateKeeper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityStateKeeperDIModule {

    @Provides
    @ActivityScoped
    fun provideSavedStateKeeper(activity: Activity): ActivityStateKeeper =
        ActivityStateKeeper(CommonSavedStateKeeper(activity as SavedStateRegistryOwner))
}
