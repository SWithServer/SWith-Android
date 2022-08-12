package com.example.swith.ui.study

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.databinding.ActivityStudyBinding
import com.example.swith.ui.home.HomeFragment
import com.example.swith.ui.manage.ManageActivity
import com.example.swith.ui.study.round.RoundFragment
import com.example.swith.ui.study.round.RoundTabFragment
import com.example.swith.utils.ToolBarManager
import com.example.swith.viewmodel.RoundViewModel

class StudyActivity : AppCompatActivity() {
    private var groupId = 0
    lateinit var binding: ActivityStudyBinding
    // BottomNav 중 회차(연필)을 누르는 경우 가장 최근에 닫은 회차 프래그먼트로 이동
    private var prevRoundFragment: String = "fragment"
    private val viewModel: RoundViewModel by viewModels()

    // 툴바 화면에 관련된 것
    private lateinit var mainMenu: Menu
    private var isManager = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_study)

        initData()

        ToolBarManager(this).initToolBar(binding.studyToolbar,
            titleVisible = false,
            backVisible = true
        )
        initBottomNavigation()
    }

    private fun initData(){
        intent.hasExtra("group")?.let { groupId = intent.getIntExtra("group", 0) }
        viewModel.groupIdx = groupId
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        mainMenu = menu
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
                startActivity(Intent(this, ManageActivity::class.java).apply {
                    putExtra("groupId", groupId)
                })
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

    fun getToolBarMenu(isManager: Boolean) : Menu{
        this.isManager = isManager
        return mainMenu
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
                                prevRoundFragment = fragments[0].toString()
                                if (fragments[0] is RoundTabFragment)
                                    replace(R.id.study_frm, RoundTabFragment())
                                        .commitAllowingStateLoss()
                                else replace(R.id.study_frm, RoundFragment())
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

    override fun onBackPressed() {
        with(supportFragmentManager) {
            // RoundTabFragment 위에 있으면 이전 키 누르면 RoundFragment로 돌아오기, 아닌 경우 activity 종료
            if (fragments[0] is RoundTabFragment) {
                beginTransaction().replace(R.id.study_frm, RoundFragment())
                    .commitAllowingStateLoss()
            } else {
                finish()
            }
        }
    }

}