package com.example.swith.ui.study.round

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundMemoBinding

class RoundMemoFragment : Fragment() {
    lateinit var binding: FragmentRoundMemoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_round_memo, container, false)
        return binding.root
    }
}