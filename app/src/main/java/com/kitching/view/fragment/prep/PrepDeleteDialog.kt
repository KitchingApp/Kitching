package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.KitchingApplication
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.view.model.PrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepDeleteDialog:
    BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

    private val viewModel = PrepViewModel.instance

    private val args: PrepDeleteDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageTV.text = "할일을 삭제하시겠습니까?"

            with(confirmButton) {
                text = "삭제"
                throttleClicks(viewLifecycleOwner) {
                    viewModel.deletePrep(args.prepId)
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.deletePrepResult.collectLatest {
                            when(it) {
                                is FirebaseResult.Success -> {
                                    viewModel.getPrepList(args.categoryId)
                                    dismiss()
                                }
                                is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                                is FirebaseResult.Failure -> {} // TODO("예외 처리")
                                is FirebaseResult.DummyConstructor -> {} // TODO()
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