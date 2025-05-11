package tsarionovtimofey.tests.architecturesamples.featurepackage.ui

import android.content.Context
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureArgs
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.model.PreviewTemplateFeatureUiMapper
import com.mvi.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Provider
import javax.inject.Scope

@Module
@InstallIn(FragmentComponent::class)
object PreviewTemplateFeatureDiModule {
    @Provides
    @FragmentScoped
    fun provideVMFactory(provider: Provider<PreviewTemplateFeatureBinder>): ViewModelFactory<PreviewTemplateFeatureBinder> =
        ViewModelFactory(provider)

    @Provides
    @FragmentScoped
    fun provideStateToModelMapper(@ApplicationContext context: Context): PreviewTemplateFeatureUiMapper =
        PreviewTemplateFeatureUiMapper(context)
}

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class PreviewTemplateFeatureScope

@PreviewTemplateFeatureScope
@DefineComponent(parent = FragmentComponent::class)
interface PreviewTemplateFeatureComponent

@DefineComponent.Builder
interface PreviewTemplateFeatureComponentBuilder {
    fun bindArgs(@BindsInstance args: PreviewTemplateFeatureArgs): PreviewTemplateFeatureComponentBuilder
    fun build(): PreviewTemplateFeatureComponent
}

@EntryPoint
@InstallIn(PreviewTemplateFeatureComponent::class)
interface PreviewTemplateFeatureEntryPoint {
    fun getBinder(): Provider<PreviewTemplateFeatureBinder>
}