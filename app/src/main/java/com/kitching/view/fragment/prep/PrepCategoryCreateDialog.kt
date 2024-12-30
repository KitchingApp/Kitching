package com.kitching.view.fragment.prep

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.kitching.common.ColorInputBaseDialog
import com.kitching.common.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class PrepCategoryCreateDialog(private val initialName: String, private val initialColor: Int): ColorInputBaseDialog() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            textField.hint = "카테고리 이름"

            with(confirmBtn) {
                text = "생성"
            }

            with(cancelBtn) {
                clicks().throttleFirst().onEach {
                    dismiss()
                }.launchIn(lifecycleScope)
            }
        }
    }
}