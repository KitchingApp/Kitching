package com.kitching.view.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.databinding.DialogCreatePrepBinding
import com.kitching.view.model.OrderViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class OrderListUpdateDialog: BaseDialog<DialogCreatePrepBinding>(DialogCreatePrepBinding::inflate) {

    private val viewModel by viewModels<OrderViewModel> {
        viewModelFactory
    }

    private val args: OrderListUpdateDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            prepNameTIL.hint = "제품 이름"
            prepNameTI.setText(args.orderName)

            with(confirmButton) {
                text = "수정"
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
        viewModel.updateOrder(args.orderId, binding.prepNameTI.text.toString())
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateOrderResult.collectLatest {
                firebaseResultHandler(it) {
                    viewModel.getOrderList(args.categoryId)
                    dismiss()
                }
            }
        }
    }
}