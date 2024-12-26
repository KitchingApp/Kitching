package com.kitchingapp.view.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.view.model.DepartmentViewModel
import com.kitchingapp.view.model.PrepViewModel
import com.kitchingapp.view.model.StaffLevelViewModel

@Suppress("UNCHECKED_CAST")
val staffLevelViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(StaffLevelViewModel::class.java) ->
                    StaffLevelViewModel(RemoteType.FIREBASE)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}