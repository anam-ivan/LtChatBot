package com.ivan.letstalk.helper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ivan.letstalk.model.AlkSideEffectsModel

class PageAdapter(fragmentManager: FragmentManager, private val size: Int, private val items: ArrayList<String>) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val firstItem = ((position + 1) * 3) - 2
        val lastItem = ((position + 1) * 3)
        val itemSet = arrayListOf<String>()
        for (i in firstItem..lastItem) {
            if (i <= items.size)
                itemSet.add(items[i - 1].toString())
        }
        return ItemFragment.newInstance(itemSet)
    }

    override fun getCount(): Int {
        return size
    }
}