package com.example.swith.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.swith.ui.MainActivity
import com.example.swith.ui.study.StudyActivity

abstract class BaseFragment<VB : ViewBinding>(@LayoutRes private val layoutRes: Int): Fragment(){
    private var _viewBinding: VB? = null
    protected val binding get() = _viewBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    fun setVisiblebar(backButton: Boolean,noticeButton: Boolean,title:String){
        activity?.let {
            if (it is MainActivity) {
                it.setVisibleBar(backButton,noticeButton,title)
            }
        }
    }

    fun setManagerLayout(isManager: Boolean){
        activity?.let{
            if (it is StudyActivity){
                it.setVisibleBar(isManager)
            }
        }
    }
}
