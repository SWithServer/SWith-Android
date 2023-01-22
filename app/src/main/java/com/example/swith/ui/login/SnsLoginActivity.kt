package com.example.swith.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.data.R
import com.example.data.databinding.ActivitySnsLoginBinding
import com.example.data.ui.MainActivity
import com.example.data.ui.profile.ProfileModifyActivity
import com.example.data.utils.SharedPrefManager
import com.example.data.viewmodel.LoginViewModel
import com.example.swith.domain.entity.LoginRequest
import com.example.swith.domain.entity.LoginResponse
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class SnsLoginActivity : AppCompatActivity(), View.OnClickListener,
    Observer<LoginResponse> {
    lateinit var binding: ActivitySnsLoginBinding

    private var mLoginViewModel: LoginViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sns_login)

        initData()
        initView()
    }

    private fun initView() {
        binding.apply {
            clickListener = this@SnsLoginActivity
            mLoginViewModel =
                ViewModelProvider(this@SnsLoginActivity, LoginViewModel.Factory()).get(
                    LoginViewModel::class.java
                ).apply {
                    loginViewModel = this
                    getCurrentLogin().observe(this@SnsLoginActivity, this@SnsLoginActivity)
                }
        }
    }

    private fun initData() {

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ll_kakao -> {
                Toast.makeText(this, "kakao", Toast.LENGTH_SHORT).show()
                hasKakaoToken()
            }
        }
    }


    private fun hasKakaoToken() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.e("doori", "토큰 정보 보기 실패,error =  $error")
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                        kakaoLogin()
                    } else {
                        //기타 에러
                        cautionDialog(error.message ?: "hasKakaoToken()오류")
                    }
                } else {
                    Log.i(
                        "doori", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo?.id}" +
                                "\n만료시간: ${tokenInfo?.expiresIn} 초"
                    )
                    getUserInfo()
                }
            }
        } else {
            //로그인 필요
            kakaoLogin()
        }
    }

    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("doori", "카카오계정으로 로그인 실패 , error =  $error")
                cautionDialog(error.message ?: "kakaoLogin()오류")
            } else if (token != null) {
                Log.i("doori", "카카오계정으로 로그인 성공 ${token.accessToken}")
                getUserInfo()
            }
        }


        // 카카오톡으로 로그인
        UserApiClient.instance.apply {
            //카톡이 깔려져있는지 검사
            if (isKakaoTalkLoginAvailable(this@SnsLoginActivity)) {
                loginWithKakaoTalk(this@SnsLoginActivity) { token, error ->
                    if (error != null) {
                        Log.e("doori", "카카오톡으로 로그인 실패", error)

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        loginWithKakaoAccount(this@SnsLoginActivity, callback = callback)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            cautionDialog(error.msg)
                            return@loginWithKakaoTalk
                        }
                    } else if (token != null) {
                        Log.e("doori", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        getUserInfo()
                    }
                }

            } else {
                loginWithKakaoAccount(this@SnsLoginActivity, callback = callback)
            }
        }
    }

    private fun getUserInfo() {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("doori", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    "doori", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진url: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )

                mLoginViewModel?.requestCurrentLogin(
                    LoginRequest(
                        user.kakaoAccount?.email ?: "null",
                        user.kakaoAccount?.profile?.nickname ?: "null",
                        user.kakaoAccount?.profile?.thumbnailImageUrl ?: "no",
                        SharedPrefManager(this@SnsLoginActivity).getFcmToken() ?: "null"
                    )
                )
                Log.i(
                    "doori", "${
                        LoginRequest(
                            user.kakaoAccount?.email ?: "null",
                            user.kakaoAccount?.profile?.nickname ?: "null",
                            user.kakaoAccount?.profile?.thumbnailImageUrl ?: "no",
                            SharedPrefManager(this@SnsLoginActivity).getFcmToken() ?: "null"
                        )
                    }"
                )
                setShowDimmed(true)
            }
        }
    }

    private fun goProfileModifyPage() {
        Intent(this@SnsLoginActivity, ProfileModifyActivity::class.java).run {
            startActivity(this)
            finishAffinity()
        }
    }

    private fun goMainPage() {
        Intent(this@SnsLoginActivity, MainActivity::class.java).run {
            startActivity(this)
            finishAffinity()
        }
    }

    private fun cautionDialog(errorMsg: String) {
        val builder = AlertDialog.Builder(this@SnsLoginActivity)
            .setTitle("카카오톡오류")
            .setMessage(errorMsg)
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(this@SnsLoginActivity, "확인", Toast.LENGTH_SHORT)
                        .show()
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(this@SnsLoginActivity, "취소", Toast.LENGTH_SHORT)
                        .show()
                })
        builder.show()
    }

    override fun onChanged(loginResponse: LoginResponse?) {
        Log.e("doori", "onChanged = $loginResponse")
        setShowDimmed(false)
        //가입되어있으면 메인페이지,아니면 프로필관리
        loginResponse?.result!!.isSignUp.apply {
            mLoginViewModel?.getCurrentLogin()?.value?.result?.apply {
                Log.e("doori", "goProfile = $this")
                SharedPrefManager(this@SnsLoginActivity).setLoginData(userIdx, accessToken)
            }
            Log.e(
                "doori",
                "SharedPrefManage = ${SharedPrefManager(this@SnsLoginActivity).getLoginData()}"
            )
            if (this) {
                goProfileModifyPage()
            } else {
                goMainPage()
            }
        }
    }

    private fun setShowDimmed(isLoading: Boolean) {
        mLoginViewModel?.apply {
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoginViewModel?.run {
            getCurrentLogin().removeObserver(this@SnsLoginActivity)
        }
    }
}