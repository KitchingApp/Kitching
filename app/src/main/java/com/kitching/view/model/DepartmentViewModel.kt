package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.DepartmentRepositoryImpl
import com.kitching.data.repository.StaffLevelRepositoryImpl
import com.kitching.domain.repository.DepartmentRepository
import com.kitching.domain.repository.StaffLevelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentViewModel(
    private val departmentRepository: DepartmentRepository = DepartmentRepositoryImpl(),
    private val staffLevelRepository: StaffLevelRepository = StaffLevelRepositoryImpl()
) : ViewModel() {

    companion object {
        val instance by lazy { DepartmentViewModel() }
    }

    private var _departments = MutableStateFlow<FirebaseResult<List<DepartmentDTO>>>(FirebaseResult.Loading)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            departmentRepository.getDepartments(teamId).collectLatest {
                _departments.value = it
            }
        }
    }

    private var _departmentResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val departmentResult get() = _departmentResult.asStateFlow()

    fun createDepartment(teamId: String, name: String, color: String) {
        viewModelScope.launch {
            departmentRepository.createDepartment(teamId, name, color).collectLatest {
                _departmentResult.value = it
            }
        }
    }

    fun updateDepartment(departmentId: String, name: String, color: String) {
        viewModelScope.launch {
            departmentRepository.updateDepartment(departmentId, name, color).collectLatest {
                _departmentResult.value = it
            }
        }
    }

    fun deleteDepartment(departmentId: String) {
        viewModelScope.launch {
            departmentRepository.deleteDepartment(departmentId).collectLatest {
                _departmentResult.value = it
            }
        }
    }

    private var _staffLevels = MutableStateFlow<FirebaseResult<List<StaffLevelDTO>>>(FirebaseResult.Loading)
    val staffLevels get() = _staffLevels.asStateFlow()

    fun getStaffLevels(departmentId: String) {
        viewModelScope.launch {
            staffLevelRepository.getStaffLevels(departmentId).collectLatest {
                _staffLevels.value = it
            }
        }
    }

    private var _staffLevelResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val staffLevelResult get() = _staffLevelResult.asStateFlow()

    fun createStaffLevel(departmentId: String, name: String) {
        viewModelScope.launch {
            staffLevelRepository.createStaffLevel(departmentId, name).collectLatest {
                _staffLevelResult.value = it
            }
        }
    }

    fun updateStaffLevel(staffLevelId: String, name: String) {
        viewModelScope.launch {
            staffLevelRepository.updateStaffLevel(staffLevelId, name).collectLatest {
                _staffLevelResult.value = it
            }
        }
    }

    fun deleteStaffLevel(staffLevelId: String) {
        viewModelScope.launch {
            staffLevelRepository.deleteStaffLevel(staffLevelId).collectLatest {
                _staffLevelResult.value = it
            }
        }
    }
}