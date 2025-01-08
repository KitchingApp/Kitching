package com.kitching.common.util

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.kitching.R

object ProgressDialog {
    private var progressDialog: Dialog? = null

    fun show(context: Context) {
        if (progressDialog?.isShowing == true) return

        progressDialog = Dialog(context).apply {
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }

            setContentView(
                ProgressBar(context).apply {
                    progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.md_theme_primary)
                    )
                }
            )
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
        }
    }

    fun cancel() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}