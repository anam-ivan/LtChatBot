package com.ivan.letstalk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.userDetails.Value
import java.util.*

class LineOfTreatmentChildAdapter(var lineOfTreatment: List<Value>) :
    RecyclerView.Adapter<LineOfTreatmentChildAdapter.LineOfTreatmentChildVH>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineOfTreatmentChildVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.line_of_treatment_child_row, parent, false)
        return LineOfTreatmentChildVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LineOfTreatmentChildVH, position: Int) {
        val lineOfTreatment = lineOfTreatment[position]
        holder.tvTreatmentPhase.text = lineOfTreatment.lineOfTreatment?.let { capitalizedString(it) }
        holder.tvTreatmentValue.text = capitalizedString(lineOfTreatment.valueDetails.toString())
    }

    override fun getItemCount(): Int {
        return lineOfTreatment.size
    }

    private fun capitalizedString(name: String): String {
        var capitalizedString = ""
        if (name.trim { it <= ' ' } != "") {
            capitalizedString =
                name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
        }
        return capitalizedString
    }

    inner class LineOfTreatmentChildVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTreatmentPhase: TextView
        var tvTreatmentValue: TextView

        init {
            tvTreatmentPhase = itemView.findViewById(R.id.tv_treatment_phase)
            tvTreatmentValue = itemView.findViewById(R.id.tv_treatment_value)
        }
    }

    companion object {
        private const val TAG = "LineOfTreatmentChildAdapter"
    }
}