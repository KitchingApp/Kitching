package com.kitching.view.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.view.model.OrderViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class OrderListDeleteDialog: BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

    private val viewModel by viewModels<OrderViewModel> {
        viewModelFactory
    }

    private val args: OrderListDeleteDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageTV.text = "발주목록을 삭제하시겠습니까?"

            with(confirmButton) {
                text = "삭제"

                throttleClicks(viewLifecycleOwner) {
                    observeViewModel()
                }

            }

            with(cancelButton) {
                throttleClicks(viewLifecycleOwner) {
                    dismiss()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.deleteOrder(args.orderId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderResult.collectLatest {
                firebaseResultHandler(it) {
                    viewModel.getOrderList(args.categoryId)
                    dismiss()
                }
            }
        }
    }
}