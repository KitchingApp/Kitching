package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitching.adapter.PrepCategoryAdapter
import com.kitching.common.BaseFragment
import com.kitching.common.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.FragmentPrepBinding
import com.kitching.view.model.PrepViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class PrepCategoryFragment : BaseFragment<FragmentPrepBinding>(FragmentPrepBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel by viewModels<PrepViewModel> {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prepCategoryAdapter = PrepCategoryAdapter(viewLifecycleOwner, navController)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    PreferencesDataSource(requireContext()).teamId.collectLatest { teamId ->
                        if(teamId != null) {
                            viewModel.getPrepCategory(teamId)
                        }
                    }
                    viewModel.prepCategory.collectLatest {
                        when(it) {
                            is FirebaseResult.Success -> prepCategoryAdapter.submitList(it.data)
                            is FirebaseResult.Loading -> TODO("로딩 처리")
                            is FirebaseResult.Failure -> TODO("예외 처리")
                            is FirebaseResult.DummyConstructor -> TODO("더미 생성")
                        }
                    }
                }
            }
        }

        with(binding) {
            with(todoCategoryRV) {
                setRvLayout(this)

                this.adapter = prepCategoryAdapter
            }

            with(createTodoCategoryBtn) {
                clicks().throttleFirst().onEach {
//                    navController.navigate(R.id.createPrepCategoryDialog)
                }.launchIn(lifecycleScope)
            }

//            parentFragmentManager.setFragmentResultListener(TODO_CATEGORY_ARGS_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
//                val categoryName = bundle.getString(TODO_CATEGORY_NAME_KEY) ?: return@setFragmentResultListener
//                val categoryColor = bundle.getString(TODO_CATEGORY_COLOR_KEY)?.let { Color.parseColor(it) } ?: return@setFragmentResultListener
//
////                todoCategoriesMockData.add(TodoCategory(categoryName, categoryColor))
////                (todoCategoryRV.adapter as? TodoCategoryAdapter)?.submitList(todoCategoriesMockData.toList())
//            }
        }
    }
}