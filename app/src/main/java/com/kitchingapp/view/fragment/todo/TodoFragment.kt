package com.kitchingapp.view.fragment.todo

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.R
import com.kitchingapp.adapter.TodoCategoryAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.common.TODO_CATEGORY_ARGS_REQUEST_KEY
import com.kitchingapp.common.TODO_CATEGORY_COLOR_KEY
import com.kitchingapp.common.TODO_CATEGORY_NAME_KEY
import com.kitchingapp.databinding.FragmentTodoBinding
import com.kitchingapp.domain.entities.TodoCategory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class TodoFragment : BaseFragment<FragmentTodoBinding>(FragmentTodoBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todoCategoriesMockData = mutableListOf(
            TodoCategory("콜드", Color.parseColor("#90CAF9")),
            TodoCategory("핫", Color.parseColor("#EF9A9A"))
        )

        with(binding) {
            with(todoCategoryRV) {
                setRvLayout(this)

                val categoryAdapter = TodoCategoryAdapter(viewLifecycleOwner, navController)
                categoryAdapter.submitList(todoCategoriesMockData)
                this.adapter = categoryAdapter
            }

            with(createTodoCategoryBtn) {
                clicks().onEach {
                    navController.navigate(R.id.createTodoCategoryDialog)
                }.launchIn(lifecycleScope)
            }

            parentFragmentManager.setFragmentResultListener(TODO_CATEGORY_ARGS_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
                val categoryName = bundle.getString(TODO_CATEGORY_NAME_KEY) ?: return@setFragmentResultListener
                val categoryColor = bundle.getString(TODO_CATEGORY_COLOR_KEY)?.let { Color.parseColor(it) } ?: return@setFragmentResultListener

                todoCategoriesMockData.add(TodoCategory(categoryName, categoryColor))
                (todoCategoryRV.adapter as? TodoCategoryAdapter)?.submitList(todoCategoriesMockData.toList())
            }
        }
    }
}