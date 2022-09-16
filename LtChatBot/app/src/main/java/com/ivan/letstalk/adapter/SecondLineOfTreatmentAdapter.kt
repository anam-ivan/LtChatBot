package com.ivan.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.userDetails.SecondLineOfTreatment
import java.util.*

class SecondLineOfTreatmentAdapter(private var transactionalDetails: List<SecondLineOfTreatment>) :
    RecyclerView.Adapter<SecondLineOfTreatmentAdapter.SecondLineOfTreatmentVH>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondLineOfTreatmentVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_health_card_row, parent, false)
        return SecondLineOfTreatmentVH(view)
    }

    override fun onBindViewHolder(holder: SecondLineOfTreatmentVH, position: Int) {
        val healthCard = transactionalDetails[position]
        holder.tvName.text = healthCard.name?.let { capitalizedString(it) }
        holder.tvValueDetails.text = healthCard.valueDetails?.let { capitalizedString(it) }
    }

    private fun capitalizedString(name: String): String {
        var capitalizedString = ""
        if (name.trim { it <= ' ' } != "") {
            capitalizedString =
                name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
        }
        return capitalizedString
    }

    override fun getItemCount(): Int {
        return transactionalDetails.size
    }

    inner class SecondLineOfTreatmentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvValueDetails: TextView

        init {
            tvName = itemView.findViewById(R.id.tv_name)
            tvValueDetails = itemView.findViewById(R.id.tv_value_details)
        }
    }

    companion object {
        private const val TAG = "FirstLineOfTreatmentAdapter"
    }
}