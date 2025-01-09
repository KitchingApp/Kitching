package com.kitching.common

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kitching.R
import com.kitching.data.firebase.FirebaseResult

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
            layoutManager = LinearLayoutManager(KitchingApplication.getAppContext())
//            addItemDecoration(
//                DividerItemDecoration(
//                    KitchingApplication.getAppContext(),
//                    LinearLayoutManager.VERTICAL
//                )
//            )
        }
    }

    /** 액션바 +버튼 */
    fun setPlusActionBtn(onClickAddBtn: () -> Unit) {
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
                        val typedValue = TypedValue()

                        val theme = requireContext().theme
                        theme.resolveAttribute(android.R.attr.colorControlNormal, typedValue, true)
                        val color = ContextCompat.getColor(requireContext(), typedValue.resourceId)

                        icon?.setTint(color)
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

    /** 액션바 v버튼 */
    fun setSaveActionBtn(onClickAddBtn: () -> Unit) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.add(
                    ActionMenuType.SAVE.groupId,
                    ActionMenuType.SAVE.itemId,
                    ActionMenuType.SAVE.order,
                    ActionMenuType.SAVE.title
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    setIcon(ActionMenuType.SAVE.icon)
                    val typedValue = TypedValue()

                    val theme = requireContext().theme
                    theme.resolveAttribute(android.R.attr.colorControlNormal, typedValue, true)
                    val color = ContextCompat.getColor(requireContext(), typedValue.resourceId)

                    icon?.setTint(color)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val actionMenu = ActionMenuType.findMenuByItemId(menuItem.itemId)
                return if (ActionMenuType.findMenuByItemId(menuItem.itemId) != null) {
                    when(actionMenu) {
                        ActionMenuType.SAVE -> {
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

    /** 액션바 옵션버튼 */
    fun setOptionsActionBtn(onClickUpdateBtn: () -> Unit, onClickDeleteBtn: () -> Unit) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.option_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.updateInOptionMenu -> {
                        onClickUpdateBtn()
                        true
                    }
                    R.id.deleteInOptionMenu -> {
                        onClickDeleteBtn()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }
}