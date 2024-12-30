package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OtherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StaffLevelViewModel(private val repository: OtherRepository = OtherRepository()) : ViewModel() {

    private val _staffLevels = MutableStateFlow<FirebaseResult<MutableList<StaffLevelDTO>>>(FirebaseResult.Loading)
    val staffLevels get() = _staffLevels.asStateFlow()

    fun getStaffLevels(departmentId: String) {
        viewModelScope.launch {
            repository.getStaffLevels(departmentId).collectLatest {
                _staffLevels.value = it
            }
        }
    }
}