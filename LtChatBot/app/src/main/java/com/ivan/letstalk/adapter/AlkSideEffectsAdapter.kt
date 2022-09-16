package com.ivan.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.AlkSideEffectsModel
import com.ivan.letstalk.model.topAlkSideEffects.Data

internal class AlkSideEffectsAdapter(private var alkSideEffectsModel: List<Data>) :
    RecyclerView.Adapter<AlkSideEffectsAdapter.MyViewHolder>() {
    private lateinit var context : Context
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context  = parent.context
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.alk_side_effects_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val alkSideEffects = alkSideEffectsModel[position]
        holder.title.text = alkSideEffects.description
        if (alkSideEffects.status == "ACTIVE"){
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_filled))
        } else {
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        }
    }

    override fun getItemCount(): Int {
        return alkSideEffectsModel.size
    }

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var ivFav: ImageView = view.findViewById(R.id.iv_fav)
    }
}