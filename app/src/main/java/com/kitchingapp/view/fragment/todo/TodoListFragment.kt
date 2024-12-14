package com.kitchingapp.view.fragment.todo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.R
import com.kitchingapp.adapter.TodoAdapter
import com.kitchingapp.adapter.TodoCategoryAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentTodoBinding
import com.kitchingapp.databinding.FragmentTodoListBinding
import com.kitchingapp.domain.entities.Todo
import com.kitchingapp.domain.entities.TodoCategory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.material.selections

class TodoListFragment : BaseFragment<FragmentTodoListBinding>(FragmentTodoListBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todosMockData = listOf(
            Todo("샐러드", TodoCategory("콜드", Color.parseColor("#90CAF9"))),
            Todo("파스타", TodoCategory("핫", Color.parseColor("#EF9A9A")))
        )

        with(binding) {
            with(todoCategoryRV) {
                setRvLayout(this)

                val todoAdapter = TodoAdapter(viewLifecycleOwner)
                todoAdapter.submitList(todosMockData)
                this.adapter = todoAdapter
            }
        }
    }
}