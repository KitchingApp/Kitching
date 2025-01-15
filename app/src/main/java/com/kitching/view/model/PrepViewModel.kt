package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.firebaseFlowHandler
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.PrepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepViewModel(private val repository: PrepRepository = PrepRepository()) : ViewModel() {

    private val _prepCategory = MutableStateFlow<FirebaseResult<MutableList<PrepCategoryDTO>>>(FirebaseResult.Loading)
    val prepCategory get() = _prepCategory.asStateFlow()

    fun getPrepCategory(teamId: String) {
        firebaseFlowHandler(_prepCategory) {
            repository.getPrepCategory(teamId)
        }
    }

    private val _createPrepCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val createPrepCategoryResult get() = _createPrepCategoryResult.asStateFlow()

    fun createPrepCategory(teamId: String, categoryName: String, color: String) {
        firebaseFlowHandler(_createPrepCategoryResult) {
            repository.createPrepCategory(teamId, categoryName, color)
        }
    }

    private val _updatePrepCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val updatePrepCategoryResult get() = _updatePrepCategoryResult.asStateFlow()

    fun updatePrepCategory(categoryId: String, categoryName: String, color: String) {
        firebaseFlowHandler(_updatePrepCategoryResult) {
            repository.updatePrepCategory(categoryId, categoryName, color)
        }
    }

    private val _deletePrepCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val deletePrepCategoryResult get() = _deletePrepCategoryResult.asStateFlow()

    fun deletePrepCategory(scheduleId: String) {
        firebaseFlowHandler(_deletePrepCategoryResult) {
            repository.deletePrepCategory(scheduleId)
        }
    }

    private val _prepList = MutableStateFlow<FirebaseResult<MutableList<PrepDTO>>>(FirebaseResult.Loading)
    val prepList get() = _prepList.asStateFlow()

    fun getPrepList(categoryId: String) {
        firebaseFlowHandler(_prepList) {
            repository.getPrepList(categoryId)
        }
    }

    private val _createPrepResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val createPrepResult get() = _createPrepResult.asStateFlow()

    fun createPrep(categoryId: String, name: String) {
        firebaseFlowHandler(_createPrepResult) {
            repository.createPrep(categoryId, name)
        }
    }

    private val _updatePrepResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val updatePrepResult get() = _updatePrepResult.asStateFlow()

    fun updatePrep(prepId: String, name: String) {
        firebaseFlowHandler(_updatePrepResult) {
            repository.updatePrep(prepId, name)
        }
    }

    private val _deletePrepResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val deletePrepResult get() = _deletePrepResult.asStateFlow()

    fun deletePrep(prepId: String) {
        firebaseFlowHandler(_deletePrepResult) {
            repository.deletePrep(prepId)
        }
    }
}