package com.kitchingapp.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.PrepCategoryAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.data.database.repository.LocalRepository
import com.kitchingapp.data.database.usecase.LocalType
import com.kitchingapp.data.database.usecase.LocalTypeUseCase
import com.kitchingapp.databinding.FragmentPrepBinding
import com.kitchingapp.view.model.PrepViewModel
import com.kitchingapp.view.model.factory.viewModelFactory
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

    private val localRepository: LocalRepository by lazy {
        LocalTypeUseCase(requireContext()).selectLocalType(LocalType.DATASTORE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    private lateinit var teamId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prepCategoryAdapter = PrepCategoryAdapter(viewLifecycleOwner, navController)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    localRepository.teamId.collectLatest {
                        if(it != null) {
                            teamId = it
                            viewModel.getPrepCategory(teamId)
                        }
                    }
                    viewModel.prepCategory.collectLatest {
                        prepCategoryAdapter.submitList(it)
                    }
                }

                launch {
                    viewModel.prepCategory.collectLatest {
                        prepCategoryAdapter.submitList(it)
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
                clicks().onEach {
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