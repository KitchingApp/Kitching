package com.kitchingapp.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kitchingapp.adapter.PrepAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentPrepListBinding
import com.kitchingapp.view.model.PrepViewModel
import com.kitchingapp.view.model.factory.prepViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepListFragment : BaseFragment<FragmentPrepListBinding>(FragmentPrepListBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel by viewModels<PrepViewModel> {
        prepViewModelFactory
    }

    private val args: PrepListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prepAdapter = PrepAdapter(viewLifecycleOwner)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getPrepList(args.prepCategoryId)
                    viewModel.prepList.collectLatest {
                        prepAdapter.submitList(it)
                    }
                }
            }
        }

        with(binding) {
            with(todoCategoryRV) {
                setRvLayout(this)

                this.adapter = prepAdapter
            }
        }
    }
}