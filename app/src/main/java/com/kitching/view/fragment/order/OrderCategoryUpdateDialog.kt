package com.kitching.view.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.radiobutton.MaterialRadioButton
import com.kitching.common.ColorInputBaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.view.model.OrderViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class OrderCategoryUpdateDialog: ColorInputBaseDialog() {

    private val viewModel by viewModels<OrderViewModel> {
        viewModelFactory
    }

    private val args: OrderCategoryUpdateDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            textField.hint = "카테고리 이름"
            textInputEditText.setText(args.name)
            colorPickerRG.findViewWithTag<MaterialRadioButton>(args.color).isChecked = true

            with(confirmBtn) {
                text = "수정"

                throttleClicks(viewLifecycleOwner) {
                    observeViewModel()
                }
            }

            with(cancelBtn) {
                throttleClicks(viewLifecycleOwner) {
                    dismiss()
                }
            }

        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            val teamId = PreferencesDataSource(requireContext()).getTeamId().toString()

            viewModel.updateOrderCategory(args.categoryId, getTextInput(), getCheckedColor())
            viewModel.orderCategoryResult.collectLatest {
                firebaseResultHandler(it) {
                    viewModel.getOrderCategory(teamId)
                    dismiss()
                }
            }
        }
    }
}