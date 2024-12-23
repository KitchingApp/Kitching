package com.kitchingapp.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(private val remoteType: RemoteType): ViewModel(),
    ViewModelProvider.Factory {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(RemoteType.FIREBASE)
    }

    private val _schedules = MutableStateFlow<List<ScheduleDTO>>(listOf<ScheduleDTO>())
    val schedules get() = _schedules.asStateFlow()

    /** read schedules */
    fun getSchedules(teamId: String, dateString: String) {
        viewModelScope.launch {
            _schedules.value = remoteRepository.getSchedules(teamId, dateString)
        }
    }

    fun rejectSchedule(scheduleId: String) {
        viewModelScope.launch {
            if(remoteRepository.deleteSchedule(scheduleId)) getSchedules(teamId = "", dateString = "")
        }
    }
}