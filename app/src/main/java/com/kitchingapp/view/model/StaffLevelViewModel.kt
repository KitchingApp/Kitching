package com.kitchingapp.view.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitchingapp.data.database.dto.DepartmentDTO
import com.kitchingapp.data.database.dto.PrepCategoryDTO
import com.kitchingapp.data.database.dto.PrepDTO
import com.kitchingapp.data.database.dto.StaffLevelDTO
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
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