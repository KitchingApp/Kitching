package com.kitchingapp.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.data.database.dto.dropDownDepartmentsDTO
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(private val remoteType: RemoteType) : ViewModel(),
    ViewModelProvider.Factory {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _schedules = MutableStateFlow<List<ScheduleDTO>>(listOf<ScheduleDTO>())
    val schedules get() = _schedules.asStateFlow()

    private val _schedulesByDepartment = MutableStateFlow<List<ScheduleDTO>>(listOf<ScheduleDTO>())
    val schedulesByDepartment get() = _schedulesByDepartment.asStateFlow()

    private val _departments =
        MutableStateFlow<List<dropDownDepartmentsDTO>>(listOf<dropDownDepartmentsDTO>())
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            _departments.value = remoteRepository.getDepartmentsForDropDown(teamId)
        }
    }

    fun getSchedules(teamId: String, dateString: String) {
        viewModelScope.launch {
            _schedules.value = remoteRepository.getSchedules(teamId, dateString)
        }
    }

    fun selectDepartment(departmentName: String) {
        viewModelScope.launch {
            _schedulesByDepartment.value =
                schedules.value.filter { it.departmentName == departmentName }
        }
    }

    fun rejectSchedule(scheduleId: String) {
        viewModelScope.launch {
            if (remoteRepository.deleteSchedule(scheduleId)) getSchedules(
                teamId = "",
                dateString = ""
            )
        }
    }
}