package com.kitching.view.fragment.prep

import android.R
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.kitching.common.BaseDialog
import com.kitching.common.throttleFirst
import com.kitching.databinding.DialogCreatePrepBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class PrepCreateDialog: BaseDialog<DialogCreatePrepBinding>(DialogCreatePrepBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val recipeListMockData = listOf("김치찌개", "된장찌개", "비빔밥", "김치볶음밥")

        val autoCompleteAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_dropdown_item_1line, recipeListMockData)

        with(binding) {
            autoCompleteTV.setAdapter(autoCompleteAdapter)

            with(confirmButton) {
                text = "생성"
                clicks().onEach {
                    val todoTitle = todoCategoryNameTI.text.toString()
                    val todoRecipe = autoCompleteTV.text.toString()

                    val createData = Bundle().apply {
                        putString("todoTitle", todoTitle)
                        putString("todoRecipe", todoRecipe)
                    }


                    dismiss()
                }.launchIn(lifecycleScope)
            }

            with(cancelButton) {
                clicks().throttleFirst().onEach {
                    dismiss()
                }.launchIn(lifecycleScope)
            }
        }
    }
}