package com.example.swith.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.swith.ui.study.round.RoundAttendFragment
import com.example.swith.ui.study.round.RoundDetailFragment
import com.example.swith.ui.study.round.RoundMemoFragment

class RoundTabVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> RoundDetailFragment()
            1 -> RoundAttendFragment()
            else -> RoundMemoFragment()
        }
    }
}