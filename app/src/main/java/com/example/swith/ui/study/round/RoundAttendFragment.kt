package com.example.swith.ui.study.round

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundAttendBinding
import com.example.swith.ui.BaseFragment
import com.example.swith.ui.adapter.AttendRVAdapter
import com.example.swith.viewmodel.AttendViewModel

class RoundAttendFragment : BaseFragment<FragmentRoundAttendBinding>(R.layout.fragment_round_attend) {
    private val viewModel: AttendViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        viewModel.setCurUser(5)

        viewModel.attendLiveData.observe(viewLifecycleOwner, Observer {
            (binding.rvAttend.adapter as AttendRVAdapter).setData(it)
        })

        with(binding){
            tvAttendMinTime.text = "출석 유효 시간 : ${viewModel.attendLimit}분"
            rvAttend.apply{
                adapter = AttendRVAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            btnAttend.visibility = if (viewModel.isUpdateAvailable()) View.VISIBLE else View.INVISIBLE
            btnAttend.setOnClickListener {
                viewModel.updateCurAttend()
            }
        }
    }
}