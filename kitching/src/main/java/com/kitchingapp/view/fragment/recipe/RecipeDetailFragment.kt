package com.kitchingapp.view.fragment.recipe

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kitchingapp.R
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.data.database.dto.IngredientDTO
import com.kitchingapp.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment: BaseFragment<FragmentRecipeDetailBinding>(FragmentRecipeDetailBinding::inflate) {
    private lateinit var navController: NavController
    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val picture = R.drawable.pancake
            recipeIV.setImageResource(picture)
            recipeNameTV.text = args.recipeName
        }
        gridLayoutSetting()
        printSteps()
    }

    private fun gridLayoutSetting() {
        val ingredientsString = args.recipeIngredients

        /** 문자열을 IngredientDTO 리스트로 변환*/
        val ingredientList = ingredientsString.split("|").map { ingredientData ->
            val regex = """IngredientDTO\(ingredientId=(.*?), ingredientName=(.*?), once=(.*?), twice=(.*?), each=(.*?)\)""".toRegex()
            val matchResult = regex.matchEntire(ingredientData.trim())
            matchResult?.let {
                val (ingredientId, ingredientName, once, twice, each) = it.destructured
                IngredientDTO(
                    ingredientId = ingredientId,
                    ingredientName = ingredientName,
                    once = once.toInt(),
                    twice = twice.toInt(),
                    each = each
                )
            } ?: IngredientDTO(
                ingredientId = "",
                ingredientName = "",
                once = 0,
                twice = 0,
                each = ""
            )
        }

        /** GridLayout에 TextView 추가*/
        val gridLayout = binding.gridLayout
        ingredientList.forEach { ingredient ->
            val onceTextView = createTableStyledTextView(ingredient.once.toString())
            val twiceTextView = createTableStyledTextView(ingredient.twice.toString())
            val eachTextView = createTableStyledTextView(ingredient.each)

            val nameTextView = createNameStyledTextView(ingredient.ingredientName)

            gridLayout.addView(onceTextView)
            gridLayout.addView(twiceTextView)
            gridLayout.addView(eachTextView)
            gridLayout.addView(nameTextView)
        }
    }

    /** once, twice, each TextView 함수 */
    private fun createTableStyledTextView(text: String): TextView {
        return AppCompatTextView(binding.root.context).apply {
            this.text = text
            this.setBackgroundResource(R.drawable.border_box)
            this.gravity = Gravity.CENTER
            this.setTextAppearance(R.style.recipeIngredientsTable)
            this.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    40f,
                    resources.displayMetrics
                ).toInt()
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
        }
    }

    /** ingredient TextView 함수 */
    private fun createNameStyledTextView(text: String): TextView {
        return AppCompatTextView(binding.root.context).apply {
            this.text = text
            this.setBackgroundResource(R.drawable.border_box)
            this.gravity = Gravity.CENTER
            this.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    40f,
                    resources.displayMetrics
                ).toInt()
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2.5f)
            }
        }
    }

    /** steps 출력 함수*/
    private fun printSteps() {
        val stepString = args.recipeStep
        val formattedSteps = stepString?.split("|")?.mapIndexed { index, step ->
            "${index + 1}. $step"
        }?.joinToString("\n\n") ?: "순서 없음"

        binding.recipeStepTV2.text = formattedSteps
    }
}