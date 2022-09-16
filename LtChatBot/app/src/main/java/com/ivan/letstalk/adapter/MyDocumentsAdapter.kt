package com.ivan.letstalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.MyDocumentModel

internal class MyDocumentsAdapter(private var sideEffectsModel: List<MyDocumentModel>) :
    RecyclerView.Adapter<MyDocumentsAdapter.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_documents_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val doc = sideEffectsModel[position]
        holder.tvDocTitle.text = doc.getDocTitle()
        holder.tvDocCategory.text = doc.getDocCategory()
    }

    override fun getItemCount(): Int {
        return sideEffectsModel.size
    }

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDocTitle: TextView = view.findViewById(R.id.tv_doc_title)
        var tvDocCategory: TextView = view.findViewById(R.id.tv_doc_category)
    }
}