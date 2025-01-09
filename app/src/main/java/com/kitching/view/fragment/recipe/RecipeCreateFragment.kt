package com.kitching.view.fragment.recipe

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kitching.common.BaseFragment
import com.kitching.common.commonToast
import com.kitching.common.util.throttleClicks
import com.kitching.databinding.FragmentCreateRecipeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.kitching.R
import com.kitching.common.firebaseResultHandler
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.view.model.RecipeViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.util.UUID
import kotlin.getValue

class RecipeCreateFragment: BaseFragment<FragmentCreateRecipeBinding>(FragmentCreateRecipeBinding::inflate) {
    private lateinit var navController: NavController
    private val viewModel by viewModels<RecipeViewModel> {
        viewModelFactory
    }

    @RequiresApi(Build.VERSION_CODES.O)

    private lateinit var imagePath: String
    private lateinit var imageName: String
    private lateinit var imageMimeType: String
    private lateinit var imageOrientation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSaveActionBtn{
            saveRecipe()
        }

        with(binding) {
            recipeIV.throttleClicks(viewLifecycleOwner) {
                bringPhotoPicker()
            }

            gridLayout1PlusBtn.throttleClicks(viewLifecycleOwner) {
                addIngredientRow()
            }

            gridLayout2PlusBtn.throttleClicks(viewLifecycleOwner) {
                addStepRow()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val pickOnlyImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { pickUri ->
        if (pickUri != null) {
            binding.recipeIV.setImageURI(pickUri)
            val projects = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.Media.ORIENTATION
            )
            CoroutineScope(Dispatchers.IO).launch {
                requireContext().contentResolver.query(pickUri, projects, null, null, null)
                    ?.use { cursor ->
                        cursor.moveToFirst()
                        imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                        imageMimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
                        imageName = UUID.randomUUID().toString().replace("-", "")
                        imageOrientation = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION))
                    } ?: Log.e("TAG", "이미지 파일 Pick Fail~~~")
            }
        } else {
            commonToast("사진을 못 가져 왔네요!")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bringPhotoPicker() {
        pickOnlyImage.launch(PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    /**
     * gridLayout1에 새로운 행 추가
     */
    private fun addIngredientRow() {
        val gridLayout = binding.gridLayout1

        val editText1 = createStyledEditText1(1f)
        val editText2 = createStyledEditText1(1f)
        val editText3 = createStyledEditText1(1f)
        val editText4 = createStyledEditText1(2.5f)

        gridLayout.addView(editText1)
        gridLayout.addView(editText2)
        gridLayout.addView(editText3)
        gridLayout.addView(editText4)
    }

    /**
     * gridLayout2에 새로운 행 추가
     */
    private fun addStepRow() {
        val gridLayout = binding.gridLayout2

        val hint = "순서를 입력해주세요."

        val editText = createStyledEditText2(hint)

        val buttonIndex = gridLayout.indexOfChild(binding.gridLayout2PlusBtn)

        gridLayout.addView(editText, buttonIndex)
    }

    /**
     * 공통적으로 사용되는 EditText 생성 함수
     */
    private fun createStyledEditText1(weight: Float): AppCompatEditText {
        return AppCompatEditText(requireContext()).apply {
            this.setBackgroundResource(R.drawable.border_box)
            this.gravity = Gravity.CENTER
            this.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    40f,
                    resources.displayMetrics
                ).toInt()
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, weight)
            }
        }
    }

    private fun createStyledEditText2(hint: String): AppCompatEditText {
        return AppCompatEditText(requireContext()).apply {
            this.hint = hint
            this.setPadding(16, 0, 0, 0)
            this.gravity = Gravity.START
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            this.layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.MATCH_PARENT
                height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    30f,
                    resources.displayMetrics
                ).toInt()
                setMargins(0, 16, 0, 0)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveRecipe() {
        viewLifecycleOwner.lifecycleScope.launch {
            val recipeName = binding.recipeNameETV.text.toString()
            if (recipeName.isBlank()) {
                commonToast("레시피 이름을 입력해주세요.")
                return@launch
            }

            val steps = (0 until binding.gridLayout2.childCount)
                .mapNotNull { index ->
                    val child = binding.gridLayout2.getChildAt(index)
                    if (child is AppCompatEditText) child.text.toString() else null
                }

            val ingredients = mutableListOf<Map<String, String>>()

            binding.gridLayout1.children.forEachIndexed { index, view ->
                if (index % 4 == 0) {
                    val child1 = binding.gridLayout1.getChildAt(index) as? AppCompatEditText
                    val child2 = binding.gridLayout1.getChildAt(index + 1) as? AppCompatEditText
                    val child3 = binding.gridLayout1.getChildAt(index + 2) as? AppCompatEditText
                    val child4 = binding.gridLayout1.getChildAt(index + 3) as? AppCompatEditText

                    if (child1 != null && child2 != null && child3 != null && child4 != null) {
                        ingredients.add(
                            mapOf(
                                "once" to child1.text.toString(),
                                "twice" to child2.text.toString(),
                                "each" to child3.text.toString(),
                                "name" to child4.text.toString()
                            )
                        )
                    }
                }
            }

            val preferencesDataSource = PreferencesDataSource(requireContext())
            val teamId = preferencesDataSource.getTeamId() ?: run {
                commonToast("팀 ID를 찾을 수 없습니다.")
                return@launch
            }

            viewModel.uploadImage(Uri.fromFile(File(imagePath)), imageName)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.uploadImageResult.collectLatest {
                    firebaseResultHandler(it) { data ->
                        val pictureUrl = data
                        viewModel.saveRecipe(recipeName, pictureUrl, steps, teamId)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.saveRecipeResult.collectLatest {
                    firebaseResultHandler(it) { data ->
                        val recipeId = data
                        viewModel.saveIngredients(recipeId, ingredients)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.saveIngredientsResult.collectLatest {
                    firebaseResultHandler(it) {
                        commonToast("레시피 저장 완료!")
                        navigateRecipeFragmentWithClearStack()
                    }
                }
            }
        }
    }

    private fun showError(error: Throwable?) {
        commonToast("작업 실패: ${error?.message}")
    }

    private fun navigateRecipeFragmentWithClearStack() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.recipeCreateFragment, true)
            .build()

        findNavController().navigate(
            R.id.action_recipeCreateFragment_to_recipeFragment,
            null,
            navOptions
        )
    }
}