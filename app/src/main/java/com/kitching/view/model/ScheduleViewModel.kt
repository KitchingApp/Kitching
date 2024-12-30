package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.dropDownDepartmentsDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) : ViewModel() {

    private val _schedules = MutableStateFlow<FirebaseResult<List<ScheduleDTO>>>(FirebaseResult.Loading)
    val schedules get() = _schedules.asStateFlow()

    private val _departments =
        MutableStateFlow<FirebaseResult<List<dropDownDepartmentsDTO>>>(FirebaseResult.Loading)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            repository.getDepartmentsForDropDown(teamId).collectLatest {
                _departments.value = it
            }
        }
    }

    fun getSchedules(teamId: String, dateString: String) {
        viewModelScope.launch {
            repository.getSchedules(teamId, dateString).collectLatest {
                _schedules.value = it
            }
        }
    }

    fun rejectSchedule(scheduleId: String) {
        viewModelScope.launch {
            if (repository.deleteSchedule(scheduleId)) getSchedules(
                teamId = "",
                dateString = ""
            )
        }
    }
}