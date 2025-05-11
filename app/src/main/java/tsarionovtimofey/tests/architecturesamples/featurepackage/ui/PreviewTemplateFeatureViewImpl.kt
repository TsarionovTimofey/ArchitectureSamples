package tsarionovtimofey.tests.architecturesamples.featurepackage.ui

import androidx.core.view.isVisible
import com.mvi.binding.BindingView
import tsarionovtimofey.tests.architecturesamples.databinding.FragmentPreviewTemplateFeatureBinding
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.Event
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.Model

class PreviewTemplateFeatureViewImpl(
    binding: () -> FragmentPreviewTemplateFeatureBinding
) : BindingView<FragmentPreviewTemplateFeatureBinding, Model, Event>(binding),
    PreviewTemplateFeatureView {

    init {
        with(binding()) {
            actionBar.progressBar.root.isVisible = false
            actionBar.ivTitleLogo.setOnClickListener {
                dispatch(Event.OnLogoClicked)
            }
        }
    }

    override fun render(binding: FragmentPreviewTemplateFeatureBinding, model: Model) = model.run {
        with(binding) {
            actionBar.progressBar.root.isVisible = model.isLoading
        }
    }
}