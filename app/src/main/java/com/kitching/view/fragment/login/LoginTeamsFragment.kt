package com.kitching.view.fragment.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kitching.R
import com.kitching.adapter.TeamListAdapter
import com.kitching.common.BaseFragment
import com.kitching.common.firebaseResultHandler
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.TeamDTO
import com.kitching.databinding.FragmentLoginTeamsBinding
import com.kitching.view.model.LoginViewModel
import com.kitching.view.model.factory.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class LoginTeamsFragment: BaseFragment<FragmentLoginTeamsBinding>(FragmentLoginTeamsBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createTeamBtn.setOnClickListener {
            navigateToLoginTeamsFragment()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val userId = PreferencesDataSource(requireContext()).getUserId().toString()

            viewModel.getTeamList(userId)

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.teamList.collectLatest {
                    firebaseResultHandler(it) { data ->
                        notifyTeamList(data)
                    }
                }
            }
        }
    }

    private fun notifyTeamList(teamList: List<TeamDTO>?) {
        with(binding.recyclerView) {
            setRvLayout(this)

            val teamAdapter = TeamListAdapter(lifecycleScope, context)
            teamAdapter.submitList(teamList)
            this.adapter = teamAdapter
        }
    }

    private fun navigateToLoginTeamsFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CreateTeamFragment())
            .commit()
    }
}