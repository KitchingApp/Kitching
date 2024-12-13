package com.kitchingapp.view.fragment.schedule

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentScheduleBinding
import com.kitchingapp.R

class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ScheduleChildFragment()).commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}