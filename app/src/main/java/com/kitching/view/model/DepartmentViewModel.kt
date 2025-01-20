package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.firebaseFlowHandler
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.DepartmentRepositoryImpl
import com.kitching.data.repository.OtherRepository
import com.kitching.domain.repository.DepartmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentViewModel(private val repository: DepartmentRepository = DepartmentRepositoryImpl()) : ViewModel() {

    companion object {
        val instance by lazy { DepartmentViewModel() }
    }

    private var _departments = MutableStateFlow<FirebaseResult<List<DepartmentDTO>>>(FirebaseResult.Loading)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            repository.getDepartments(teamId).collectLatest {
                _departments.value = it
            }
        }
    }

    private var _departmentResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val departmentResult get() = _departmentResult.asStateFlow()

    fun createDepartment(teamId: String, name: String, color: String) {
        viewModelScope.launch {
            repository.createDepartment(teamId, name, color).collectLatest {
                _departmentResult.value = it
            }
        }
    }

//    private var _updateDepartmentResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val updateDepartmentResult get() = _updateDepartmentResult.asStateFlow()

    fun updateDepartment(departmentId: String, name: String, color: String) {
        viewModelScope.launch {
            repository.updateDepartment(departmentId, name, color).collectLatest {
                _departmentResult.value = it
            }
        }
    }

//    private var _deleteDepartmentResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val deleteDepartmentResult get() = _deleteDepartmentResult.asStateFlow()

    fun deleteDepartment(departmentId: String) {
        viewModelScope.launch {
            repository.deleteDepartment(departmentId).collectLatest {
                _departmentResult.value = it
            }
        }
    }

    private var _staffLevels = MutableStateFlow<FirebaseResult<List<StaffLevelDTO>>>(FirebaseResult.Loading)
    val staffLevels get() = _staffLevels.asStateFlow()

    fun getStaffLevels(departmentId: String) {
        viewModelScope.launch {
            repository.getStaffLevels(departmentId).collectLatest {
                _staffLevels.value = it
            }
        }
    }

    private var _staffLevelResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val staffLevelResult get() = _staffLevelResult.asStateFlow()

    fun createStaffLevel(departmentId: String, name: String) {
        viewModelScope.launch {
            repository.createStaffLevel(departmentId, name).collectLatest {
                _staffLevelResult.value = it
            }
        }
    }

//    private var _updateStaffLevelResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val updateStaffLevelResult get() = _updateStaffLevelResult.asStateFlow()

    fun updateStaffLevel(staffLevelId: String, name: String) {
        viewModelScope.launch {
            repository.updateStaffLevel(staffLevelId, name).collectLatest {
                _staffLevelResult.value = it
            }
        }
    }

//    private var _deleteStaffLevelResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val deleteStaffLevelResult get() = _deleteStaffLevelResult.asStateFlow()

    fun deleteStaffLevel(staffLevelId: String) {
        viewModelScope.launch {
            repository.deleteStaffLevel(staffLevelId).collectLatest {
                _staffLevelResult.value = it
            }
        }
    }
}