package com.kitching.view.fragment.other.department

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.databinding.DialogCreatePrepBinding
import com.kitching.view.model.DepartmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StaffLevelCreateDialog: BaseDialog<DialogCreatePrepBinding>(DialogCreatePrepBinding::inflate) {
    private val args: StaffLevelCreateDialogArgs by navArgs()

    private val viewModel = DepartmentViewModel.instance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            prepNameTIL.hint = "직급 이름"

            with(confirmButton) {
                text = "생성"
                throttleClicks(viewLifecycleOwner) {
                    viewModel.createStaffLevel(args.departmentId, prepNameTI.text.toString())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.staffLevelResult.collectLatest {
                            firebaseResultHandler(it) {
                                viewModel.getStaffLevels(args.departmentId)
                                dismiss()
                            }
                        }
                    }
                }
            }

            with(cancelButton) {
                throttleClicks(viewLifecycleOwner) {
                    dismiss()
                }
            }
        }
    }
}