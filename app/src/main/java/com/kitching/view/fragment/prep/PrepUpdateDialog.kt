package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.databinding.DialogCreatePrepBinding
import com.kitching.view.model.factory.FactoryPrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepUpdateDialog: BaseDialog<DialogCreatePrepBinding>(DialogCreatePrepBinding::inflate) {
    private val args: PrepUpdateDialogArgs by navArgs()

    private val viewModel = FactoryPrepViewModel.fetchPrepViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            prepNameTIL.hint = "할 일 이름"
            prepNameTI.setText(args.prepName)

            with(confirmButton) {
                text = "수정"
                throttleClicks(viewLifecycleOwner) {
                    viewModel.updatePrep(args.prepId, prepNameTI.text.toString())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.updatePrepResult.collectLatest {
                            firebaseResultHandler(it) {
                                viewModel.getPrepList(args.categoryId)
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