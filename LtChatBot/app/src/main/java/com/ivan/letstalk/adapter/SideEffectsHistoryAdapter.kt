package com.ivan.letstalk.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.sideEffectsHistoryResponse.SideEffectsHistoryDateDuration
import java.text.SimpleDateFormat

class SideEffectsHistoryAdapter(var sideEffectsHistoryList: List<SideEffectsHistoryDateDuration>) :
    RecyclerView.Adapter<SideEffectsHistoryAdapter.SideEffectsHistoryVH>() {
    private lateinit var context: Context
    private val limit = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SideEffectsHistoryVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.side_effects_list_row, parent, false)
        return SideEffectsHistoryVH(view)
    }

    override fun onBindViewHolder(holder: SideEffectsHistoryVH, position: Int) {
        val sideEffectsHistory = sideEffectsHistoryList[position]
        val str = sideEffectsHistory.startDate
        var str1 = ""
        if (sideEffectsHistory.endDate.isNotEmpty()){
            str1 = sideEffectsHistory.endDate
        }
        // val str1 = sideEffectsHistory.endDate
        var date = ""
        if (str.isNotEmpty()) {
            date = str.split("\\s".toRegex())[0]
            Log.d("date", date)
        }
        // Start Date
        var spf = SimpleDateFormat("yyyy-MM-dd")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("dd MMM yy")
        val newDateString = newDate?.let { spf.format(it) }
        Log.d("newDateString",newDateString.toString())

        var mEndDate = ""
        if (str1.isNotEmpty()) {
            mEndDate = str1.split("\\s".toRegex())[0]
            // Log.d("mEndDate",mEndDate)
        }
        var mDayDifference = ""
        var newEndDateString = ""
        // End Date
        var mSpf = SimpleDateFormat("yyyy-MM-dd")
        if (mEndDate.isNotEmpty()) {
            val newEndDate = mSpf.parse(mEndDate)
            mSpf = SimpleDateFormat("dd MMM yy")
            newEndDateString = newEndDate?.let { mSpf.format(it) }.toString()
            Log.d("newDateString", newEndDateString.toString())
            // Days Diff.
            val mDate1 = date
            val mDate2 = mEndDate
            val mDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val mDate11 = mDateFormat.parse(mDate1)
            val mDate22 = mDateFormat.parse(mDate2)
            val mDifference = kotlin.math.abs(mDate11.time - mDate22.time)
            val mDifferenceDates = mDifference / (24 * 60 * 60 * 1000)
            mDayDifference = mDifferenceDates.toString()
            Log.d("mDayDifference",mDayDifference)
        }

        /*// Days Diff.
        val mDate1 = date
        val mDate2 = mEndDate
        val mDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val mDate11 = mDateFormat.parse(mDate1)
        val mDate22 = mDateFormat.parse(mDate2)
        val mDifference = kotlin.math.abs(mDate11.time - mDate22.time)
        val mDifferenceDates = mDifference / (24 * 60 * 60 * 1000)
        val mDayDifference = mDifferenceDates.toString()
        Log.d("mDayDifference",mDayDifference)*/

        if (sideEffectsHistory.endDate.isEmpty()) {
            holder.tvSideEffectsStartDate.text = newDateString.plus("  ").plus("to").plus(" ")
            holder.tvSideEffectsEndDate.text = "Till Date"
        } else {
            holder.tvSideEffectsStartDate.text = newDateString.plus(" ").plus("to").plus(" ").plus(newEndDateString)
        }
        if (sideEffectsHistory.endDate.isEmpty()) {
            holder.tvSideEffectsDuration.text = "Continuing"
            holder.tvSideEffectsDuration.setTextColor(ContextCompat.getColor(context, R.color.yellow))
        } else {
            holder.tvSideEffectsDuration.setTextColor(ContextCompat.getColor(context, R.color.red))
            holder.tvSideEffectsDuration.text = "$mDayDifference days"
        }
        // holder.tvSideEffectsDuration.text = sideEffectsHistory.endDate
    }

    override fun getItemCount(): Int {
        /*return if (sideEffectsHistoryList.size > limit) {
            limit
        } else {
            sideEffectsHistoryList.size
        }*/
        return sideEffectsHistoryList.size
    }

    inner class SideEffectsHistoryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSideEffectsStartDate: TextView
        var tvSideEffectsEndDate: TextView
        var tvSideEffectsDuration: TextView

        init {
            tvSideEffectsStartDate = itemView.findViewById(R.id.tv_side_effects_start_date)
            tvSideEffectsEndDate = itemView.findViewById(R.id.tv_side_effects_end_date)
            tvSideEffectsDuration = itemView.findViewById(R.id.tv_side_effects_duration)
        }
    }
}