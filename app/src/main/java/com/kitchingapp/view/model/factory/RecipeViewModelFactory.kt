import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.view.model.RecipeViewModel

@Suppress("UNCHECKED_CAST")
val recipeViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(RecipeViewModel::class.java) ->
                    RecipeViewModel(RemoteType.FIREBASE)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}