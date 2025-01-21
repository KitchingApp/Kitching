package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.ScheduleTimeRepositoryImpl
import com.kitching.domain.repository.ScheduleTimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleTimeViewModel(private val repository: ScheduleTimeRepository = ScheduleTimeRepositoryImpl()) : ViewModel() {

    companion object {
        val instance = ScheduleTimeViewModel()
    }

    private var _scheduleTime = MutableStateFlow<FirebaseResult<List<ScheduleTimeListDTO>>>(FirebaseResult.Loading)
    val scheduleTime get() = _scheduleTime.asStateFlow()

    fun getScheduleTimes(teamId: String) {
        viewModelScope.launch {
            repository.getScheduleTimes(teamId).collectLatest {
                _scheduleTime.value = it
            }
        }
    }

    private var _scheduleTimeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val scheduleTimeResult get() = _scheduleTimeResult.asStateFlow()

    fun createScheduleTime(teamId: String, name: String, color: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            repository.createScheduleTime(teamId, name, color, startTime, endTime).collectLatest {
                _scheduleTimeResult.value = it
            }
        }
    }

    fun updateScheduleTime(scheduleTimeId: String, name: String, color: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            repository.updateScheduleTime(scheduleTimeId, name, color, startTime, endTime).collectLatest {
                _scheduleTimeResult.value = it
            }
        }
    }

    fun deleteScheduleTime(scheduleTimeId: String) {
        viewModelScope.launch {
            repository.deleteScheduleTime(scheduleTimeId).collectLatest {
                _scheduleTimeResult.value = it
            }
        }
    }
}