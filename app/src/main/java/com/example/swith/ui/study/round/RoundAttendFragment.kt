package com.example.swith.ui.study.round

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundAttendBinding
import com.example.swith.utils.base.BaseFragment
import com.example.swith.ui.adapter.AttendRVAdapter
import com.example.swith.ui.dialog.BottomSheet
import com.example.swith.viewmodel.AttendViewModel

class RoundAttendFragment(private val curCount: Int) : BaseFragment<FragmentRoundAttendBinding>(R.layout.fragment_round_attend){
    private val viewModel: AttendViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        // 현재 회차 받기
        viewModel.setCurUser(5)
        setVisibility(viewModel.isUpdateAvailable(), viewModel.attendLiveData.value == null)

        viewModel.attendLiveData.observe(viewLifecycleOwner, Observer {
            setVisibility(viewModel.isUpdateAvailable(), viewModel.attendLiveData.value == null)
            (binding.rvAttend.adapter as AttendRVAdapter).setData(it)
        })

        with(binding){
            tvAttendMinTime.text = "출석 유효 시간 : ${viewModel.attendLimit}분"
            rvAttend.apply{
                adapter = AttendRVAdapter(5)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            btnAttend.setOnClickListener {
                BottomSheet("${curCount}회차 출석", "회차 시작 후 ${viewModel.attendLimit}분 까지 출석 가능", resources.getString(R.string.bottom_attend_guide)
                , "출석 하기").apply {
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


    // Todo : 10분 넘었을 때 지각으로 처리되는 로직도 추가해야함
}