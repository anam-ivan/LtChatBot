package com.ivan.letstalk.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.model.healthVitals.MetaDeleteBodies
import com.ivan.letstalk.model.myHealthVitals.Data
import com.ivan.letstalk.ui.AddHealthVitals
import com.ivan.letstalk.ui.MyHealthVitals
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HealthVitalsAdapter(private var vitalsDetails: ArrayList<Data>) :
    RecyclerView.Adapter<HealthVitalsAdapter.HealthVitalsVH>() {
    private lateinit var context: Context
    private val id: MutableList<String> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthVitalsVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_health_vitals_row, parent, false)
        return HealthVitalsVH(view)
    }

    override fun onBindViewHolder(holder: HealthVitalsVH, position: Int) {
        // val healthVitals = vitalsDetails[position].value[position].vital[position]
        // val formattedTime = healthVitals.time
        /*val formattedTime = vitalsDetails[position].value[position].time?.get(position)
            val str = formattedTime.toString().split(":")
            var hour = ""
            var minute = ""
            if (str != null) {
                Log.d("star", str[0])
                hour = str[0]
                minute = str[1]
            }
            var mHour = 0
            if (hour > 12.toString()){
                mHour = hour.toInt() - 12
                Log.d("ds",mHour.toString())
            } else {
                mHour = hour.toInt()
            }
            *//*if (hour == "24") {
            hour = "00"
            mHour = hour.toInt()
            }*//*
            if (mHour.toString() < 12.toString() && mHour.toString() >= 0.toString()) {
                holder.tvTime.text = ("$mHour:$minute AM")
            } else {
                holder.tvTime.text = ("$mHour:$minute PM")
            }*/
        /*val time = "$hour:$minute"
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj: Date = sdf.parse(time)
            println(dateObj)
            println(SimpleDateFormat("K:mm").format(dateObj))
        } catch (e: ParseException) {
            e.printStackTrace()
        }*/
        val date = vitalsDetails[position].date
        var spf = SimpleDateFormat("yyyy-MM-dd")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("dd MMM yy")
        val newDateString = newDate?.let { spf.format(it) }
        println(newDateString)
        Log.d("newDateString",newDateString.toString())
        holder.tvDate.text = newDateString
        holder.btnInfo.setOnClickListener {
            SimpleTooltip.Builder(context)
                .anchorView(holder.btnInfo)
                .text("Text to do Tooltip")
                .gravity(Gravity.BOTTOM)
                .textColor(Color.WHITE)
                .backgroundColor(context.getColor(R.color.hex_blue))
                .arrowColor(context.getColor(R.color.hex_blue))
                .animated(true)
                .transparentOverlay(false)
                .build()
                .show()
        }
        // val healthVitalsElementsAdapter = HealthVitalsElementsAdapter(vitalsDetails[position].value[position].vital)
        val healthVitalsElementsAdapter = HealthVitalsElementsAdapter(vitalsDetails[position].value)
        holder.childRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        holder.childRecyclerView.adapter = healthVitalsElementsAdapter
        /*holder.btnEdit.setOnClickListener {
            // Log.d("TAG", "vitalsList: ${Gson().toJson(healthVitals.vital)}")
            val intent = Intent(context, AddHealthVitals::class.java)
            intent.putExtra("isFromHealthVitalCard",true)
            // intent.putParcelableArrayListExtra("vitalList", healthVitals.vital)
            // intent.putParcelableArrayListExtra("vitalList", healthVitals.vital)
            // intent.putParcelableArrayListExtra("data", vitalsDetails)
            intent.putExtra("position",position)
            intent.putExtra("data", vitalsDetails as Serializable)
            // intent.putParcelableArrayListExtra("dataValue", vitalsDetails[position].value)
            context.startActivity(intent)
        }*/
        if (position == 0) {
            holder.rlFilter.visibility = View.VISIBLE
            holder.ivCalender.setOnClickListener {
                Log.d("filter", "Filter Clicked")
            }
        }
        /*holder.btnDelete.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context,R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            builder.setTitle("Delete Vital")
            builder.setMessage("Are you sure you want to Delete?")
            builder.setPositiveButton("Yes"){ dialog,which->
                val pos: Int = holder.absoluteAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {

                }
                vitalsDetails[position].value[position].vital[position].Id?.let { it1 -> id.add(it1) }
                val body = MetaDeleteBodies.MetaDeleteBody(
                    // _id = healthCard._id,
                    _id = id,
                    status = "INACTIVE",
                )
                (context as MyHealthVitals).deleteHealthVitalsObservers(
                    body
                )
                deleteItem(pos)
            }
            builder.setNegativeButton("No"){dialog,which->
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
            window.setLayout(1200, 300)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
        }*/
    }

    private fun mDeleteItem(position: Int) {
        vitalsDetails.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, vitalsDetails.size)
        // holder.itemView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return vitalsDetails.size
    }

    inner class HealthVitalsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDate: TextView
        var childRecyclerView: RecyclerView
        // var btnEdit: AppCompatTextView
        var rlFilter: RelativeLayout
        var ivCalender: ImageView
        var btnInfo: ImageView
        // var btnDelete: AppCompatTextView

        init {
            tvDate = itemView.findViewById(R.id.tv_date)
            childRecyclerView = itemView.findViewById(R.id.child_recycler_view)
            // btnEdit = itemView.findViewById(R.id.btn_edit)
            rlFilter = itemView.findViewById(R.id.rl_filter)
            ivCalender = itemView.findViewById(R.id.iv_calender)
            btnInfo = itemView.findViewById(R.id.btn_info)
            // btnDelete = itemView.findViewById(R.id.btn_delete)
        }
    }

    companion object {
        private const val TAG = "HealthVitalsAdapter"
    }
}