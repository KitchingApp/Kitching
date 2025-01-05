package com.kitching.view.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) : ViewModel() {

    companion object {
        val instance = ScheduleViewModel()
    }

    private val _schedules = MutableStateFlow<FirebaseResult<List<ScheduleDTO>>>(FirebaseResult.DummyConstructor)
    val schedules get() = _schedules.asStateFlow()

    private val _departments =
        MutableStateFlow<FirebaseResult<List<DropDownDepartmentsDTO>>>(FirebaseResult.DummyConstructor)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            _departments.value = FirebaseResult.Loading
            repository.getDepartmentsForDropDown(teamId).collectLatest {
                _departments.value = it
            }
        }
    }

    fun getSchedules(teamId: String, dateString: String) {
        viewModelScope.launch {
            _schedules.value = FirebaseResult.Loading
            repository.getSchedules(teamId, dateString).collectLatest {
                _schedules.value = it
            }
        }
    }

    fun rejectSchedule(scheduleId: String) {
        viewModelScope.launch {
//            if (repository.deleteSchedule(scheduleId)) getSchedules(
//                teamId = "",
//                dateString = ""
//            )
        }
    }

    private val _members = MutableStateFlow<FirebaseResult<List<DropDownMembersDTO>>>(FirebaseResult.DummyConstructor)
    val members get() = _members.asStateFlow()

    fun getMembers(teamId: String) {
        viewModelScope.launch {
            _members.value = FirebaseResult.Loading
            repository.getMembers(teamId).collectLatest {
                _members.value = it
            }
        }
    }

    private val _scheduleTimes = MutableStateFlow<FirebaseResult<List<ScheduleTimeChipsDTO>>>(FirebaseResult.DummyConstructor)
    val scheduleTimes get() = _scheduleTimes.asStateFlow()

    fun getScheduleTimes(teamId: String) {
        viewModelScope.launch {
            _scheduleTimes.value = FirebaseResult.Loading
            repository.getScheduleTimes(teamId).collectLatest {
                _scheduleTimes.value = it
            }
        }
    }
}