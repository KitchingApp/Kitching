package com.kitchingapp.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.PrepAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentPrepListBinding

class PrepListFragment : BaseFragment<FragmentPrepListBinding>(FragmentPrepListBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val todosMockData = listOf(
//            Todo("샐러드", TodoCategory("콜드", Color.parseColor("#90CAF9"))),
//            Todo("파스타", TodoCategory("핫", Color.parseColor("#EF9A9A")))
//        )

        with(binding) {
            with(todoCategoryRV) {
                setRvLayout(this)

                val prepAdapter = PrepAdapter(viewLifecycleOwner)
//                todoAdapter.submitList(todosMockData)
                this.adapter = prepAdapter
            }
        }
    }
}