package com.kitching.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitching.adapter.NoticeAdapter
import com.kitching.common.BaseFragment
import com.kitching.data.repository.LocalRepository
import com.kitching.data.usecase.LocalType
import com.kitching.data.usecase.LocalTypeUseCase
import com.kitching.databinding.FragmentNoticeBinding
import com.kitching.view.model.NoticeViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoticeFragment : BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel by viewModels<NoticeViewModel> {
        viewModelFactory
    }

    private val localRepository: LocalRepository by lazy {
        LocalTypeUseCase(requireContext()).selectLocalType(LocalType.DATASTORE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var teamId: String
        val noticeAdapter = NoticeAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    localRepository.teamId.collect {
                        if (it != null) {
                            teamId = it
                            viewModel.getNotices(teamId)
                        }
                    }
                }

                launch {
                    viewModel.notices.collectLatest {
                        noticeAdapter.submitList(it)
                    }
                }
            }
        }

        with(binding.noticeRV) {
            setRvLayout(this)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noticeAdapter
        }
    }
}