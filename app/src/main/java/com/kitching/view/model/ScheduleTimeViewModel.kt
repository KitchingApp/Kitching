package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.repository.RemoteRepository
import com.kitching.data.usecase.RemoteType
import com.kitching.data.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleTimeViewModel(private val remoteType: RemoteType) : ViewModel() {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _scheduleTime = MutableStateFlow<MutableList<ScheduleTimeListDTO>>(mutableListOf<ScheduleTimeListDTO>())
    val scheduleTime get() = _scheduleTime.asStateFlow()

    fun getScheduleTimes(teamId: String) {
        viewModelScope.launch {
            _scheduleTime.value = remoteRepository.getScheduleTimeList(teamId)
        }
    }
}