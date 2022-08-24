package com.example.swith.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.swith.R
import com.example.swith.databinding.ActivityMainBinding
import com.example.swith.ui.home.HomeFragment
import com.example.swith.ui.login.LoginActivity
import com.example.swith.ui.profile.ProfileFragment
import com.example.swith.ui.resume.ResumeFragment
import com.example.swith.ui.study.find.StudyFindFragment


class MainActivity : AppCompatActivity(), View.OnClickListener {
    //뒤로가기 눌렀던 시간 저장
    private var backKeyPressedTime: Long = 0
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomNavigation()

        initData()
        initView()


//        ToolBarManager(this).initToolBar(binding.mainToolbar,
//            titleVisible = false,
//            backVisible = false
//        )
    }

    private fun initView() {
        intent.getStringExtra("profile")?.let {
            Log.e("doori","main tag = $it")
            if(it==ProfileFragment.TAG){
                //특정 프레그먼트로 이동
                binding.mainBnv.selectedItemId = R.id.bottom_nav_profile
            }
        }
        binding.clickListener=this@MainActivity
    }

    private fun initData() {
    }

    private fun initBottomNavigation(){
        goMainPage()
        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.bottom_nav_home ->{
                    goMainPage()
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_search -> {
                    // TODO: goSearchPage()
                    goSearchPage()
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_profile -> {
                    goProfilePage()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        menu.findItem(R.id.toolbar_setting).isVisible = false
        return true
    }


    interface onBackPressedListener{
        fun onBackPressed()
    }

    override fun onBackPressed(){
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList)
        {
            if (fragment is onBackPressedListener){
                (fragment as onBackPressedListener).onBackPressed()
                return
            }
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            // 뒤로가기 두 번 누르면 종료
            finish()
        } else{
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로 가기 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goProfilePage() {
        Log.e("doori","goProfilePage")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, ProfileFragment())
            .commitAllowingStateLoss()
    }
    private fun goMainPage() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }
    fun goResumePage(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, ResumeFragment())
            .commitAllowingStateLoss()
    }

    fun goSearchPage() {
        Log.e("summer","goSearchPage")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm,StudyFindFragment())
            .commitAllowingStateLoss()
    }

    fun goDeatailPage(groupIdx:Long,applicationMethod:Int,fragment: Fragment) {
        Log.e("summer","goDetailPage")
        var fragment_ = fragment
        var bundle = Bundle()
        fragment.arguments=bundle
        bundle.putLong("groupIdx",groupIdx)
        bundle.putInt("applicationMethod",applicationMethod)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm,fragment_)
            .addToBackStack(null)
            .commit()
    }
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_back ->{
                onBackPressed()
            }
            R.id.ib_notice->{
                Toast.makeText(this@MainActivity,"notice",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setVisibleBar(backButton: Boolean,noticeButton: Boolean,title:String,midTitle:String){
        binding.mainToolbar.apply {
            if(backButton){
                ibBack.visibility=VISIBLE
            }else {
                ibBack.visibility= INVISIBLE
            }
            if(noticeButton){
                ibNotice.visibility= VISIBLE
            }else{
                ibNotice.visibility= INVISIBLE
            }
            tvTitle.text=title
            tvMidTitle.text=midTitle
        }
    }

    fun loginPage(){
        Intent(this@MainActivity,LoginActivity::class.java).run {
            startActivity(this)
            finishAffinity()
        }
    }
}