package com.kitchingapp.view.fragment.other

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.StaffLevelAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentStafflevelBinding
import com.kitchingapp.domain.entities.Department
import com.kitchingapp.domain.entities.StaffLevel

class StaffLevelFragment : BaseFragment<FragmentStafflevelBinding>(FragmentStafflevelBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val staffLevelMockData = listOf(
//            StaffLevel("쫄병", Department("홀", Color.parseColor("#90CAF9"))),
//            StaffLevel("대장", Department("주방", Color.parseColor("#EF9A9A")))
//        )

        with(binding.departmentRV) {
            setRvLayout(this)

            val staffLevelAdapter = StaffLevelAdapter(viewLifecycleOwner)
//            staffLevelAdapter.submitList(staffLevelMockData)
            this.adapter = staffLevelAdapter
        }
    }
}