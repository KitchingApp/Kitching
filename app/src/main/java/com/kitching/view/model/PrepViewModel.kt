package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.firebaseFlowHandler
import com.kitching.data.dto.PrepCategoryDTO
import com.kitching.data.dto.PrepDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.PrepCategoryRepositoryImpl
import com.kitching.data.repository.PrepRepositoryImpl
import com.kitching.domain.repository.PrepCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepViewModel(
    private val prepCategoryRepository: PrepCategoryRepository = PrepCategoryRepositoryImpl(),
    private val prepRepository: PrepRepositoryImpl = PrepRepositoryImpl()
) : ViewModel() {

    companion object {
        val instance = PrepViewModel()
    }

    private val _prepCategory = MutableStateFlow<FirebaseResult<List<PrepCategoryDTO>>>(FirebaseResult.Loading)
    val prepCategory get() = _prepCategory.asStateFlow()

    fun getPrepCategory(teamId: String) {
        viewModelScope.launch {
            prepCategoryRepository.getPrepCategory(teamId).collectLatest {
                _prepCategory.value = it
            }
        }
    }

    private val _prepCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val prepCategoryResult get() = _prepCategoryResult.asStateFlow()

    fun createPrepCategory(teamId: String, categoryName: String, color: String) {
        viewModelScope.launch {
            prepCategoryRepository.createPrepCategory(teamId, categoryName, color).collectLatest {
                _prepCategoryResult.value = it
            }
        }
    }

    fun updatePrepCategory(categoryId: String, categoryName: String, color: String) {
        viewModelScope.launch {
            prepCategoryRepository.updatePrepCategory(categoryId, categoryName, color).collectLatest {
                _prepCategoryResult.value = it
            }
        }
    }

    fun deletePrepCategory(categoryId: String) {
        viewModelScope.launch {
            prepCategoryRepository.deletePrepCategory(categoryId).collectLatest {
                _prepCategoryResult.value = it
            }
        }
    }

    private val _prepList = MutableStateFlow<FirebaseResult<List<PrepDTO>>>(FirebaseResult.Loading)
    val prepList get() = _prepList.asStateFlow()

    fun getPrepList(categoryId: String) {
        viewModelScope.launch {
            prepRepository.getPrepList(categoryId).collectLatest {
                _prepList.value = it
            }
        }
    }

    private val _prepResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val prepResult get() = _prepResult.asStateFlow()

    fun createPrep(categoryId: String, name: String) {
        viewModelScope.launch {
            prepRepository.createPrep(categoryId, name).collectLatest {
                _prepResult.value = it
            }
        }
    }

    fun updatePrep(prepId: String, name: String) {
        viewModelScope.launch {
            prepRepository.updatePrep(prepId, name).collectLatest {
                _prepResult.value = it
            }
        }
    }

    fun deletePrep(prepId: String) {
        viewModelScope.launch {
            prepRepository.deletePrep(prepId).collectLatest {
                _prepResult.value = it
            }
        }
    }
}