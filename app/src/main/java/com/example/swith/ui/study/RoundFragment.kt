package com.example.swith.ui.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundBinding

class RoundFragment : Fragment() {
    lateinit var binding: FragmentRoundBinding
    private val viewModel: RoundViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_round, container, false)

        // Check Box 선택 여부 리스너
        binding.roundPreviousCb.setOnCheckedChangeListener { view, isChecked ->
            when(view.isChecked){
                true -> {
                    // TODO : 과거 라운드 포함해서 보여줘야 함
                }
                false -> {
                    // TODO : 현재랑 현재 이후만 보여주기
                }
            }
        }

        viewModel.roundLiveData.observe(viewLifecycleOwner, Observer {
            // TODO : 회차 List 보여주기
        })

        return binding.root
    }
}