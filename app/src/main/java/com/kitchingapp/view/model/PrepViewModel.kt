package com.kitchingapp.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitchingapp.data.database.dto.PrepCategoryDTO
import com.kitchingapp.data.database.dto.PrepDTO
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PrepViewModel(private val remoteType: RemoteType) : ViewModel() {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _prepCategory = MutableStateFlow<MutableList<PrepCategoryDTO>>(mutableListOf<PrepCategoryDTO>())
    val prepCategory get() = _prepCategory.asStateFlow()

    fun getPrepCategory(teamId: String) {
        viewModelScope.launch {
            _prepCategory.value = remoteRepository.getPrepCategory(teamId)
        }
    }

    private val _prepList = MutableStateFlow<MutableList<PrepDTO>>(mutableListOf<PrepDTO>())
    val prepList get() = _prepList.asStateFlow()

    fun getPrepList(categoryId: String) {
        viewModelScope.launch {
            _prepList.value = remoteRepository.getPrepList(categoryId)
        }
    }
}