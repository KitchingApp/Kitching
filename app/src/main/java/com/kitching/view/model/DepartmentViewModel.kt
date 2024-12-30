package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.repository.RemoteRepository
import com.kitching.data.usecase.RemoteType
import com.kitching.data.usecase.RemoteTypeUseCase
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