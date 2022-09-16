package com.ivan.letstalk.helper

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.TextView

class CustomAlert {
    companion object {
        /*fun showDialog(activity: Activity?, msg: String?, mDialogSubHeaderText:String?) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogHeaderText = dialog.findViewById<View>(R.id.tvDialogHeader) as TextView
            val dialogSubHeaderText = dialog.findViewById<View>(R.id.tvDialogSubHeader) as TextView
            dialogHeaderText.text = msg
            dialogSubHeaderText.text = mDialogSubHeaderText
            val dialogButton: TextView = dialog.findViewById<View>(R.id.tvOk) as TextView
            dialogButton.setOnClickListener {
                dialog.dismiss()
                val requestMoney = Intent(activity, DashboardActivity.)
                activity.startActivity(requestMoney)
                activity.overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }
            dialog.show()
        }*/
    }
}