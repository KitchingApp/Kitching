package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kitching.common.ColorInputBaseDialog
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.view.model.PrepViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepCategoryCreateDialog(): ColorInputBaseDialog() {

    private val viewModel by viewModels<PrepViewModel> {
        viewModelFactory
    }

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
                            when (it) {
                                is FirebaseResult.Success -> {
                                    viewModel.getPrepCategory(teamId)
                                    dismiss()
                                }
                                is FirebaseResult.Loading -> {

                                }
                                is FirebaseResult.Failure -> {

                                }
                                is FirebaseResult.DummyConstructor -> {

                                }
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