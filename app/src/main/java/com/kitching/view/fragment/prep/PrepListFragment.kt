package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepListFragment : BaseFragment<FragmentPrepListBinding>(FragmentPrepListBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel = PrepViewModel.instance

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
                    firebaseResultHandler(it) { data ->
                        prepAdapter.submitList(data)
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

        setPlusActionBtn {
            val action = PrepListFragmentDirections.actionPrepListFragmentToPrepCreateDialog(args.prepCategoryId)
            navController.navigate(action)
        }
    }
}