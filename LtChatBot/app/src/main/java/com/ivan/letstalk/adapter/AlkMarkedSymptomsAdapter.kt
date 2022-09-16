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
import com.ivan.letstalk.model.chat.ChatPossibleProblems
import com.ivan.letstalk.model.markedAlkSideEffects.Data

internal class AlkMarkedSymptomsAdapter(private var alkSideEffectsModel: ArrayList<Data>, private val click: onClickListener) :
    RecyclerView.Adapter<AlkMarkedSymptomsAdapter.MyViewHolder>() {
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
        holder.title.text = alkSideEffects.top_alk_details.description
        if (alkSideEffects.status == "ACTIVE"){
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_filled))
        } else {
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        }
        holder.ivFav.setOnClickListener{
            click.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return alkSideEffectsModel.size
    }

    fun deleteItem(position: Int) {
        alkSideEffectsModel.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, alkSideEffectsModel.size)
        // holder.itemView.visibility = View.GONE
    }

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var ivFav: ImageView = view.findViewById(R.id.iv_fav)
    }
    interface onClickListener{
        fun onClick(pos:Int)
    }
}