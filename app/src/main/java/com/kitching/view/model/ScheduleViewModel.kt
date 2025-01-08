package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.firebaseFlowHandler
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

class ScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) :
    ViewModel() {

    companion object {
        val instance = ScheduleViewModel()
    }

    private val _departments =
        MutableStateFlow<FirebaseResult<List<DropDownDepartmentsDTO>>>(FirebaseResult.DummyConstructor)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        firebaseFlowHandler(_departments) {
            repository.getDepartmentsForDropDown(teamId)
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
            _fixedSchedules.value = FirebaseResult.Loading
            _appliedSchedules.value = FirebaseResult.Loading

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

    private val _createScheduleResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.DummyConstructor)
    val createScheduleResult get() = _createScheduleResult.asStateFlow()

    fun createSchedule(teamId: String, dateString: String, userId: String, scheduleTimeId: String, isFix: Boolean = true) {
        firebaseFlowHandler(_createScheduleResult) {
            repository.createSchedule(teamId, dateString, userId, scheduleTimeId, isFix)
        }
    }

    private val _applyScheduleResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.DummyConstructor)
    val applyScheduleResult get() = _applyScheduleResult.asStateFlow()

    fun applySchedule(scheduleId: String) {
        firebaseFlowHandler(_applyScheduleResult) {
            repository.applySchedule(scheduleId)
        }
    }

    private val _deleteScheduleResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.DummyConstructor)
    val deleteScheduleResult get() = _deleteScheduleResult.asStateFlow()

    fun deleteSchedule(scheduleId: String, isReject: Boolean = false) {
        firebaseFlowHandler(_deleteScheduleResult) {
            repository.deleteSchedule(scheduleId)
        }
    }

    private val _members =
        MutableStateFlow<FirebaseResult<List<DropDownMembersDTO>>>(FirebaseResult.DummyConstructor)
    val members get() = _members.asStateFlow()

    fun getMembers(teamId: String) {
        firebaseFlowHandler(_members) {
            repository.getMembers(teamId)
        }
    }

    private val _scheduleTimes =
        MutableStateFlow<FirebaseResult<List<ScheduleTimeChipsDTO>>>(FirebaseResult.DummyConstructor)
    val scheduleTimes get() = _scheduleTimes.asStateFlow()

    fun getScheduleTimes(teamId: String) {
        firebaseFlowHandler(_scheduleTimes) {
            repository.getScheduleTimes(teamId)
        }
    }
}