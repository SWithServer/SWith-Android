package com.example.swith.ui.study.round

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundAttendBinding
import com.example.swith.utils.base.BaseFragment
import com.example.swith.ui.adapter.AttendRVAdapter
import com.example.swith.ui.dialog.BottomSheet
import com.example.swith.ui.dialog.CustomAlertDialog
import com.example.swith.viewmodel.RoundViewModel

class RoundAttendFragment(private val curCount: Int) : BaseFragment<FragmentRoundAttendBinding>(R.layout.fragment_round_attend){
    private val viewModel: RoundViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.sessionLiveData.observe(viewLifecycleOwner, Observer {
            setVisibility(viewModel.isUpdateAvailable(), (viewModel.curUserAttend == null))
            (binding.rvAttend.adapter as AttendRVAdapter).setData(it.getAttendanceList)
        })

        viewModel.attendLiveEvent.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.result) {
                    1 -> {
                        CustomAlertDialog("출석 완료", "출석 처리되었습니다.")
                            .show(requireActivity().supportFragmentManager, "출석 완료")
                    }
                    2 -> {
                        CustomAlertDialog("지각", "지각 처리되었습니다.")
                            .show(requireActivity().supportFragmentManager, "지각")
                    }
                }
                (binding.rvAttend.adapter as AttendRVAdapter).updateUserStatus(it.result)
                binding.btnAttend.visibility = View.INVISIBLE
            }
        })

        viewModel.mutableErrorMessage.observe(viewLifecycleOwner, Observer {
            CustomAlertDialog("출석 오류", it)
                .show(requireActivity().supportFragmentManager, "출석 오류")
        })

        viewModel.sessionLiveData.observe(viewLifecycleOwner, Observer {
            setVisibility(viewModel.isUpdateAvailable(), viewModel.curUserAttend == null)
            (binding.rvAttend.adapter as AttendRVAdapter).setData(it.getAttendanceList)
        })
    }
    private fun initView(){
        with(binding){
            tvAttendMinTime.text = "출석 유효 시간 : ${viewModel.getAttendValidTime()}분"
            rvAttend.apply{
                adapter = AttendRVAdapter(viewModel.curUserAttend?.userIdx)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            btnAttend.setOnClickListener {
                BottomSheet("${curCount}회차 출석", "회차 시작 후 ${viewModel.getAttendValidTime()}분 까지 출석 가능", resources.getString(R.string.bottom_attend_guide)
                , "출석").apply {
                    setCustomListener(object: BottomSheet.customClickListener{
                        override fun onCheckClick() {
                            dismiss()
                            viewModel.updateCurAttend()
                        }
                    })
                }.show(requireActivity().supportFragmentManager, "bottomAttend")
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
}