package com.kitchingapp.view.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.view.model.DepartmentViewModel
import com.kitchingapp.view.model.NoticeViewModel
import com.kitchingapp.view.model.OrderViewModel
import com.kitchingapp.view.model.PrepViewModel
import com.kitchingapp.view.model.RecipeViewModel
import com.kitchingapp.view.model.ScheduleTimeViewModel
import com.kitchingapp.view.model.ScheduleViewModel
import com.kitchingapp.view.model.StaffLevelViewModel

@Suppress("UNCHECKED_CAST")
val viewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(DepartmentViewModel::class.java) ->
                    DepartmentViewModel(RemoteType.FIREBASE)
                isAssignableFrom(NoticeViewModel::class.java) ->
                    NoticeViewModel(RemoteType.FIREBASE)
                isAssignableFrom(OrderViewModel::class.java) ->
                    OrderViewModel(RemoteType.FIREBASE)
                isAssignableFrom(PrepViewModel::class.java) ->
                    PrepViewModel(RemoteType.FIREBASE)
                isAssignableFrom(RecipeViewModel::class.java) ->
                    RecipeViewModel(RemoteType.FIREBASE)
                isAssignableFrom(ScheduleTimeViewModel::class.java) ->
                    ScheduleTimeViewModel(RemoteType.FIREBASE)
                isAssignableFrom(ScheduleViewModel::class.java) -> ScheduleViewModel(RemoteType.FIREBASE)
                isAssignableFrom(StaffLevelViewModel::class.java) ->
                    StaffLevelViewModel(RemoteType.FIREBASE)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}