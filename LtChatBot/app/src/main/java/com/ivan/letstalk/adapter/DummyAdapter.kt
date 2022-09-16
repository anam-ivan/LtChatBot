package com.ivan.letstalk.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.helper.Helpers
import java.util.*

 class DummyAdapter(private val mDataArray: List<String>?) :
    RecyclerView.Adapter<DummyAdapter.ViewHolder>(), SectionIndexer {
    private val mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#"
    private var sectionsTranslator = HashMap<Int, Int>()
    private var mSectionPositions: ArrayList<Int>? = null

    override fun getItemCount(): Int {
        Log.d("mDataArray",mDataArray.toString())
        return mDataArray?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.side_effects_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSideEffectName.text = mDataArray!![position]
        // holder.tvSideEffectDesc.text = mDataArray!![position]
        /*holder.mImageButton.setOnClickListener {
            // mDataArray.removeAt(holder.adapterPosition)
            notifyDataSetChanged()
        }*/
    }

    override fun getSectionForPosition(position: Int): Int {
        return 0
    }

    override fun getSections(): Array<Any> {
        /* List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = mDataArray.size(); i < size; i++) {
            String section = String.valueOf(mDataArray.get(i).charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        } */
        val sections: MutableList<String> = ArrayList(27)
        val alphabetFull = ArrayList<String>()
        // alphabetFull.toArray(alphabetFull.toTypedArray())
        mSectionPositions = ArrayList()
        run {
            var i = 0
            val size = mDataArray!!.size
            while (i < size) {
                val section =
                    mDataArray[i][0].toString().uppercase(Locale.getDefault())
                if (!sections.contains(section)) {
                    sections.add(section)
                    mSectionPositions!!.add(i)
                }
                i++
            }
        }
        for (i in 0 until mSections.length) {
            alphabetFull.add(mSections[i].toString())
        }
        sectionsTranslator = Helpers.Companion.sectionsHelper(sections, alphabetFull)
        // return arrayOf(alphabetFull.toTypedArray<String>())
        //  return sections.toTypedArray() as Array<Any>
        return alphabetFull.toTypedArray() as Array<Any>
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mSectionPositions!![sectionsTranslator[sectionIndex]!!]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSideEffectName: TextView
        var tvSideEffectDesc: TextView
        // var tvSideEffectSymptoms: TextView

        init {
            tvSideEffectName = itemView.findViewById(R.id.title)
            tvSideEffectDesc = itemView.findViewById(R.id.desc)
            // tvSideEffectSymptoms = itemView.findViewById(R.id.tv_side_effect_symptoms)
        }
    }
}