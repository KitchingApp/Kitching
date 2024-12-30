package com.kitching.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitching.common.BaseFragment
import com.kitching.databinding.FragmentInviteCodeBinding

class InviteCodeFragment : BaseFragment<FragmentInviteCodeBinding>(FragmentInviteCodeBinding::inflate) {
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