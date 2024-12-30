package com.kitching.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitching.R
import com.kitching.common.BaseFragment
import com.kitching.common.throttleFirst
import com.kitching.databinding.FragmentOtherBinding
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
        }
    }
}