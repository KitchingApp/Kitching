package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.firebaseFlowHandler
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.ScheduleRepositoryImpl
import com.kitching.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepositoryImpl()) :
    ViewModel() {

    companion object {
        val instance = ScheduleViewModel()
    }

    private var _departments =
        MutableStateFlow<FirebaseResult<List<DropDownDepartmentsDTO>>>(FirebaseResult.DummyConstructor)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            repository.getDepartments(teamId).collectLatest { _departments.value = it }
        }
    }

    private val _fixedSchedules =
        MutableStateFlow<FirebaseResult<List<ScheduleDTO>>>(FirebaseResult.DummyConstructor)
    val fixedSchedules get() = _fixedSchedules.asStateFlow()

    private val _appliedSchedules =
        MutableStateFlow<FirebaseResult<List<ScheduleDTO>>>(FirebaseResult.DummyConstructor)
    val appliedSchedules = _appliedSchedules.asStateFlow()

    fun getSchedules(teamId: String, dateString: String) {
        viewModelScope.launch {
            repository.getSchedules(teamId, dateString).collectLatest { it ->
                when (it) {
                    is FirebaseResult.Success -> {
                        _fixedSchedules.value = FirebaseResult.Success(it.data.filter { it.isFix })
                        _appliedSchedules.value =
                            FirebaseResult.Success(it.data.filter { !it.isFix })
                    }

                    else -> {
                        _fixedSchedules.value = it
                        _appliedSchedules.value = it
                    }
                }
            }
        }
    }

    private val _scheduleResult =
        MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Success(true))
    val scheduleResult get() = _scheduleResult.asStateFlow()

    fun createSchedule(
        teamId: String,
        dateString: String,
        userId: String,
        scheduleTimeId: String,
        isFix: Boolean = true
    ) {
        viewModelScope.launch {
            repository.createSchedule(teamId, dateString, userId, scheduleTimeId, isFix)
                .collectLatest { _scheduleResult.value = it }
        }
    }

//    private val _applyScheduleResult =
//        MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.DummyConstructor)
//    val applyScheduleResult get() = _applyScheduleResult.asStateFlow()

    fun applySchedule(scheduleId: String) {
        viewModelScope.launch {
            repository.applySchedule(scheduleId).collectLatest { _scheduleResult.value = it }
        }
    }

//    private val _deleteScheduleResult =
//        MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.DummyConstructor)
//    val deleteScheduleResult get() = _deleteScheduleResult.asStateFlow()

    fun deleteSchedule(scheduleId: String, isReject: Boolean = false) {
        viewModelScope.launch {
            repository.deleteSchedule(scheduleId).collectLatest { _scheduleResult.value = it }
        }
    }

    private val _members =
        MutableStateFlow<FirebaseResult<List<DropDownMembersDTO>>>(FirebaseResult.DummyConstructor)
    val members get() = _members.asStateFlow()

    fun getMembers(teamId: String) {
        viewModelScope.launch {
            repository.getMembers(teamId).collectLatest { _members.value = it }
        }
    }

    private val _scheduleTimes =
        MutableStateFlow<FirebaseResult<List<ScheduleTimeChipsDTO>>>(FirebaseResult.DummyConstructor)
    val scheduleTimes get() = _scheduleTimes.asStateFlow()

    fun getScheduleTimes(teamId: String) {
        viewModelScope.launch {
            repository.getScheduleTimes(teamId).collectLatest { _scheduleTimes.value = it }
        }
    }
}