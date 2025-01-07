package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.util.throttleClicks
import com.kitching.common.util.throttleFirst
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.DialogCreatePrepBinding
import com.kitching.view.model.PrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class PrepCreateDialog: BaseDialog<DialogCreatePrepBinding>(DialogCreatePrepBinding::inflate) {
    private val args: PrepCreateDialogArgs by navArgs()

    private val viewModel = PrepViewModel.instance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            prepNameTIL.hint = "할 일 이름"

            with(confirmButton) {
                text = "생성"
                throttleClicks(viewLifecycleOwner) {
                    viewModel.createPrep(args.categoryId, todoCategoryNameTI.text.toString())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.createPrepResult.collectLatest {
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