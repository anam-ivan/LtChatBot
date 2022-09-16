package com.ivan.letstalk.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.SideEffectsModel
import com.ivan.letstalk.model.glossary.Data
import com.ivan.letstalk.ui.KnowYourSideEffectsDetails
import com.ivan.letstalk.ui.LoginActivity
import com.ivan.letstalk.ui.VerifyOTPActivity
import java.util.*

class KnowYourSideEffectsAdapter(dataList: List<Data>) :
    RecyclerView.Adapter<KnowYourSideEffectsAdapter.KnowYourSideEffectsHolder>(), SectionIndexer, Comparable<Any?> {
    var dataList: List<Data>
    private var mSectionPositions: ArrayList<Int>? = null
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowYourSideEffectsHolder {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.know_your_side_effects_row, null)
        return KnowYourSideEffectsHolder(view)
    }

    override fun onBindViewHolder(holder: KnowYourSideEffectsHolder, position: Int) {
        val data: Data = dataList[position]
        holder.tvTitle.text = data.title
        holder.tvShortDesc.text = data.shortDescription
        holder.tvSymptoms.text = data.symptoms
        holder.rootView.setOnClickListener{
            val intent = Intent(context, KnowYourSideEffectsDetails::class.java)
            intent.putExtra("title", data.title)
            intent.putExtra("symptoms", data.symptoms)
            intent.putExtra("shortDesc", data.shortDescription)
            intent.putExtra("description", data.description)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getSections(): Array<Any>? {
        val sections: MutableList<String> = ArrayList()
        mSectionPositions = ArrayList()
        var i = 0
        val size = dataList.size
        while (i < size) {
            val section = dataList[i].title!![0].toString().uppercase(Locale.getDefault())
            if (!sections.contains(section)) {
                sections.add(section)
                mSectionPositions!!.add(i)
            }
            i++
        }
        // return sections.toTypedArray<String>()
        return sections.toTypedArray() as Array<Any>
    }

    override fun getPositionForSection(i: Int): Int {
        return mSectionPositions!![i]
    }

    override fun getSectionForPosition(i: Int): Int {
        return 0
    }

    inner class KnowYourSideEffectsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvShortDesc: TextView
        var tvSymptoms: TextView
        var rootView: RelativeLayout

        init {
            tvTitle = itemView.findViewById(R.id.tv_title)
            tvShortDesc = itemView.findViewById(R.id.tv_short_desc)
            tvSymptoms = itemView.findViewById(R.id.tv_symptoms)
            rootView = itemView.findViewById(R.id.rootView)
        }
    }

    init {
        this.dataList = dataList
    }

    override fun compareTo(other: Any?): Int {
        TODO("Not yet implemented")
    }
}