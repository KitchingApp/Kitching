package com.kitchingapp.view.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.view.model.DepartmentViewModel
import com.kitchingapp.view.model.PrepViewModel

@Suppress("UNCHECKED_CAST")
val departmentViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(DepartmentViewModel::class.java) ->
                    DepartmentViewModel(RemoteType.FIREBASE)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}