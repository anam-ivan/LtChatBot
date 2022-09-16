package com.ivan.letstalk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.userDetails.Gen

class LineOfTreatmentAdapter(var lineOfTreatment: List<Gen>) :
    RecyclerView.Adapter<LineOfTreatmentAdapter.LineOfTreatmentVH>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineOfTreatmentVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.line_of_treatment_row, parent, false)
        return LineOfTreatmentVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LineOfTreatmentVH, position: Int) {
        val lineOfTreatment = lineOfTreatment[position]
        holder.tvHeading.text = lineOfTreatment.name
        val lineOfTreatmentChildAdapter = LineOfTreatmentChildAdapter(lineOfTreatment.value)
        holder.rvLineOfTreatmentChild.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        holder.rvLineOfTreatmentChild.adapter = lineOfTreatmentChildAdapter
    }

    override fun getItemCount(): Int {
        return lineOfTreatment.size
    }

    inner class LineOfTreatmentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvHeading: TextView
        var rvLineOfTreatmentChild: RecyclerView

        init {
            tvHeading = itemView.findViewById(R.id.tv_heading)
            rvLineOfTreatmentChild = itemView.findViewById(R.id.rv_line_of_treatment_child)
        }
    }

    companion object {
        private const val TAG = "LineOfTreatmentAdapter"
    }
}