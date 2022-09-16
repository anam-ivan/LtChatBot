package com.ivan.letstalk.helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R

class ItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {

        // Creates the view controlled by the fragment
        val view = inflater.inflate(R.layout.page, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)

        // Retrieve and display the movie data from the Bundle
        val args = arguments
        recycler.layoutManager = GridLayoutManager(activity,3)
        recycler.adapter = ItemAdapter(args?.getStringArrayList("items")!!, this.requireActivity())

        return view
    }
    companion object {

        fun newInstance(items: ArrayList<String>): ItemFragment {

            val args = Bundle()
            args.putStringArrayList("items", items)

            val fragment = ItemFragment()
            fragment.arguments = args
            return fragment
        }
    }
}