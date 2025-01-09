package com.kitching.view.fragment.other.department

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.common.util.throttleFirst
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.DialogCreatePrepBinding
import com.kitching.view.model.DepartmentViewModel
import com.kitching.view.model.PrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class StaffLevelUpdateDialog: BaseDialog<DialogCreatePrepBinding>(DialogCreatePrepBinding::inflate) {

    private val args: StaffLevelUpdateDialogArgs by navArgs()

    private val viewModel = DepartmentViewModel.instance

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