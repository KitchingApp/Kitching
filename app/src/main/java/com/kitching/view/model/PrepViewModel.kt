package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.PrepRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepViewModel(private val repository: PrepRepository = PrepRepository()) : ViewModel() {

    companion object {
        val instance = PrepViewModel()
    }

    private val _prepCategory = MutableStateFlow<FirebaseResult<MutableList<PrepCategoryDTO>>>(FirebaseResult.Loading)
    val prepCategory get() = _prepCategory.asStateFlow()

    fun getPrepCategory(teamId: String) {
        viewModelScope.launch {
            repository.getPrepCategory(teamId).collectLatest {
                _prepCategory.value = it
            }
        }
    }

    private val _createPrepCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val createPrepCategoryResult get() = _createPrepCategoryResult.asStateFlow()

    fun createPrepCategory(teamId: String, categoryName: String, color: String) {
        viewModelScope.launch {
            repository.createPrepCategory(teamId, categoryName, color).collectLatest {
                _createPrepCategoryResult.value = it
            }
        }
    }

    private val _updatePrepCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val updatePrepCategoryResult get() = _updatePrepCategoryResult.asStateFlow()

    fun updatePrepCategory(categoryId: String, categoryName: String, color: String) {
        viewModelScope.launch {
            repository.updatePrepCategory(categoryId, categoryName, color).collectLatest {
                _updatePrepCategoryResult.value = it
            }
        }
    }

    private val _deletePrepCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val deletePrepCategoryResult get() = _deletePrepCategoryResult.asStateFlow()

    fun deletePrepCategory(scheduleId: String) {
        viewModelScope.launch {
            repository.deletePrepCategory(scheduleId).collectLatest {
                _deletePrepCategoryResult.value = it
            }
        }
    }

    private val _prepList = MutableStateFlow<FirebaseResult<MutableList<PrepDTO>>>(FirebaseResult.Loading)
    val prepList get() = _prepList.asStateFlow()

    fun getPrepList(categoryId: String) {
        viewModelScope.launch {
            repository.getPrepList(categoryId).collectLatest {
                _prepList.value = it
            }
        }
    }
}