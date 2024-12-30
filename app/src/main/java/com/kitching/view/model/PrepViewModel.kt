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

    private val _prepCategory = MutableStateFlow<FirebaseResult<MutableList<PrepCategoryDTO>>>(FirebaseResult.Loading)
    val prepCategory get() = _prepCategory.asStateFlow()

    fun getPrepCategory(teamId: String) {
        viewModelScope.launch {
            repository.getPrepCategory(teamId).collectLatest {
                _prepCategory.value = it
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