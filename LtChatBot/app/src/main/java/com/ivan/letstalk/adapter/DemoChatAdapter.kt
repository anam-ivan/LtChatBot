package com.ivan.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.FaqModel

class DemoChatAdapter(var faqList: List<FaqModel>) :
    RecyclerView.Adapter<DemoChatAdapter.MovieVH>() {
    private lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        context  = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_message_received_item, parent, false)
        return MovieVH(view)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val faqs = faqList[position]
        holder.faqTitle.text = faqs.title
        holder.faqDesc.text = faqs.desc
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var faqTitle: TextView
        var faqDesc: TextView

        init {
            faqTitle = itemView.findViewById(R.id.faq_title)
            faqDesc = itemView.findViewById(R.id.faq_desc)
        }
    }

    companion object {
        private const val TAG = "FaqAdapter"
    }
}