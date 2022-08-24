package com.example.swith.ui.resume

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.swith.R
import com.example.swith.databinding.DialogCreateBinding
import com.example.swith.databinding.FragmentResumeDetailBinding
import com.example.swith.databinding.ResumeDialogBinding
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.ui.dialog.CustomResumeDialog
import com.example.swith.utils.CustomBinder
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.ProfileViewModel
import com.example.swith.viewmodel.ResumeDetailViewModel

class ResumeDetailFragment : BaseFragment<FragmentResumeDetailBinding>(R.layout.fragment_resume_detail), View.OnClickListener {
    private var mResumeDetailViewModel: ResumeDetailViewModel? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    companion object {
        @JvmStatic
        fun newInstance()=ResumeDetailFragment()
    }

    fun initData(){

    }
    fun initView(){
        setVisiblebar(true,false,"","")
        binding.clickListener=this@ResumeDetailFragment

        mResumeDetailViewModel = activity?.let {
            ViewModelProvider(it, ResumeDetailViewModel.Factory())[ResumeDetailViewModel::class.java].apply {
                //binding.profileViewModel = this
                binding.lifecycleOwner = this@ResumeDetailFragment
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_save->{
                showSaveDialog()
            }
            R.id.btn_cancel->{
                showCancelDialog()
            }
        }
    }

    fun showCancelDialog(){
        DataBindingUtil.inflate<ResumeDialogBinding>(
            LayoutInflater.from(requireActivity()), R.layout.resume_dialog, null, false
        ).apply {
            this.resumeDialog =
                CustomBinder.showResumemDialog(getString(R.string.resume_cancel), requireContext(),
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

    fun showSaveDialog(){
        DataBindingUtil.inflate<ResumeDialogBinding>(
            LayoutInflater.from(activity), R.layout.resume_dialog, null, false
        ).apply {
            this.resumeDialog =
                CustomBinder.showResumemDialog(getString(R.string.resume_save), requireContext(),
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