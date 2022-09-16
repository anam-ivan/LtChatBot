package com.ivan.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ivan.letstalk.R


class CustomAdapter(applicationContext: Context, flags: IntArray, fruit: Array<String>) :
    BaseAdapter() {
    var context: Context
    var images: IntArray
    var fruit: Array<String>
    var inflter: LayoutInflater
    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        view = inflter.inflate(R.layout.spinner_custom_layout, null)
        val icon: ImageView = view.findViewById<View>(R.id.imageView) as ImageView
        val names = view.findViewById<View>(R.id.textView) as TextView
        icon.setImageResource(images[i])
        names.text = fruit[i]
        return view
    }

    init {
        context = applicationContext
        images = flags
        this.fruit = fruit
        inflter = LayoutInflater.from(applicationContext)
    }
}