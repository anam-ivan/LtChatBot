package com.ivan.letstalk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ivan.letstalk.R
import com.ivan.letstalk.model.doctorsAssigned.Data
import de.hdodenhof.circleimageview.CircleImageView


class DoctorsAssignedForPatientAdapter(var doctorsAssigned: List<Data>) :
    RecyclerView.Adapter<DoctorsAssignedForPatientAdapter.ProgressiveDataVH>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressiveDataVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.doctors_assigned_row, parent, false)
        return ProgressiveDataVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProgressiveDataVH, position: Int) {
        val doctorsAssigned = doctorsAssigned[position]
        holder.tvDoctorName.text = doctorsAssigned.doctorDetails!!.name
        holder.tvMobile.text = ("+").plus(doctorsAssigned.doctorDetails!!.phoneNumber)
        val number = doctorsAssigned.doctorDetails!!.phoneNumber
        holder.tvMobile.setOnClickListener {
            /*val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            context.startActivity(callIntent)*/
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
            context.startActivity(intent)
        }
        holder.tvSpecialization.text = doctorsAssigned.doctorDetails!!.specialization
        if (doctorsAssigned.doctorDetails!!.qualification == null) {
            holder.tvQualification.visibility = View.GONE
        } else {
            holder.tvQualification.text = doctorsAssigned.doctorDetails!!.qualification
        }
        // holder.tvEmergencyMobile.text = doctorsAssigned.doctorDetails!!.emergencyMobile
        Glide.with(context)
            .load("http://letstalk.dev13.ivantechnology.in/".plus(doctorsAssigned.doctorDetails!!.image))
            .placeholder(R.drawable.img_placeholder)
            .into(holder.ivDoctorImg)
    }

    /*private fun capitalizedString(name: String): String {
        var capitalizedString = ""
        if (name.trim { it <= ' ' } != "") {
            capitalizedString =
                name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
        }
        return capitalizedString
    }*/

    override fun getItemCount(): Int {
        return doctorsAssigned.size
    }

    inner class ProgressiveDataVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDoctorName: TextView
        var tvQualification: TextView
        var tvMobile: TextView
        var tvSpecialization: TextView
        // var tvEmergencyMobile: TextView
        var ivDoctorImg: CircleImageView

        init {
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name)
            tvQualification = itemView.findViewById(R.id.tv_qualification)
            tvMobile = itemView.findViewById(R.id.tv_mobile)
            tvSpecialization = itemView.findViewById(R.id.tv_doctor_specialization)
            // tvEmergencyMobile = itemView.findViewById(R.id.tv_emergency_mobile)
            ivDoctorImg = itemView.findViewById(R.id.iv_doctor_img)

        }
    }

    companion object {
        private const val TAG = "DoctorsAssignedForPatientAdapter"
    }
}