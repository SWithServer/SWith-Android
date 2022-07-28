package com.example.swith.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.swith.R
import com.example.swith.databinding.FragmentProfileBinding
import com.example.swith.ui.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile),
    View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    companion object {
        const val TAG: String = "ProfileFragment"
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
    private fun initData() {

    }
    private fun initView() {
        binding.clickListener=this@ProfileFragment
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.iv_setting->{
                Toast.makeText(context,"setting",Toast.LENGTH_SHORT).show()
            }
            R.id.btn_resume->{
                Toast.makeText(context,"resume",Toast.LENGTH_SHORT).show()
            }
            R.id.tv_logout->{
                Toast.makeText(context,"logout",Toast.LENGTH_SHORT).show()
            }
        }
    }
}