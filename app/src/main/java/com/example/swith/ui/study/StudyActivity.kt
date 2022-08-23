package com.example.swith.ui.study

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.example.swith.R
import com.example.swith.databinding.ActivityStudyBinding
import com.example.swith.ui.manage.ManageActivity
import com.example.swith.ui.study.announce.AnnounceActivity
import com.example.swith.ui.study.round.RoundAttendFragment
import com.example.swith.ui.study.round.RoundFragment
import com.example.swith.ui.study.round.RoundMemoFragment
import com.example.swith.ui.study.round.RoundTabFragment
import com.example.swith.viewmodel.RoundViewModel

class StudyActivity : AppCompatActivity(), View.OnClickListener {
    private var groupId = 0
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    lateinit var binding: ActivityStudyBinding
    // BottomNav 중 회차(연필)을 누르는 경우 가장 최근에 닫은 회차 프래그먼트로 이동
    private var prevRoundFragment: String = "fragment"
    private val viewModel: RoundViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_study)

        initData()
        initBottomNavigation()
    }

    private fun initData(){
        intent.hasExtra("group")?.let { groupId = intent.getIntExtra("group", 0) }
        binding.clickListener = this
        viewModel.groupIdx = groupId

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if(it.resultCode == Activity.RESULT_OK){
                viewModel.loadData()
            }
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.ib_study_notice ->{
                Toast.makeText(applicationContext, "알림 버튼 Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.ib_study_setting -> {
                activityResultLauncher.launch(Intent(this, ManageActivity::class.java).apply {
                    putExtra("groupId", groupId)
                })
            }
            R.id.ib_study_back -> {
                with(supportFragmentManager){
                    if (fragments[0] is RoundTabFragment || (fragments.size > 1 && fragments[1] is RoundTabFragment))
                        beginTransaction().replace(R.id.study_frm, RoundFragment())
                            .commitAllowingStateLoss()
                    else {
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        }
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
                                prevRoundFragment = if (fragments[0] is RoundTabFragment || (fragments.size > 2 && fragments[1] is RoundTabFragment)) {
                                    replace(R.id.study_frm, RoundTabFragment())
                                        .commitAllowingStateLoss()
                                    "RoundTabFragment"
                                } else {
                                    replace(R.id.study_frm, RoundFragment())
                                        .commitAllowingStateLoss()
                                    "RoundFragment"
                                }
                            }
                        }
                        return@setOnItemSelectedListener true
                    }
                    R.id.bottom_nav_calendar -> {
                        if (fragments[0] is RoundTabFragment || fragments[0] is RoundFragment ) prevRoundFragment = fragments[0].toString()
                        else if(fragments.size > 1 && (fragments[1] is RoundTabFragment || fragments[1] is RoundFragment)) prevRoundFragment = fragments[1].toString()
                        beginTransaction()
                            .replace(R.id.study_frm, CalendarFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                    else -> {
                        if (fragments[0] is RoundTabFragment || fragments[0] is RoundFragment ) prevRoundFragment = fragments[0].toString()
                        else if(fragments.size > 1 && (fragments[1] is RoundTabFragment || fragments[1] is RoundFragment)) prevRoundFragment = fragments[1].toString()
                        beginTransaction()
                            .replace(R.id.study_frm, StatsFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener  true
                    }
                }
            }
            false
        }
    }

    override fun onBackPressed() {
        with(supportFragmentManager) {
            // RoundTabFragment 위에 있으면 이전 키 누르면 RoundFragment로 돌아오기, 아닌 경우 activity 종료
            if (fragments[0] is RoundTabFragment || (fragments.size > 1 && fragments[1] is RoundTabFragment))
                beginTransaction().replace(R.id.study_frm, RoundFragment())
                    .commitAllowingStateLoss()
            else {
                setResult(RESULT_OK)
                finish()
            }
        }
    }
    fun setVisibleBar(isManager: Boolean){
        binding.studyToolbar.apply {
            studyToolbar.visibility = View.VISIBLE
            ibStudySetting.visibility = if (isManager) View.VISIBLE else View.GONE
        }
    }
}