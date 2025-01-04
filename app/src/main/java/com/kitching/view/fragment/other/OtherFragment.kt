package com.kitching.view.fragment.other

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitching.LoginActivity
import com.kitching.R
import com.kitching.common.BaseFragment
import com.kitching.common.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.FragmentOtherBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class OtherFragment : BaseFragment<FragmentOtherBinding>(FragmentOtherBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            inviteCodeBtn.clicks().throttleFirst().onEach {
                navController.navigate(R.id.action_otherFragment_to_inviteFragment)
            }.launchIn(lifecycleScope)

            noticeBtn.clicks().throttleFirst().onEach {
                navController.navigate(R.id.action_otherFragment_to_noticeFragment)
            }.launchIn(lifecycleScope)

            departmentManagementBtn.clicks().throttleFirst().onEach {
                navController.navigate(R.id.action_otherFragment_to_departmentFragment)
            }.launchIn(lifecycleScope)

            scheduleTimeBtn.clicks().throttleFirst().onEach {
                navController.navigate(R.id.action_otherFragment_to_scheduleTimeFragment)
            }.launchIn(lifecycleScope)

            memberBtn.clicks().throttleFirst().onEach {
                navController.navigate(R.id.action_otherFragment_to_memberListFragment)
            }.launchIn(lifecycleScope)

            logoutBtn.clicks().throttleFirst().onEach {
                logout()
            }.launchIn(lifecycleScope)
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            PreferencesDataSource(requireContext()).clearUserId()
            PreferencesDataSource(requireContext()).clearTeamId()

            /** intent.flags를 통해 새테스크를 만드는데 메인으로 가야하나 로그인으로 가야하나
             *  여기서 로그인 보내고 로그인에서는 팀고르면 메인으로 보내면 되려나...???
             *  이건 액티비티 스택으로인해 로그인으로 가는게 맞는듯??
             * */
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}