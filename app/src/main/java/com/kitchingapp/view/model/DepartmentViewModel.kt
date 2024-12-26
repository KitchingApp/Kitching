package com.kitchingapp.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitchingapp.data.database.dto.DepartmentDTO
import com.kitchingapp.data.database.dto.PrepCategoryDTO
import com.kitchingapp.data.database.dto.PrepDTO
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DepartmentViewModel(private val remoteType: RemoteType) : ViewModel() {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _departments = MutableStateFlow<MutableList<DepartmentDTO>>(mutableListOf<DepartmentDTO>())
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            _departments.value = remoteRepository.getDepartments(teamId)
        }
    }
}