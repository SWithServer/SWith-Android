package com.example.swith.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.swith.R
import com.example.swith.databinding.FragmentProfileBinding
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.ProfileState
import com.example.swith.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile){
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
        initListener()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.profileData.collect {
                        when (it) {
                            is ProfileState.Loading -> {
                                binding.flLoadingLayout.visibility = View.VISIBLE
                            }
                            is ProfileState.Success -> {
                                binding.profile = it.profile
                                binding.flLoadingLayout.visibility = View.INVISIBLE
                            }
                            else -> {
                                binding.flLoadingLayout.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        setVisiblebar(backButton = false, noticeButton = true, getString(R.string.profile), "")
//
//        mProfileViewModel?.getCurrentProfile()?.observe(viewLifecycleOwner, Observer {
//            Log.e("doori", "observer = $it")
//            setShowDimmed(false)
//            if (it.isSuccess) {
//                it.result.interestIdx1?.apply {
//                    binding.tvInteresting1.text = resources.getStringArray(R.array.intersting)[this]
//                }
//                it.result.interestIdx2?.apply {
//                    binding.tvInteresting2.text = resources.getStringArray(R.array.intersting)[this]
//                }
//            } else {
//                Toast.makeText(context, "잠시 후 다시 시작해주세요", Toast.LENGTH_SHORT).show()
//            }
//        })
//        getProfile()
    }

    private fun initListener(){
        with(binding){
            // 프로필 관리 페이지로 이동
            ivSetting.setOnClickListener {
                startActivity(Intent(requireContext(), ProfileModifyActivity::class.java).apply {
                    putExtra("profile", profile)
                })
            }
            // 로그아웃
            //            R.id.btn_logout -> {
//                context?.let { SharedPrefManager(it).deleteLoginData() }
////                kakaoLogout()
//                goLoginPage()
//            }
            btnLogout.setOnClickListener {
                // Todo : 다이얼로그 띄운 후 로그아웃
            }
            btnResume.setOnClickListener {
                goResumePage()
            }

        }
    }

//    override fun onResume() {
//        super.onResume()
//        initView()
//    }

//    fun kakaoLogout() {
//        UserApiClient.instance.logout { error ->
//            if (error != null) {
//                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
//            } else {
//                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
//            }
//        }
//    }

}