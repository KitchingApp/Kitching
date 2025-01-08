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
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.FragmentNoticeBinding
import com.kitching.view.model.NoticeViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoticeFragment : BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate) {

    private val viewModel by viewModels<NoticeViewModel> {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var teamId: String
        val noticeAdapter = NoticeAdapter(viewLifecycleOwner)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
                viewModel.getNotices(teamId)
                viewModel.notices.collectLatest {
                    firebaseResultHandler(it) { data ->
                        noticeAdapter.submitList(data)
                    }
                }
            }
        }
        with(binding.noticeRV)
        {
            setRvLayout(this)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noticeAdapter
        }

        setPlusActionBtn {
            val action = NoticeFragmentDirections.actionNoticeFragmentToNoticeCreateFragment()
            findNavController().navigate(action)
        }
    }
}