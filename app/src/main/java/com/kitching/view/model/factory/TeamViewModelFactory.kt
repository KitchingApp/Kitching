package com.kitching.view.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kitching.data.usecase.RemoteType
import com.kitching.view.model.TeamViewModel

@Suppress("UNCHECKED_CAST")
val teamViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(TeamViewModel::class.java) -> TeamViewModel(RemoteType.FIREBASE)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}