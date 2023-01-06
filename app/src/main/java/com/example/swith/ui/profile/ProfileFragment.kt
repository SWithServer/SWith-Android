package com.example.swith.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.swith.R
import com.example.swith.databinding.FragmentProfileBinding
import com.example.swith.utils.SharedPrefManager
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.ProfileViewModel
import com.kakao.sdk.user.UserApiClient

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile),
    View.OnClickListener {
    private var mProfileViewModel: ProfileViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
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
        setShowDimmed(false)

        binding.clickListener = this@ProfileFragment
        setVisiblebar(false, true, getString(R.string.profile), "")
        mProfileViewModel = activity?.let {
            ViewModelProvider(it, ProfileViewModel.Factory())[ProfileViewModel::class.java].apply {
                binding.profileViewModel = this
                binding.lifecycleOwner = this@ProfileFragment
            }
        }
        mProfileViewModel?.getCurrentProfile()?.observe(viewLifecycleOwner, Observer {
            Log.e("doori", "observer = $it")
            setShowDimmed(false)
            if (it.isSuccess) {
                it.result.interestIdx1?.apply {
                    binding.tvInteresting1.text = resources.getStringArray(R.array.intersting)[this]
                }
                it.result.interestIdx2?.apply {
                    binding.tvInteresting2.text = resources.getStringArray(R.array.intersting)[this]
                }
            } else {
                Toast.makeText(context, "잠시 후 다시 시작해주세요", Toast.LENGTH_SHORT).show()
            }
        })
        getProfile()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_setting -> {
                Intent(context, ProfileModifyActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.btn_resume -> {
                goResumePage()
            }
            R.id.btn_logout -> {
                context?.let { SharedPrefManager(it).deleteLoginData() }
                kakaoLogout()
                goLoginPage()
            }
        }
    }

    private fun getProfile() {
        setShowDimmed(true)
        mProfileViewModel?.requestCurrentProfile(
            com.example.swith.entity.ProfileRequest(
                context?.let { SharedPrefManager(it).getLoginData() }!!.userIdx as Long
            )
        )
    }

    private fun setShowDimmed(isLoading: Boolean) {
        mProfileViewModel?.apply {
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    fun kakaoLogout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }

}