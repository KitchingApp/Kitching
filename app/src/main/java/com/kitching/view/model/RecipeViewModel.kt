package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.RecipeDetailDTO
import com.kitching.data.repository.RemoteRepository
import com.kitching.data.usecase.RemoteType
import com.kitching.data.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(private val remoteType: RemoteType) : ViewModel() {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _recipeList = MutableStateFlow<MutableList<RecipeDetailDTO>>(mutableListOf<RecipeDetailDTO>())
    val recipeList get() = _recipeList.asStateFlow()

    fun getRecipeList(teamId: String) {
        viewModelScope.launch {
            _recipeList.value = remoteRepository.getRecipeList(teamId)
        }
    }
}