package com.ivan.letstalk.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.ivan.letstalk.R
import java.lang.String
import java.util.*


class SetNewReminderActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var ivChooseTime: AppCompatImageView
    private lateinit var ivChooseDate: AppCompatImageView
    private lateinit var ivDropdown: AppCompatImageView
    private lateinit var tvTime: TextView
    private lateinit var tvDate: TextView
    private lateinit var spRow: Spinner
    private lateinit var reminderLayout: RelativeLayout

    private var reminderTime = arrayOf(
        "PC (After Breakfast)", "PC (After Lunch)",
        "Before Meal (AC)", "After Lunch (PC)",
        "QHS (Every night at Bedtime)", "BID (Twice a Day)"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_reminder)
        reminderLayout = findViewById(R.id.reminder_layout)
        ivDropdown = findViewById(R.id.iv_dropdown)
        ivChooseTime = findViewById(R.id.iv_choose_time)
        ivChooseDate = findViewById(R.id.iv_choose_date)
        tvTime = findViewById(R.id.tv_time)
        tvDate = findViewById(R.id.tv_date)

        spRow = findViewById(R.id.spRow)
        spRow.onItemSelectedListener = this

        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            reminderTime
        )
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spRow.adapter = ad

        ivChooseTime.setOnClickListener {
            val mCurrentTime = Calendar.getInstance()
            val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mCurrentTime[Calendar.MINUTE]
            val mTimePicker = TimePickerDialog(
                this@SetNewReminderActivity,
                { _, selectedHour, selectedMinute ->
                    tvTime.text = "$selectedHour:$selectedMinute"
                },
                hour,
                minute,
                true
            ) // Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
            /*val mTimePicker: TimePickerDialog
            val mCurrentTime = Calendar.getInstance()
            val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mCurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    tvTime.text = String.format("%d : %d", hourOfDay, minute)
                }
            }, hour, minute, false)*/

            /*if (reminderLayout != null) {
                reminderLayout.addView(spRow);
            }*/

            ivDropdown.setOnClickListener {
                spRow.performClick()
            }
        }

        ivChooseDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd =
                DatePickerDialog(this, OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    val dateString = String.format("%02d-%02d-%d", dayOfMonth, month, year)
                    // tvDate.text = "$dayOfMonth $month, $year"
                    tvDate.text = dateString
                }, year, month, day)

            dpd.show()
        }
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            onBackPressed()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        /*Toast.makeText(
            applicationContext,
            reminderTime[p2],
            Toast.LENGTH_LONG)
            .show()*/
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}