package com.example.swith.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.swith.R
import com.example.swith.data.StudyFindData
import com.example.swith.databinding.ActivityMainBinding
import com.example.swith.ui.home.HomeFragment
import com.example.swith.ui.profile.ProfileFragment
import com.example.swith.ui.study.find.StudyFindDetailFragment
import com.example.swith.ui.study.find.StudyFindFragment
import com.example.swith.utils.ToolBarManager


class MainActivity : AppCompatActivity() {
    //뒤로가기 눌렀던 시간 저장
    private var backKeyPressedTime: Long = 0
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomNavigation()

        initData()
        initView()


        ToolBarManager(this).initToolBar(binding.mainToolbar,
            titleVisible = false,
            backVisible = false
        )
    }

    private fun initView() {
        intent.getStringExtra("profile")?.let {
            Log.e("doori","main tag = $it")
            if(it==ProfileFragment.TAG){
                //특정 프레그먼트로 이동
                binding.mainBnv.selectedItemId = R.id.bottom_nav_profile
            }
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_notification ->{
                Toast.makeText(applicationContext, "알림 버튼 Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.toolbar_setting -> {
                Toast.makeText(applicationContext, "설정 버튼 Clicked", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
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

//    override fun onBackPressed() {
//        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
//            // 뒤로가기 두 번 누르면 종료
//            finish()
//        } else{
//            backKeyPressedTime = System.currentTimeMillis()
//            Toast.makeText(this, "뒤로 가기 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
//        }
//    }

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
    fun goSearchPage() {
        Log.e("summer","goSearchPage")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm,StudyFindFragment())
            .commitAllowingStateLoss()
    }
    fun goDeatailPage(groupIdx:Int,fragment: Fragment) {
        Log.e("summer","goDetailPage")
        var fragment_ = fragment
        var bundle = Bundle()
        fragment.arguments=bundle
        bundle.putInt("groupIdx",groupIdx)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm,fragment_)
            .addToBackStack(null)
            .commit()
    }
}