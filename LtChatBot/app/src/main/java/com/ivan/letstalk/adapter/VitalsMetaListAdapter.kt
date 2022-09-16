package com.ivan.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ivan.letstalk.R
import com.ivan.letstalk.model.healthVitals.Data

class VitalsMetaListAdapter(ctx: Context, countries: ArrayList<Data>) : ArrayAdapter<Data>(ctx, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    private fun createItemView(position: Int, recycledView: View?, parent: ViewGroup):View {
        val country = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.item_custom_spinner,
            parent,
            false
        )
        val metaIcon: ImageView = view.findViewById<View>(R.id.iv_meta) as ImageView
        val metaName = view.findViewById<View>(R.id.iv_name) as TextView
        if (country != null) {
            metaName.text = country.name
        }
        /*Glide.with(this)
            .load("http://letstalk.dev13.ivantechnology.in/".plus(country.icon)
                .placeholder(R.drawable.img_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(metaIcon)*/
        if (country != null) {
            Glide.with(context).load("http://letstalk.dev13.ivantechnology.in/".plus(country.icon))
                 //.placeholder(R.drawable.img_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(metaIcon)
        }
        return view
    }
}