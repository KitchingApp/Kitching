package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.radiobutton.MaterialRadioButton
import com.kitching.common.ColorInputBaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.view.model.PrepViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepCategoryUpdateDialog(): ColorInputBaseDialog() {

    private val viewModel = PrepViewModel.instance

    private val args: PrepCategoryUpdateDialogArgs by navArgs()

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
            textInputEditText.setText(args.name)
            colorPickerRG.findViewWithTag<MaterialRadioButton>(args.color).isChecked = true

            with(confirmBtn) {
                text = "수정"

                throttleClicks(viewLifecycleOwner) {
                    viewModel.updatePrepCategory(args.categoryId, getTextInput(), getCheckedColor())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.updatePrepCategoryResult.collectLatest {
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