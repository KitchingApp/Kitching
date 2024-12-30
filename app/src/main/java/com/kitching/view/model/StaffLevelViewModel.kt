package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.repository.RemoteRepository
import com.kitching.data.usecase.RemoteType
import com.kitching.data.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaffLevelViewModel(private val remoteType: RemoteType) : ViewModel() {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _staffLevels = MutableStateFlow<MutableList<StaffLevelDTO>>(mutableListOf<StaffLevelDTO>())
    val staffLevels get() = _staffLevels.asStateFlow()

    fun getStaffLevels(departmentId: String) {
        viewModelScope.launch {
            _staffLevels.value = remoteRepository.getStaffLevels(departmentId)
        }
    }
}