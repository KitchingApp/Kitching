package com.kitching.view.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.view.model.OrderViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class OrderCategoryDeleteDialog: BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

    private val viewModel by viewModels<OrderViewModel> {
        viewModelFactory
    }

    private val args: OrderCategoryDeleteDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageTV.text = "카테고리를 삭제하시겠습니까?"

            with(confirmButton) {
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
        viewLifecycleOwner.lifecycleScope.launch {
            val teamId = PreferencesDataSource(requireContext()).getTeamId().toString()
            viewModel.deleteOrderCategory(args.categoryId)
            viewModel.orderCategoryResult.collectLatest {
                firebaseResultHandler(it) {
                    viewModel.getOrderCategory(teamId)
                    dismiss()
                }
            }
        }
    }
}