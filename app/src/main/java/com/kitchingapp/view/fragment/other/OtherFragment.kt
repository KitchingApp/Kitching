package com.kitchingapp.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentOtherBinding

class OtherFragment : BaseFragment<FragmentOtherBinding>(FragmentOtherBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}