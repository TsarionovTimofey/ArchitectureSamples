package tsarionovtimofey.tests.architecturesamples.featurepackage.ui

import android.os.Bundle
import android.view.View
import com.mvi.binding.viewBinding
import com.mvi.observe
import com.mvi.viewFrom
import com.mvi.viewModelFrom
import dagger.hilt.EntryPoints
import dagger.hilt.android.AndroidEntryPoint
import tsarionovtimofey.tests.architecturesamples.featurepackage.domain.PreviewTemplateFeatureArgs
import com.common.extensions.fragment.initialArguments
import com.common.extensions.fragment.showSnackbarMessage
import com.common.extensions.fragment.withInitialArguments
import tsarionovtimofey.tests.architecturesamples.featurepackage.ui.PreviewTemplateFeatureView.UiLabel
import com.mvi.BaseMviFragment
import tsarionovtimofey.tests.architecturesamples.R
import tsarionovtimofey.tests.architecturesamples.databinding.FragmentPreviewTemplateFeatureBinding
import javax.inject.Inject

@AndroidEntryPoint
class PreviewTemplateFeatureFragment :
    BaseMviFragment<PreviewTemplateFeatureView>(R.layout.fragment_preview_template_feature) {

    @Inject
    lateinit var componentBuilder: PreviewTemplateFeatureComponentBuilder

    override val binder: PreviewTemplateFeatureBinder by viewModelFrom {
        val component = componentBuilder.bindArgs(initialArguments()).build()
        EntryPoints.get(component, PreviewTemplateFeatureEntryPoint::class.java).getBinder()
    }

    override val viewImpl by viewFrom { PreviewTemplateFeatureViewImpl(::binding) }

    private val binding by viewBinding(FragmentPreviewTemplateFeatureBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(binder.labelSubject, ::handleUiLabel)
    }

    private fun handleUiLabel(uiLabel: UiLabel) = when (uiLabel) {
        is UiLabel.ShowMessage -> showSnackbarMessage(uiLabel.message)
    }

    companion object {
        fun newInstance(args: PreviewTemplateFeatureArgs): PreviewTemplateFeatureFragment =
            PreviewTemplateFeatureFragment().withInitialArguments(args)
    }
}