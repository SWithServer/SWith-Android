package com.example.swith.ui.resume

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.swith.R
import com.example.swith.databinding.FragmentProfileBinding
import com.example.swith.databinding.FragmentResumeBinding
import com.example.swith.ui.adapter.ResumeAdapter
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.ProfileViewModel
import com.example.swith.viewmodel.ResumeViewModel

class ResumeFragment : BaseFragment<FragmentResumeBinding>(R.layout.fragment_resume) {
    private var mResumeViewModel: ResumeViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        setVisiblebar(true,false,"","")
        mResumeViewModel = activity?.let {
            ViewModelProvider(it, ResumeViewModel.Factory())[ResumeViewModel::class.java]
        }
        var adapter=ResumeAdapter()
        var testList = listOf<String>("asd","asd","asd","asdasdasd")
        adapter.setData(testList)
        adapter.setItemClickListener(object :ResumeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Log.e("doori","position = $position")
                goResumeDetailPage()
            }

        })
        binding.rcResume.adapter=adapter
    }
}