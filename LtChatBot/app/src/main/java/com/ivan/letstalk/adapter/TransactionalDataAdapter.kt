package com.ivan.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.userDetails.Transactional
import java.util.*

class TransactionalDataAdapter(var transactionalDetails: List<Transactional>) :
    RecyclerView.Adapter<TransactionalDataAdapter.TransactionalVH>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionalVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_health_card_row, parent, false)
        return TransactionalVH(view)
    }

    override fun onBindViewHolder(holder: TransactionalVH, position: Int) {
        val healthCard = transactionalDetails[position]
        holder.tvName.text = healthCard.name
        holder.tvValueDetails.text = healthCard.valueDetails?.let { capitalizedString(it) }
        /*if (healthCard.valueDetails == "1") {
            holder.tvValueDetails.text = context.resources.getString(R.string.male)
        } else if (healthCard.valueDetails == "2") {
            holder.tvValueDetails.text = context.resources.getString(R.string.female)
        }
        if (healthCard.valueDetails?.isEmpty() == true) {
            holder.tvValueDetails.text = "N/A"
        }*/
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

    inner class TransactionalVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvValueDetails: TextView

        init {
            tvName = itemView.findViewById(R.id.tv_name)
            tvValueDetails = itemView.findViewById(R.id.tv_value_details)
        }
    }

    companion object {
        private const val TAG = "MyHealthCardAdapter"
    }
}