package com.kitchingapp.view.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.view.model.NoticeViewModel

@Suppress("UNCHECKED_CAST")
val noticeViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(NoticeViewModel::class.java) ->
                    NoticeViewModel(RemoteType.FIREBASE)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}