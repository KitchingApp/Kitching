package com.kitchingapp.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.R
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentOtherBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            userDetailBtn.clicks().onEach {
                navController.navigate(R.id.action_otherFragment_to_userDetailFragment)
            }.launchIn(lifecycleScope)

            inviteCodeBtn.clicks().onEach {
                navController.navigate(R.id.action_otherFragment_to_inviteCodeFragment)
            }.launchIn(lifecycleScope)

            noticeBtn.clicks().onEach {
                navController.navigate(R.id.action_otherFragment_to_noticeFragment)
            }.launchIn(lifecycleScope)

            departmentManagementBtn.clicks().onEach {
                navController.navigate(R.id.action_otherFragment_to_humanResourceFragment)
            }.launchIn(lifecycleScope)

            scheduleTimeBtn.clicks().onEach {
                navController.navigate(R.id.action_otherFragment_to_scheduleTimeFragment)
            }.launchIn(lifecycleScope)

            memberBtn.clicks().onEach {
                navController.navigate(R.id.action_otherFragment_to_memberListFragment)
            }.launchIn(lifecycleScope)
        }
    }
}