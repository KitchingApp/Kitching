package com.kitching.view.fragment.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kitching.common.BaseFragment
import com.kitching.common.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.FragmentCreateTeamBinding
import com.kitching.view.model.TeamViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue
import com.kitching.R

class CreateTeamFragment: BaseFragment<FragmentCreateTeamBinding>(FragmentCreateTeamBinding::inflate) {
    private val viewModel by viewModels<TeamViewModel> {
        viewModelFactory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createTeamBtn.throttleClicks(viewLifecycleOwner) {
            val teamName = binding.teamNameEditText.text.toString()

            if (teamName.isBlank()) {
                Toast.makeText(requireContext(), "팀 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@throttleClicks
            }

            observeViewModel()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                val teamName = binding.teamNameEditText.text.toString()

                val ownerId = PreferencesDataSource(requireContext()).getUserId()

                viewModel.createTeam(ownerId.toString(), teamName)

                viewModel.createTeamResult.collectLatest { result ->
                    when (result) {
                        is FirebaseResult.Loading -> {}
                        is FirebaseResult.Success -> {
                            Toast.makeText(requireContext(), "팀 생성 완료!", Toast.LENGTH_SHORT).show()
                            navigateToNextScreen()
                        }
                        is FirebaseResult.Failure -> showError(result.throwable)
                        is FirebaseResult.DummyConstructor -> {}
                    }
                }
            }
        }
    }

    private fun navigateToNextScreen() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LoginTeamsFragment())
            .commit()
    }

    private fun showError(error: Throwable?) {
        Toast.makeText(requireContext(), "팀 생성 실패: ${error?.message}", Toast.LENGTH_SHORT).show()
    }
}