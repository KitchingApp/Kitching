package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.commonToast
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

    private val _fixedSchedules =
        MutableStateFlow<FirebaseResult<List<ScheduleDTO>>>(FirebaseResult.DummyConstructor)
    val fixedSchedules get() = _fixedSchedules.asStateFlow()

    private val _appliedSchedules =
        MutableStateFlow<FirebaseResult<List<ScheduleDTO>>>(FirebaseResult.DummyConstructor)
    val appliedSchedules = _appliedSchedules.asStateFlow()

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

    fun applySchedule(scheduleId: String) {
        viewModelScope.launch {
            if (!repository.applySchedule(scheduleId)) {
                commonToast("스케줄 승인 실패")
            }
        }
    }

    fun deleteSchedule(scheduleId: String, isReject: Boolean = false) {
        viewModelScope.launch {
            if (!repository.deleteSchedule(scheduleId)) {
                if (isReject) {
                    commonToast("스케줄 반려 실패")
                } else {
                    commonToast("스케줄 삭제 실패")
                }
            }
        }
    }

    private val _members =
        MutableStateFlow<FirebaseResult<List<DropDownMembersDTO>>>(FirebaseResult.DummyConstructor)
    val members get() = _members.asStateFlow()

    fun getMembers(teamId: String) {
        viewModelScope.launch {
            _members.value = FirebaseResult.Loading
            repository.getMembers(teamId).collectLatest {
                _members.value = it
            }
        }
    }

    private val _scheduleTimes =
        MutableStateFlow<FirebaseResult<List<ScheduleTimeChipsDTO>>>(FirebaseResult.DummyConstructor)
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