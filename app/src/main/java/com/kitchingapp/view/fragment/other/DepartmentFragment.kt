package com.kitchingapp.view.fragment.other

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.DepartmentAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentDepartmentBinding
import com.kitchingapp.databinding.FragmentInviteCodeBinding
import com.kitchingapp.domain.entities.Department
import com.kitchingapp.domain.entities.OrderCategory

class DepartmentFragment : BaseFragment<FragmentDepartmentBinding>(FragmentDepartmentBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val departmentMockData = listOf(
//            Department("홀", Color.parseColor("#90CAF9")),
//            Department("주방", Color.parseColor("#EF9A9A"))
//        )

        with(binding.departmentRV) {
            setRvLayout(this)

            val departmentAdapter = DepartmentAdapter(viewLifecycleOwner, navController)
//            departmentAdapter.submitList(departmentMockData)
            this.adapter = departmentAdapter
        }
    }
}