package com.kitchingapp.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitchingapp.adapter.MemberAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.data.database.dto.MemberListDTO
import com.kitchingapp.data.database.repository.LocalRepository
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.LocalType
import com.kitchingapp.data.database.usecase.LocalTypeUseCase
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import com.kitchingapp.databinding.FragmentMemberlistBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MemberListFragment: BaseFragment<FragmentMemberlistBinding>(FragmentMemberlistBinding::inflate) {
    private lateinit var navController: NavController

    private val localRepository: LocalRepository by lazy {
        LocalTypeUseCase(requireContext()).selectLocalType(LocalType.DATASTORE)
    }

    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(RemoteType.FIREBASE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var teamId: String
        lateinit var memberListDTO: MemberListDTO
        val memberAdapter = MemberAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    localRepository.teamId.collectLatest {
                        if (it != null) {
                            teamId = it
                            memberListDTO = remoteRepository.getMemberList(teamId)
                            memberAdapter.submitList(memberListDTO.members)
                            binding.teamNameTV.text = memberListDTO.teamName
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