package com.unicomg.uaa.core

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.moviestask.R

fun initProgressDialog(activity: Activity): Dialog {
    val progressDialog = Dialog(activity)
    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    progressDialog.setCancelable(false)
    progressDialog.setCanceledOnTouchOutside(false)
    progressDialog.setContentView(R.layout.loading_dialog)
    progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return progressDialog
}