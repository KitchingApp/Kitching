package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.RecipeDetailDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository = RecipeRepository()) : ViewModel() {

    private val _recipeList = MutableStateFlow<FirebaseResult<MutableList<RecipeDetailDTO>>>(FirebaseResult.Loading)
    val recipeList get() = _recipeList.asStateFlow()

    fun getRecipeList(teamId: String) {
        viewModelScope.launch {
            repository.getRecipeList(teamId).collectLatest {
                _recipeList.value = it
            }
        }
    }
}