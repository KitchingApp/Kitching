package com.kitching.view.fragment.other.department

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.view.model.factory.FactoryDepartmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StaffLevelDeleteDialog:
    BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

    private val viewModel = FactoryDepartmentViewModel.fetchDepartmentViewModel()

    private val args: StaffLevelDeleteDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageTV.text = "직급을 삭제하시겠습니까?"

            with(confirmButton) {
                text = "삭제"
                throttleClicks(viewLifecycleOwner) {
                    viewModel.deleteStaffLevel(args.departmentId)
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.deleteStaffLevelResult.collectLatest {
                            firebaseResultHandler(it) {
                                viewModel.getStaffLevels(args.departmentId)
                                dismiss()
                            }
                        }
                        dismiss()
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