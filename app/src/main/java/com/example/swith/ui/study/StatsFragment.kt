package com.example.swith.ui.study

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.swith.R
import com.example.swith.databinding.FragmentStatsBinding
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.RoundViewModel

class StatsFragment : BaseFragment<FragmentStatsBinding>(R.layout.fragment_stats) {
    private val viewModel : RoundViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}