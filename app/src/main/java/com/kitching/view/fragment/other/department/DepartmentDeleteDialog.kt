package com.kitching.view.fragment.other.department

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.KitchingApplication
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.view.model.DepartmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentDeleteDialog:
    BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

    private val viewModel = DepartmentViewModel.instance

    private val args: DepartmentDeleteDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageTV.text = "부서를 삭제하시겠습니까?"

            with(confirmButton) {
                text = "삭제"
                throttleClicks(viewLifecycleOwner) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val teamId = PreferencesDataSource(KitchingApplication.getAppContext()).getTeamId() ?: ""
                        viewModel.deleteDepartment(args.departmentId)
                        viewModel.departmentResult.collectLatest {
                            firebaseResultHandler(it) {
                                viewModel.getDepartments(teamId)
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