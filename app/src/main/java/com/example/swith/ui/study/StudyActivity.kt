package com.example.swith.ui.study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import com.example.swith.R
import com.example.swith.databinding.ActivityStudyBinding
import com.example.swith.ui.home.HomeFragment
import com.example.swith.ui.study.round.RoundFragment
import com.example.swith.ui.study.round.RoundTabFragment
import com.example.swith.utils.ToolBarManager
import com.example.swith.viewmodel.RoundViewModel

class StudyActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudyBinding
    // BottomNav 중 회차(연필)을 누르는 경우 가장 최근에 닫은 회차 프래그먼트로 이동
    lateinit var prevRoundFragment: String
    private val isManager = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_study)

        ToolBarManager(this).initToolBar(binding.studyToolbar,
            titleVisible = false,
            backVisible = true
        )
        initBottomNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        if (!isManager)
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
            android.R.id.home -> {
                with(supportFragmentManager){
                    if (fragments[0] is RoundFragment || fragments[0] is CalendarFragment)
                        finish()
                    else {
                        beginTransaction()
                            .replace(R.id.study_frm, RoundFragment())
                            .commitAllowingStateLoss()
                    }
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.study_frm, RoundFragment() )
            .commitAllowingStateLoss()
        binding.studyBottomNav.setOnItemSelectedListener{ item ->
            with(supportFragmentManager) {
                when (item.itemId) {
                    R.id.bottom_nav_round -> {
                        // BottomNav 중 회차(연필)을 누르는 경우 가장 최근에 닫은 회차 프래그먼트로 이동
                        beginTransaction().apply {
                            if (prevRoundFragment.contains("RoundTabFragment"))
                                replace(R.id.study_frm, RoundTabFragment())
                                    .commitAllowingStateLoss()
                            else{
                                replace(R.id.study_frm, RoundFragment())
                                    .commitAllowingStateLoss()
                            }
                        }
                        return@setOnItemSelectedListener true
                    }
                    R.id.bottom_nav_calendar -> {
                        if (fragments[0] is RoundTabFragment || fragments[0] is RoundFragment) prevRoundFragment = fragments[0].toString()
                        beginTransaction()
                            .replace(R.id.study_frm, CalendarFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                    R.id.bottom_nav_statistics -> {
                        if (fragments[0] is RoundTabFragment || fragments[0] is RoundFragment) prevRoundFragment = fragments[0].toString()
                        // Todo
                    }
                }
            }
            false
        }
    }

}