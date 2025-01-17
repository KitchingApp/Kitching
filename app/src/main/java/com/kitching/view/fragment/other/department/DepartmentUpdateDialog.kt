package com.kitching.view.fragment.other.department

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.radiobutton.MaterialRadioButton
import com.kitching.common.ColorInputBaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.view.model.DepartmentViewModel
import com.kitching.view.model.PrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentUpdateDialog(): ColorInputBaseDialog() {

    private val viewModel = DepartmentViewModel.instance

    private val args: DepartmentUpdateDialogArgs by navArgs()

    private lateinit var teamId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
            }
        }

        with(binding) {
            textField.hint = "부서 이름"
            textInputEditText.setText(args.name)
            colorPickerRG.findViewWithTag<MaterialRadioButton>(args.color).isChecked = true

            with(confirmBtn) {
                text = "수정"

                throttleClicks(viewLifecycleOwner) {
                    viewModel.updateDepartment(args.departmentId, getTextInput(), getCheckedColor())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.departmentResult.collectLatest {
                            firebaseResultHandler(it) {
                                viewModel.getDepartments(teamId)
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