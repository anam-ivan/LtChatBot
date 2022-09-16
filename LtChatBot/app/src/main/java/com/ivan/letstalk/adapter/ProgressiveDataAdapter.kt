package com.ivan.letstalk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.userDetails.WithoutGen
import java.util.*

class ProgressiveDataAdapter(var healthDetails: List<WithoutGen>) :
    RecyclerView.Adapter<ProgressiveDataAdapter.ProgressiveDataVH>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressiveDataVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_health_card_row, parent, false)
        return ProgressiveDataVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProgressiveDataVH, position: Int) {
        val healthCard = healthDetails[position]
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
        return healthDetails.size
    }

    inner class ProgressiveDataVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvValueDetails: TextView

        init {
            tvName = itemView.findViewById(R.id.tv_name)
            tvValueDetails = itemView.findViewById(R.id.tv_value_details)
        }
    }

    companion object {
        private const val TAG = "ProgressiveDataAdapter"
    }
}