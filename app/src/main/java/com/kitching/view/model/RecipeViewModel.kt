package com.kitching.view.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.RecipeDetailDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.RecipeRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepositoryImpl = RecipeRepositoryImpl()) : ViewModel() {

    private val _recipeList = MutableStateFlow<FirebaseResult<MutableList<RecipeDetailDTO>>>(FirebaseResult.Loading)
    val recipeList get() = _recipeList.asStateFlow()

    fun getRecipeList(teamId: String) {
        viewModelScope.launch {
            repository.getRecipeList(teamId).collectLatest {
                _recipeList.value = it
            }
        }
    }

    private val _uploadImageResult = MutableStateFlow<FirebaseResult<String>>(FirebaseResult.DummyConstructor)
    val uploadImageResult get() = _uploadImageResult.asStateFlow()

    fun uploadImage(imageUri: Uri, imageName: String) {
        viewModelScope.launch {
            repository.uploadImage(imageUri, imageName).collectLatest {
                _uploadImageResult.value = it
            }
        }
    }

    private val _saveRecipeResult = MutableStateFlow<FirebaseResult<String>>(FirebaseResult.DummyConstructor)
    val saveRecipeResult get() = _saveRecipeResult.asStateFlow()

    fun saveRecipe(name: String, picture: String, steps: List<String>, teamId: String) {
        viewModelScope.launch {
            repository.saveRecipe(name, picture, steps, teamId).collectLatest {
                _saveRecipeResult.value = it
            }
        }
    }

    private val _saveIngredientsResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.DummyConstructor)
    val saveIngredientsResult get() = _saveIngredientsResult.asStateFlow()

    fun saveIngredients(recipeId: String, ingredients: List<Map<String, String>>) {
        viewModelScope.launch {
            repository.saveIngredients(recipeId, ingredients).collectLatest {
                _saveIngredientsResult.value = it
            }
        }
    }
}