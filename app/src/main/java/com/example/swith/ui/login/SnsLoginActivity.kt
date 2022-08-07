package com.example.swith.ui.login

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivitySnsLoginBinding
import com.example.swith.ui.profile.ProfileModifyActivity
import com.example.swith.utils.SharedPrefManager
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class SnsLoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivitySnsLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sns_login)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@SnsLoginActivity


    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ll_kakao -> {
                Toast.makeText(this, "kakao", Toast.LENGTH_SHORT).show()
                hasKakaoToken()
            }
//            R.id.btn_logout -> {
//                // 연결 끊기
//                UserApiClient.instance.unlink { error ->
//                    if (error != null) {
//                        Log.e("doori", "연결 끊기 실패", error)
//                        cautionDialog(error.message?:"연결끊기오류")
//                    } else {
//                        Log.i("doori", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
//                        SharedPrefManager(this@SnsLoginActivity).deleteLoginData()
//                    }
//                }
//            }

        }
    }

    private fun hasKakaoToken() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.e("doori", "토큰 정보 보기 실패", error)
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                        kakaoLogin()
                    } else {
                        //기타 에러
                        cautionDialog(error.message?:"hasKakaoToken()오류")
                    }
                } else {
                    //TODO 토큰 유효성 체크 && 서버에 사용자 정보 넘겨주기
                    Log.i(
                        "doori", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo?.id}" +
                                "\n만료시간: ${tokenInfo?.expiresIn} 초"
                    )
                    getUserInfo()
                    goProfileModifyPage()
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
                Log.e("doori", "카카오계정으로 로그인 실패", error)
                cautionDialog(error.message?:"kakaoLogin()오류")
            } else if (token != null) {
                //TODO 서버에 사용자 정보 넘겨주기
                Log.i("doori", "카카오계정으로 로그인 성공 ${token.accessToken}")
                getUserInfo()
                goProfileModifyPage()
            }
        }


        // 카카오톡으로 로그인
        UserApiClient.instance.apply {
            //카톡이 깔려져있는지 검사
            if (isKakaoTalkLoginAvailable(this@SnsLoginActivity)) {
                loginWithKakaoTalk(this@SnsLoginActivity) { token, error ->
                    if (error != null) {
                        Log.e("doori", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            cautionDialog(error.msg)
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        loginWithKakaoAccount(this@SnsLoginActivity, callback = callback)
                    } else if (token != null) {
                        //TODO 서버에 사용자 정보 넘겨주기
                        Log.e("doori", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        getUserInfo()
                        goProfileModifyPage()
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
            }
        }
    }

    private fun goProfileModifyPage(){
        //TODO 자동로그인 테스트를 위한 임시데이터
        SharedPrefManager(this@SnsLoginActivity).setLoginData("1234","asdasd")
        Intent(this@SnsLoginActivity, ProfileModifyActivity::class.java).run {
            startActivity(this)
            finishAffinity()
        }
    }

    private fun cautionDialog(errorMsg: String){
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
}