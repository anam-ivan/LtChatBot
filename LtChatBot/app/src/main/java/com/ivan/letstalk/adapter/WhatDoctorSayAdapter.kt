package com.ivan.letstalk.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.databinding.DoctorSayItemBinding
import com.ivan.letstalk.model.glossary.Data
import com.ivan.letstalk.ui.WhatYourDoctorSayDetails

class WhatDoctorSayAdapter(
    private val photosList: List<Data>,
    private var context: Context
) : RecyclerView.Adapter<WhatDoctorSayAdapter.PhotosHolder>() {

    class PhotosHolder(val itemLayoutBinding: DoctorSayItemBinding) :
        RecyclerView.ViewHolder(itemLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosHolder {
        context = parent.context
        val itemLayoutBinding = DataBindingUtil.inflate<DoctorSayItemBinding>(
            LayoutInflater.from(parent.context), R.layout.doctor_say_item, parent, false
        )
        return PhotosHolder(itemLayoutBinding)
    }

    override fun getItemCount(): Int {
        Log.d("photosList", photosList.size.toString())
        return photosList.size
    }

    override fun onBindViewHolder(holder: PhotosHolder, position: Int) {
        holder.itemLayoutBinding.photo = photosList[position]
        val data: Data = photosList[position]
        holder.itemView.setOnClickListener {
            Log.d("position", position.toString())
            val intent = Intent(context, WhatYourDoctorSayDetails::class.java)
            intent.putExtra("title", data.title)
            intent.putExtra("shortDesc", data.shortDescription)
            intent.putExtra("description", data.description)
            context.startActivity(intent)
        }
    }


}