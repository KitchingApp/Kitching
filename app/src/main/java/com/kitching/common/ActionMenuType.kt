package com.kitching.common

import android.view.Menu
import androidx.annotation.DrawableRes
import com.kitching.R

enum class ActionMenuType(val groupId: Int, val itemId: Int, val order: Int, val title: String, @DrawableRes val icon: Int) {
    ADD(Menu.NONE, 12345, Menu.NONE, "추가", R.drawable.baseline_add_24);

    companion object {
        fun findMenuByItemId(itemId: Int) = entries.find { it.itemId == itemId }
    }
}