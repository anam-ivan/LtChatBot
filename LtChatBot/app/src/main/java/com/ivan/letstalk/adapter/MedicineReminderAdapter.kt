package com.ivan.letstalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.MedicineReminderModel

class MedicineReminderAdapter(private var medicineReminderModel: List<MedicineReminderModel>) :
    RecyclerView.Adapter<MedicineReminderAdapter.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.medicine_scheduled_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val medicineReminderModel = medicineReminderModel[position]
        holder.medicineName.text = medicineReminderModel.getMedicineName()
        holder.noOfPills.text = medicineReminderModel.getNoOfPills().toString()
        holder.dosage.text = medicineReminderModel.getDosage()
    }

    override fun getItemCount(): Int {
        return medicineReminderModel.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var medicineName: TextView = view.findViewById(R.id.tv_medicine_name)
        var noOfPills: TextView = view.findViewById(R.id.tv_no_of_pills)
        var dosage: TextView = view.findViewById(R.id.tv_dosage)
    }
}