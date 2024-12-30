package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OtherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleTimeViewModel(private val repository: OtherRepository = OtherRepository()) : ViewModel() {

    private val _scheduleTime = MutableStateFlow<FirebaseResult<MutableList<ScheduleTimeListDTO>>>(FirebaseResult.Loading)
    val scheduleTime get() = _scheduleTime.asStateFlow()

    fun getScheduleTimes(teamId: String) {
        viewModelScope.launch {
            repository.getScheduleTimeList(teamId).collectLatest {
                _scheduleTime.value = it
            }
        }
    }
}