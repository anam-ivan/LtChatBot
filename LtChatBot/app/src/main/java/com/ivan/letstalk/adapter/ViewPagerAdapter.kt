package com.ivan.letstalk.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ivan.letstalk.fragment.BasicInfoFragment
import com.ivan.letstalk.fragment.DoctorsAssignedFragment
import com.ivan.letstalk.fragment.LineOfTreatmentFragment
import com.ivan.letstalk.fragment.ProgressiveDataFragment

private const val NUM_TABS = 4

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return BasicInfoFragment()
            1 -> return LineOfTreatmentFragment()
            2 -> return ProgressiveDataFragment()
        }
        return DoctorsAssignedFragment()
    }
}