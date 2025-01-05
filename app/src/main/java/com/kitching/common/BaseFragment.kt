package com.kitching.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kitching.R

typealias FragmentInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: FragmentInflate<VB>
) : Fragment() {

    var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setRvLayout(recyclerView: RecyclerView){
        with(recyclerView){
            layoutManager = LinearLayoutManager(KitchingApplication.Companion.getAppContext())
//            addItemDecoration(
//                DividerItemDecoration(
//                    KitchingApplication.getAppContext(),
//                    LinearLayoutManager.VERTICAL
//                )
//            )
        }
    }

    fun setActionBtn(onClickAddBtn: () -> Unit) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.add(
                        ActionMenuType.ADD.groupId,
                        ActionMenuType.ADD.itemId,
                        ActionMenuType.ADD.order,
                        ActionMenuType.ADD.title
                    ).apply {
                        setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                        setIcon(ActionMenuType.ADD.icon)
                    }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val actionMenu = ActionMenuType.findMenuByItemId(menuItem.itemId)
                return if (ActionMenuType.findMenuByItemId(menuItem.itemId) != null) {
                    when(actionMenu) {
                        ActionMenuType.ADD -> {
                            onClickAddBtn()
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
        }, viewLifecycleOwner)
    }
}