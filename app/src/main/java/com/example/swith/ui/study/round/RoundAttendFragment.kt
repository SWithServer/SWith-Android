package com.example.swith.ui.study.round

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studywith.ConfirmDialog
import com.example.studywith.ConfirmDialogInterface
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundAttendBinding
import com.example.swith.ui.BaseFragment
import com.example.swith.ui.adapter.AttendRVAdapter
import com.example.swith.viewmodel.AttendViewModel

class RoundAttendFragment : BaseFragment<FragmentRoundAttendBinding>(R.layout.fragment_round_attend){
    private val viewModel: AttendViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        viewModel.setCurUser(5)
        setVisibility(viewModel.isUpdateAvailable(), viewModel.attendLiveData.value == null)

        viewModel.attendLiveData.observe(viewLifecycleOwner, Observer {
            setVisibility(viewModel.isUpdateAvailable(), viewModel.attendLiveData.value == null)
            (binding.rvAttend.adapter as AttendRVAdapter).setData(it)
        })

        with(binding){
            tvAttendMinTime.text = "출석 유효 시간 : ${viewModel.attendLimit}분"
            rvAttend.apply{
                adapter = AttendRVAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            btnAttend.setOnClickListener {
                BottomSheet(1, viewModel.attendLimit).apply {
                    setCustomListener(object: BottomSheet.customClickListener{
                        override fun onAttendClick() {
                            dismiss()
                            viewModel.updateCurAttend()
                        }

                        override fun onCancelClick() {
                            dismiss()
                        }
                    })
                }.show(requireActivity().supportFragmentManager, "bottomSheet")
            }
        }
    }

    private fun setVisibility(updateAvail: Boolean, isNull: Boolean){
        with(binding) {
            btnAttend.visibility = if (updateAvail) View.VISIBLE else View.INVISIBLE
            svAttend.visibility = if (isNull) View.INVISIBLE else View.VISIBLE
            tvNoPeople.visibility = if (isNull) View.VISIBLE else View.INVISIBLE
        }
    }


    // Todo : 10분 넘었을 때 지각으로 처리되는 로직도 추가해야함
}