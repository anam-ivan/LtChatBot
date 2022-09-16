package com.ivan.letstalk.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ivan.letstalk.R
import com.ivan.letstalk.model.myHealthVitals.Vital

class ExampleAdapter(private var healthElementDetails: List <Vital>) :
    RecyclerView.Adapter<ExampleAdapter.HealthVitalsVH>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthVitalsVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.example_list, parent, false)
        return HealthVitalsVH(view)
    }

    override fun onBindViewHolder(holder: HealthVitalsVH, position: Int) {
        val healthCard = healthElementDetails[position]
        holder.tvTitle.text = healthCard.title
        holder.tvValue.text = healthCard.value.plus(" ").plus(healthCard.unit)
        Glide.with(context).load("http://letstalk.dev13.ivantechnology.in/".plus(healthCard.icon))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imgView)
    }

    override fun getItemCount(): Int {
        Log.d("health_vitals", healthElementDetails.size.toString())
        return healthElementDetails.size
    }

    inner class HealthVitalsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgView : ImageView
        var tvTitle: TextView
        var tvValue: TextView

        init {
            imgView = itemView.findViewById(R.id.iv_health_vitals)
            tvTitle = itemView.findViewById(R.id.tv_title)
            tvValue = itemView.findViewById(R.id.tv_value)
        }
    }

    companion object {
        private const val TAG = "ExampleAdapterAdapter"
    }
}