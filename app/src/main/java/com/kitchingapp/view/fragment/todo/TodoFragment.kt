package com.kitchingapp.view.fragment.todo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.R
import com.kitchingapp.adapter.TodoCategoryAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentTodoBinding
import com.kitchingapp.domain.entities.TodoCategory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.material.selections

class TodoFragment : BaseFragment<FragmentTodoBinding>(FragmentTodoBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
//        childFragmentManager.beginTransaction().replace(R.id.todoChildFragmentContainer, TodoChildFragment()).commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todoCategoriesMockData = listOf(
            TodoCategory("콜드", Color.parseColor("#90CAF9")),
            TodoCategory("핫", Color.parseColor("#EF9A9A"))
        )

        with(binding) {
            with(todoCategoryRV) {
                setRvLayout(this)

                val categoryAdapter = TodoCategoryAdapter()
                categoryAdapter.submitList(todoCategoriesMockData)
                this.adapter = categoryAdapter
            }
        }
    }
}