package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kitching.common.ColorInputBaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.view.model.factory.FactoryPrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepCategoryCreateDialog(): ColorInputBaseDialog() {

    private val viewModel = FactoryPrepViewModel.fetchPrepViewModel()

    private lateinit var teamId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
            }
        }

        with(binding) {
            textField.hint = "카테고리 이름"

            with(confirmBtn) {
                text = "생성"

                throttleClicks(viewLifecycleOwner) {
                    viewModel.createPrepCategory(teamId, getTextInput(), getCheckedColor())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.createPrepCategoryResult.collectLatest {
                            firebaseResultHandler(it) {
                                viewModel.getPrepCategory(teamId)
                                dismiss()
                            }
                        }
                    }
                }
            }

            with(cancelBtn) {
                throttleClicks(viewLifecycleOwner) {
                    dismiss()
                }
            }
        }
    }
}