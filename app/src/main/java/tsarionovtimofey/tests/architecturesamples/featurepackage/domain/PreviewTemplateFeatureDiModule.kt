package tsarionovtimofey.tests.architecturesamples.featurepackage.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureComponent
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureScope

@Module
@InstallIn(PreviewTemplateFeatureComponent::class)
object PreviewTemplateFeatureDiModule {

    @Provides
    @PreviewTemplateFeatureScope
    internal fun providePreviewTemplateFeatureStore(factory: PreviewTemplateFeatureStoreFactory) =
        factory.create()
}