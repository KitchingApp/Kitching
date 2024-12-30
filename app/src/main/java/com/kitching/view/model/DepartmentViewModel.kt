package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OtherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentViewModel(private val repository: OtherRepository = OtherRepository()) : ViewModel() {

    private val _departments = MutableStateFlow<FirebaseResult<MutableList<DepartmentDTO>>>(FirebaseResult.Loading)
    val departments get() = _departments.asStateFlow()

    fun getDepartments(teamId: String) {
        viewModelScope.launch {
            repository.getDepartments(teamId).collectLatest {
                _departments.value = it
            }
        }
    }
}