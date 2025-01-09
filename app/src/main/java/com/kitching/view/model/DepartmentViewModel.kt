package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.firebaseFlowHandler
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OtherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentViewModel(private val repository: OtherRepository = OtherRepository()) : ViewModel() {

    companion object {
        val instance by lazy { DepartmentViewModel() }
    }

    private var _departments = MutableStateFlow<FirebaseResult<MutableList<DepartmentDTO>>>(FirebaseResult.Loading)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            repository.getDepartments(teamId).collectLatest {
                _departments.value = it
            }
        }
    }

    private var _createDepartmentResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val createDepartmentResult get() = _createDepartmentResult.asStateFlow()

    fun createDepartment(teamId: String, name: String, color: String) {
        firebaseFlowHandler(_createDepartmentResult) {
            repository.createDepartment(teamId, name, color)
        }
    }

    private var _updateDepartmentResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val updateDepartmentResult get() = _updateDepartmentResult.asStateFlow()

    fun updateDepartment(departmentId: String, name: String, color: String) {
        firebaseFlowHandler(_updateDepartmentResult) {
            repository.updateDepartment(departmentId, name, color)
        }
    }

    private var _deleteDepartmentResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val deleteDepartmentResult get() = _deleteDepartmentResult.asStateFlow()

    fun deleteDepartment(departmentId: String) {
        firebaseFlowHandler(_deleteDepartmentResult) {
            repository.deleteDepartment(departmentId)
        }
    }

    private var _staffLevels = MutableStateFlow<FirebaseResult<MutableList<StaffLevelDTO>>>(FirebaseResult.Loading)
    val staffLevels get() = _staffLevels.asStateFlow()

    fun getStaffLevels(departmentId: String) {
        viewModelScope.launch {
            repository.getStaffLevels(departmentId).collectLatest {
                _staffLevels.value = it
            }
        }
    }

    private var _createStaffLevelResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val createStaffLevelResult get() = _createStaffLevelResult.asStateFlow()

    fun createStaffLevel(departmentId: String, name: String) {
        firebaseFlowHandler(_createStaffLevelResult) {
            repository.createStaffLevel(departmentId, name)
        }
    }

    private var _updateStaffLevelResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val updateStaffLevelResult get() = _updateStaffLevelResult.asStateFlow()

    fun updateStaffLevel(staffLevelId: String, name: String) {
        firebaseFlowHandler(_createStaffLevelResult) {
            repository.updateStaffLevel(staffLevelId, name)
        }
    }

    private var _deleteStaffLevelResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val deleteStaffLevelResult get() = _deleteStaffLevelResult.asStateFlow()

    fun deleteStaffLevel(staffLevelId: String) {
        firebaseFlowHandler(_createStaffLevelResult) {
            repository.deleteStaffLevel(staffLevelId)
        }
    }
}