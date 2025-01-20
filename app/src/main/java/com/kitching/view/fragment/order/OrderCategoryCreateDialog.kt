package com.kitching.view.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kitching.common.ColorInputBaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.view.model.OrderViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class OrderCategoryCreateDialog : ColorInputBaseDialog(){

    private lateinit var teamId: String

    private val viewModel by viewModels<OrderViewModel> {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTeamId()

        with(binding) {
            textField.hint = "카테고리 이름"

            with(confirmBtn) {
                text = "생성"

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

    private fun getTeamId() {
        viewLifecycleOwner.lifecycleScope.launch {
            teamId = PreferencesDataSource(requireContext()).getTeamId().orEmpty()
        }
    }

    private fun observeViewModel() {
//        val categoryName = getTextInput()
//        val color = getCheckedColor()
//
//        if (categoryName.isBlank()) {
//            commonToast("카테고리 이름을 입력해주세요.")
//            return
//        }

        viewModel.createOrderCategory(teamId, getTextInput(), getCheckedColor())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderCategoryResult.collectLatest {
                firebaseResultHandler(it) {
                    viewModel.getOrderCategory(teamId)
                    dismiss()
                }
            }
        }
    }
}