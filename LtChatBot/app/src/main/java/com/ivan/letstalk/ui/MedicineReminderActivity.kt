package com.ivan.letstalk.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.AlkSideEffectsAdapter
import com.ivan.letstalk.adapter.MedicineReminderAdapter
import com.ivan.letstalk.model.AlkSideEffectsModel
import com.ivan.letstalk.model.MedicineReminderModel
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar

class MedicineReminderActivity : AppCompatActivity() {
    lateinit var collapsibleCalendar : CollapsibleCalendar
    private lateinit var rvMedicineScheduled: RecyclerView
    private val medicineReminderModel = ArrayList<MedicineReminderModel>()
    private lateinit var medicineReminderAdapter: MedicineReminderAdapter
    private lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_reminder)
        fab = findViewById(R.id.add_fab)
        rvMedicineScheduled = findViewById(R.id.rv_medicine_scheduled)
        medicineReminderAdapter = MedicineReminderAdapter(medicineReminderModel)
        rvMedicineScheduled.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )
        rvMedicineScheduled.itemAnimator = DefaultItemAnimator()
        rvMedicineScheduled.adapter = medicineReminderAdapter
        initMedicineReminder()
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            onBackPressed()
        }
        fab.setOnClickListener{
            val intent = Intent(this, SetNewReminderActivity::class.java)
            startActivity(intent)
        }
        collapsibleCalendar = findViewById(R.id.calender)
        collapsibleCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDaySelect() {
                val day: Day = collapsibleCalendar.selectedDay!!
                Log.i(
                    javaClass.name, "Selected Day: "
                            + day.year + "/" + (day.month + 1) + "/" + day.day
                )
            }

            override fun onItemClick(v: View) {

            }

            override fun onClickListener() {

            }

            override fun onDataUpdate() {

            }

            override fun onDayChanged() {

            }

            override fun onMonthChange() {

            }

            override fun onWeekChange(position: Int) {

            }
        })
    }

    private fun initMedicineReminder() {
        var movie = MedicineReminderModel("Acetaminophen",1,"PC (After Breakfast)")
        medicineReminderModel.add(movie)
        movie = MedicineReminderModel("Paracetamol",1,"PC (After Lunch)")
        medicineReminderModel.add(movie)
        movie = MedicineReminderModel("Ambroxol",1,"PC (Every night at Bedtime)")
        medicineReminderModel.add(movie)
        medicineReminderAdapter.notifyDataSetChanged()
    }
}