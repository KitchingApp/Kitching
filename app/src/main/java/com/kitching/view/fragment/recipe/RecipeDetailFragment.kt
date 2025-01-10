package com.kitching.view.fragment.recipe

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
import com.bumptech.glide.Glide
import com.kitching.R
import com.kitching.common.BaseFragment
import com.kitching.data.dto.IngredientDTO
import com.kitching.databinding.FragmentRecipeDetailBinding

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
            Glide.with(binding.root.context)
                .load(args.recipeImage)
                .placeholder(R.drawable.pancake) // 로딩 중 표시할 기본 이미지
                .error(R.drawable.chestnutcream) // 로드 실패 시 표시할 이미지
                .into(recipeIV)

            recipeNameTV.text = args.recipeName
        }
        gridLayoutSetting()
        createStepsTextView()
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
        val gridLayout = binding.gridLayout1
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
    private fun createStepsTextView() {
        val stepsString = args.recipeStep

        val stepsList = stepsString?.split("|")?.mapIndexed { index, step ->
            "${index + 1}. $step"
        } ?: listOf("순서 없음")

        val gridLayout = binding.gridLayout2

        stepsList.forEach { step ->
            val stepTextView = AppCompatTextView(binding.root.context).apply {
                this.text = step
                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                this.gravity = Gravity.START
                this.layoutParams = GridLayout.LayoutParams().apply {
                    width = GridLayout.LayoutParams.MATCH_PARENT
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(0, 16, 0, 0)
                    setPadding(0, 0, 0, 16)
                }
                this.minHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    30f,
                    resources.displayMetrics
                ).toInt()
            }
            gridLayout.addView(stepTextView)
        }
    }
}