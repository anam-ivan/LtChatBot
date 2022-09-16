package com.ivan.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.documents.Data

class DocumentCategoryListAdapter(ctx: Context, documentCategoryList: ArrayList<Data>) : ArrayAdapter<Data>(ctx, 0, documentCategoryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    private fun createItemView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val documentCategoryList = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.document_category_list_custom,
            parent,
            false
        )
        val metaName = view.findViewById<View>(R.id.iv_name) as TextView
        if (documentCategoryList != null) {
            metaName.text = documentCategoryList.documentCategoryName
        }
        return view
    }
}