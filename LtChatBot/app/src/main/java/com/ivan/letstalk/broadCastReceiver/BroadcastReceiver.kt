package com.ivan.letstalk.broadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ivan.letstalk.R


var downloadListener: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(ct: Context?, intent: Intent?) {
        val builder = MaterialAlertDialogBuilder(ct!!, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
        builder.setTitle("Delete Document")
        builder.setMessage("Are you sure you want to Delete?")
        builder.setPositiveButton("Yes"){ dialog,which->

        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        builder.setOnCancelListener {

        }
        builder.setOnDismissListener {

        }
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.alert_dialog_back)
        dialog.show()
        val window: Window = dialog.window!!
        window.setLayout(800, 500)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(ct!!, R.color.blue))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(ct!!, R.color.blue))
    }
}