package com.kitchingapp.view.fragment.todo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.kitchingapp.R
import com.kitchingapp.adapter.ScheduleApplyAdapter
import com.kitchingapp.adapter.ScheduleFixAdapter
import com.kitchingapp.adapter.TodoCategoryAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.ChildfragmentCategoryBinding
import com.kitchingapp.databinding.ChildfragmentScheduleDepartmentBinding
import com.kitchingapp.domain.entities.Todo
import com.kitchingapp.domain.entities.TodoCategory

class TodoChildFragment: BaseFragment<ChildfragmentCategoryBinding>(
    ChildfragmentCategoryBinding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val todoMockData = Todo()

        val todoCategoriesMockData = listOf(TodoCategory("콜드", Color.parseColor("#90CAF9")),TodoCategory("핫", Color.parseColor("#EF9A9A")))

        with(binding) {
            with(categoryRV) {
                setRvLayout(this)

                val categoryAdapter = TodoCategoryAdapter()

                categoryAdapter.submitList(todoCategoriesMockData)

                this.adapter = categoryAdapter
            }
        }
    }
}