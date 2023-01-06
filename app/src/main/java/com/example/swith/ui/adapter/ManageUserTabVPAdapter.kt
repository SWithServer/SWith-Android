package com.example.swith.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.swith.ui.manage.ManageUserApplication1Fragment
import com.example.swith.ui.manage.ManageUserApplication2Fragment

private const val NUM_TABS = 2

class ManageUserTabVPAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                Log.e("첫번째 프래그먼트", "true")
                return ManageUserApplication1Fragment()
            }
            else -> {
                Log.e("두번째 프래그먼트", "true")
                return ManageUserApplication2Fragment()
            }
        }
    }
}