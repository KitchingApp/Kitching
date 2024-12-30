package com.kitching.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitching.adapter.MemberAdapter
import com.kitching.common.BaseFragment
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.MemberListDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OtherRepository
import com.kitching.databinding.FragmentMemberlistBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MemberListFragment: BaseFragment<FragmentMemberlistBinding>(FragmentMemberlistBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    private val memberAdapter = MemberAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    PreferencesDataSource(requireContext()).teamId.collectLatest { teamId ->
                        if (teamId != null) {
                            OtherRepository().getMemberList(teamId).collectLatest {
                                when(it) {
                                    is FirebaseResult.Success -> {
                                        memberAdapter.submitList(it.data.members)
                                        binding.teamNameTV.text = it.data.teamName
                                    }
                                    is FirebaseResult.Loading -> TODO("로딩 처리")
                                    is FirebaseResult.Failure -> TODO("예외 처리")
                                    is FirebaseResult.DummyConstructor -> TODO("더미 생성")
                                }
                            }
                        }
                    }
                }
            }
        }

        with(binding) {

            with(memberListRV) {
                setRvLayout(this)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = memberAdapter
            }
        }
    }
}