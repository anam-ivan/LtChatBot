package com.ivan.letstalk.helper

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AdapterView
import android.widget.Spinner


internal abstract class SpinnerOpenCloseListener(spinner: Spinner) : OnTouchListener,
    AdapterView.OnItemSelectedListener {
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            onOpen()
        }
        return false
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, arg2: Int, arg3: Long) {
        onClose()
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        onClose()
    }

    abstract fun onOpen()
    abstract fun onClose()

    init {
        spinner.setOnTouchListener(this)
        spinner.onItemSelectedListener = this
    }
}