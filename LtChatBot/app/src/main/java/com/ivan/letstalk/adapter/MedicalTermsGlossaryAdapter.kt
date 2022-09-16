package com.ivan.letstalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.glossary.Data
import java.util.*


class MedicalTermsGlossaryAdapter(dataList: List<Data>) :
    RecyclerView.Adapter<MedicalTermsGlossaryAdapter.MedicalTermsGlossaryHolder>(), SectionIndexer, Comparable<Any?> {
    var dataList: List<Data>
    private var mSectionPositions: ArrayList<Int>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalTermsGlossaryHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.side_effects_row, null)
        return MedicalTermsGlossaryHolder(view)
    }

    override fun onBindViewHolder(holder: MedicalTermsGlossaryHolder, position: Int) {
        val data: Data = dataList[position]
        holder.title.text = data.title
        holder.desc.text = data.description
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

    inner class MedicalTermsGlossaryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var desc: TextView

        init {
            title = itemView.findViewById(R.id.title)
            desc = itemView.findViewById(R.id.desc)
        }
    }

    init {
        this.dataList = dataList
    }

    override fun compareTo(other: Any?): Int {
        TODO("Not yet implemented")
    }
}
