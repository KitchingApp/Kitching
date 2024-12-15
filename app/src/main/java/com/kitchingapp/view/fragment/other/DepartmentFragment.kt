package com.kitchingapp.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentDepartmentBinding
import com.kitchingapp.databinding.FragmentInviteCodeBinding

class DepartmentFragment : BaseFragment<FragmentDepartmentBinding>(FragmentDepartmentBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
        }
    }
}