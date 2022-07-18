package com.example.swith.ui.study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityStudyBinding
import com.example.swith.utils.ToolBarManager

class StudyActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudyBinding
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
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.study_frm, RoundFragment() )
            .commitAllowingStateLoss()
        binding.studyBottomNav.setOnItemSelectedListener{ item ->
            when (item.itemId){
                R.id.bottom_nav_round -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.study_frm, RoundFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_calendar -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.study_frm, CalendarFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_statistics -> {
                    // Todo
                }
            }
            false
        }
    }

}