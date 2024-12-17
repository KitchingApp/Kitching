package com.kitchingapp.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.R
import com.kitchingapp.adapter.NoticeAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentInviteCodeBinding
import com.kitchingapp.databinding.FragmentNoticeBinding
import com.kitchingapp.databinding.FragmentOtherBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class NoticeFragment : BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allNotice = RestaurantGenerator.restaurantList().flatMap { it.notices }

        with(binding.noticeRV) {
            setRvLayout(this)

            val noticeAdapter = NoticeAdapter()
            noticeAdapter.submitList(allNotice)
            this.adapter = noticeAdapter
        }
    }
}