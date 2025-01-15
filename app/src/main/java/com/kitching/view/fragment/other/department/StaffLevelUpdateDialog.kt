package com.kitching.view.fragment.other.department

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.databinding.DialogCreatePrepBinding
import com.kitching.view.model.factory.FactoryDepartmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StaffLevelUpdateDialog: BaseDialog<DialogCreatePrepBinding>(DialogCreatePrepBinding::inflate) {

    private val args: StaffLevelUpdateDialogArgs by navArgs()

    private val viewModel = FactoryDepartmentViewModel.fetchDepartmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            prepNameTIL.hint = "직급 이름"
            prepNameTI.setText(args.name)

            with(confirmButton) {
                text = "수정"
                throttleClicks(viewLifecycleOwner) {
                    viewModel.updateStaffLevel(args.name, prepNameTI.text.toString())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.updateStaffLevelResult.collectLatest {
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