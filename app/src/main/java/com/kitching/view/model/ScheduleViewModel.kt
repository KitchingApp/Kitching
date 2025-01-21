package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.dto.DropDownDepartmentsDTO
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.DepartmentRepositoryImpl
import com.kitching.data.repository.ScheduleRepositoryImpl
import com.kitching.data.repository.ScheduleTimeRepositoryImpl
import com.kitching.data.repository.UserTeamRepositoryImpl
import com.kitching.domain.repository.DepartmentRepository
import com.kitching.domain.repository.ScheduleRepository
import com.kitching.domain.repository.ScheduleTimeRepository
import com.kitching.domain.repository.UserTeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val departmentRepository: DepartmentRepository = DepartmentRepositoryImpl(),
    private val scheduleTimeRepository: ScheduleTimeRepository = ScheduleTimeRepositoryImpl(),
    private val userTeamRepository: UserTeamRepository = UserTeamRepositoryImpl(),
    private val scheduleRepository: ScheduleRepository = ScheduleRepositoryImpl()
) :
    ViewModel() {

    companion object {
        val instance = ScheduleViewModel()
    }

    private var _departments =
        MutableStateFlow<FirebaseResult<List<DropDownDepartmentsDTO>>>(FirebaseResult.DummyConstructor)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            departmentRepository.getDepartmentsForDropdown(teamId).collectLatest { _departments.value = it }
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
            scheduleRepository.getSchedules(teamId, dateString).collectLatest { it ->
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
            scheduleRepository.createSchedule(teamId, dateString, userId, scheduleTimeId, isFix)
                .collectLatest { _scheduleResult.value = it }
        }
    }

    fun applySchedule(scheduleId: String) {
        viewModelScope.launch {
            scheduleRepository.applySchedule(scheduleId).collectLatest { _scheduleResult.value = it }
        }
    }

    fun deleteSchedule(scheduleId: String, isReject: Boolean = false) {
        viewModelScope.launch {
            scheduleRepository.deleteSchedule(scheduleId).collectLatest { _scheduleResult.value = it }
        }
    }

    private val _members =
        MutableStateFlow<FirebaseResult<List<DropDownMembersDTO>>>(FirebaseResult.DummyConstructor)
    val members get() = _members.asStateFlow()

    fun getMembers(teamId: String) {
        viewModelScope.launch {
            userTeamRepository.getAllMembersForSchedule(teamId).collectLatest { _members.value = it }
        }
    }

    private val _scheduleTimes =
        MutableStateFlow<FirebaseResult<List<ScheduleTimeChipsDTO>>>(FirebaseResult.DummyConstructor)
    val scheduleTimes get() = _scheduleTimes.asStateFlow()

    fun getScheduleTimes(teamId: String) {
        viewModelScope.launch {
            scheduleTimeRepository.getScheduleTimesForChips(teamId).collectLatest { _scheduleTimes.value = it }
        }
    }
}