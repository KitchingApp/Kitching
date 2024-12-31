package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kitching.adapter.PrepAdapter
import com.kitching.common.BaseFragment
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.FragmentPrepListBinding
import com.kitching.view.model.PrepViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepListFragment : BaseFragment<FragmentPrepListBinding>(FragmentPrepListBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel by viewModels<PrepViewModel> {
        viewModelFactory
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
                viewModel.getPrepList(args.prepCategoryId)
                viewModel.prepList.collectLatest {
                    when (it) {
                        is FirebaseResult.Success -> prepAdapter.submitList(it.data)
                        is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                        is FirebaseResult.Failure -> {} // TODO("예외 처리")
                        is FirebaseResult.DummyConstructor -> {} // TODO("더미 생성")
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