package com.kitching.view.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kitching.view.model.DepartmentViewModel
import com.kitching.view.model.NoticeViewModel
import com.kitching.view.model.OrderViewModel
import com.kitching.view.model.PrepViewModel
import com.kitching.view.model.RecipeViewModel
import com.kitching.view.model.ScheduleTimeViewModel
import com.kitching.view.model.ScheduleViewModel
import com.kitching.view.model.StaffLevelViewModel
import com.kitching.view.model.TeamViewModel

@Suppress("UNCHECKED_CAST")
val viewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(DepartmentViewModel::class.java) ->
                    DepartmentViewModel()
                isAssignableFrom(NoticeViewModel::class.java) ->
                    NoticeViewModel()
                isAssignableFrom(OrderViewModel::class.java) ->
                    OrderViewModel()
                isAssignableFrom(PrepViewModel::class.java) ->
                    PrepViewModel()
                isAssignableFrom(RecipeViewModel::class.java) ->
                    RecipeViewModel()
                isAssignableFrom(ScheduleTimeViewModel::class.java) ->
                    ScheduleTimeViewModel()
                isAssignableFrom(ScheduleViewModel::class.java) -> ScheduleViewModel()
                isAssignableFrom(StaffLevelViewModel::class.java) ->
                    StaffLevelViewModel()
                isAssignableFrom(TeamViewModel::class.java) ->
                    TeamViewModel()
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}