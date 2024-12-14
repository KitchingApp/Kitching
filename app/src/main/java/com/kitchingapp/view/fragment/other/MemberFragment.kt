package com.kitchingapp.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.MemberAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentMemberlistBinding

class MemberFragment: BaseFragment<FragmentMemberlistBinding>(FragmentMemberlistBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allUsers = RestaurantGenerator.restaurantList().flatMap { it.members }

        with(binding.memberListRV) {
            setRvLayout(this)

            val memberAdapter = MemberAdapter()
            memberAdapter.submitList(allUsers)
            this.adapter = memberAdapter
        }
    }
}