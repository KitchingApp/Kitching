package com.kitching.common

import android.widget.Toast

fun commonToast(msg: String) {
    Toast.makeText(KitchingApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show()
}