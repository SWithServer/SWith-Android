package com.example.swith.ui.resume

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.swith.R
import com.example.swith.data.ResumeResult
import com.example.swith.databinding.FragmentProfileBinding
import com.example.swith.databinding.FragmentResumeBinding
import com.example.swith.databinding.ResumeDetailDialogBinding
import com.example.swith.databinding.ResumeDialogBinding
import com.example.swith.ui.adapter.ResumeAdapter
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.ui.dialog.CustomResumeDialog
import com.example.swith.utils.CustomBinder
import com.example.swith.utils.SharedPrefManager
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.ProfileViewModel
import com.example.swith.viewmodel.ResumeViewModel

class ResumeFragment : BaseFragment<FragmentResumeBinding>(R.layout.fragment_resume) {
    private var mResumeViewModel: ResumeViewModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    companion object {
        @JvmStatic
        fun newInstance()=ResumeFragment()
    }

    fun initData(){

    }
    fun initView(){
        Log.e("doori","resumePage")
        setVisiblebar(true,false,"","")
        mResumeViewModel = activity?.let {
            ViewModelProvider(it, ResumeViewModel.Factory())[ResumeViewModel::class.java]
        }
        mResumeViewModel?.getCurrentResume()?.observe(viewLifecycleOwner, Observer {
            Log.e("doori","observer = $it")
            if(it!=null){
                if(it.isSuccess) {
                    setAdaper(it.result)
                }else{
                    Toast.makeText(context,"지원서가 없습니다.",Toast.LENGTH_SHORT).show()
                    goProfilePage()
                }
            }else{
                Toast.makeText(context,"지원서가 없습니다.",Toast.LENGTH_SHORT).show()
                goProfilePage()
            }

        })
        mResumeViewModel?.requestCurrentResume(context?.let { SharedPrefManager(it).getLoginData() }!!.userIdx)
        //mResumeViewModel?.requestCurrentResume(1)

    }

    private fun setAdaper(data:List<ResumeResult>) {
        var adapter=ResumeAdapter()
        adapter.setData(data)
        adapter.setItemClickListener(object :ResumeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Log.e("doori","position = $position , resume=${data.get(position)}")

                //TODO 다이얼로그가 아니라 fragment나 activity로 바꿔야함
                showDialog(data.get(position))
            }

        })
        binding.rcResume.adapter=adapter
    }

    fun showDialog(data:ResumeResult){
        DataBindingUtil.inflate<ResumeDetailDialogBinding>(
            LayoutInflater.from(requireActivity()), R.layout.resume_detail_dialog, null, false
        ).apply {
            this.resume=data
            this.tvDate.text="${data.createdAt.get(0)}년 ${data.createdAt.get(1)}월 ${data.createdAt.get(2)}일"
            this.tvLocation.text = "${data.regionIdx1} , ${data.regionIdx2}"
            this.tvStatusDetail.text=getStatus(data.status)
            this.resumeDialog =
                CustomBinder.showCustomDialog(requireContext(),
                    root,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    object : CustomDialog.DialogClickListener {
                        override fun onClose() {
                            //showCancleDialog()
                        }

                        override fun onConfirm() {
                            //showSaveDialog()
                        }
                    })
        }
    }

    fun getStatus(status:Int):String{
        Log.e("doori","status = $status")
        if (status == 0) {
            return  "승인대기"
        } else if (status == 1) {
            return "승인"
        } else if (status == 2) {
            return "거절"
        } else if (status == 3) {
            return "추방"
        }
        return "대기"

    }

    fun showSaveDialog(){
        DataBindingUtil.inflate<ResumeDialogBinding>(
            LayoutInflater.from(requireActivity()), R.layout.resume_dialog, null, false
        ).apply {
            this.resumeDialog =
                CustomBinder.showResumemDialog(getString(R.string.resume_save),requireContext(),
                    root,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    object : CustomResumeDialog.DialogClickListener {
                        override fun onClose() {

                        }

                        override fun onConfirm() {

                        }
                    })
        }
    }
    fun showCancleDialog(){
        DataBindingUtil.inflate<ResumeDialogBinding>(
            LayoutInflater.from(requireActivity()), R.layout.resume_dialog, null, false
        ).apply {
            this.resumeDialog =
                CustomBinder.showResumemDialog(getString(R.string.resume_cancel),requireContext(),
                    root,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    object : CustomResumeDialog.DialogClickListener {
                        override fun onClose() {

                        }

                        override fun onConfirm() {

                        }
                    })
        }
    }
}