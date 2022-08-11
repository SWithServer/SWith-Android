package com.example.swith.ui.study.announce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.data.Announce
import com.example.swith.data.AnnounceCreate
import com.example.swith.databinding.ActivityAnnounceBinding
import com.example.swith.ui.adapter.AnnounceRVAdapter
import com.example.swith.ui.dialog.CustomAlertDialog
import com.example.swith.ui.dialog.CustomAnnounceCreateDialog
import com.example.swith.ui.dialog.CustomConfirmDialog
import com.example.swith.utils.ToolBarManager
import com.example.swith.utils.error.ErrorType
import com.example.swith.utils.error.ScreenState
import com.example.swith.viewmodel.AnnounceViewModel

class AnnounceActivity : AppCompatActivity(){
    private val viewModel : AnnounceViewModel by viewModels()
    // 매니저
    private val isManager: Boolean by lazy {
        if (intent.hasExtra("manager"))
            intent.getBooleanExtra("manager", false)
        else false
    }
    // group Idx
    private val groupIdx: Int by lazy {
        if (intent.hasExtra("groupIdx"))
            intent.getIntExtra("groupIdx", 0)
        else 0
    }

    lateinit var binding: ActivityAnnounceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_announce)

        ToolBarManager(this).initToolBar(binding.tbAnnounce,
            titleVisible = false,
            backVisible = true
        )

        initView()
        observeViewModel()
    }

    private fun initView() {
        with(binding){
            rvAnnounce.adapter = AnnounceRVAdapter(isManager).apply {
                setListener(object: AnnounceRVAdapter.CustomListener{
                    override fun onDelete(announce: Announce) {
                        CustomConfirmDialog("공지사항 삭제", "해당 공지사항을 삭제하시겠습니까?\n내용 : ${announce.announcementContent}").apply {
                            setCustomListener(object: CustomConfirmDialog.CustomListener{
                                override fun onConfirm() {
                                    dismiss()
                                    viewModel.deleteAnnounce(announce.announcementIdx)
                                }
                            })
                        }.show(supportFragmentManager, "공지사항 삭제")
                    }

                    override fun onItemClick(announce: Announce) {
                        CustomAlertDialog("공지사항", "${announce.announcementContent}").apply {
                            show(supportFragmentManager, "공지사항")
                        }
                    }
                })
            }
            rvAnnounce.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            ivAnnounceCreate.apply {
                visibility = if (isManager) View.VISIBLE else View.INVISIBLE
                setOnClickListener { CustomAnnounceCreateDialog().apply {
                    setCustomListener(object: CustomAnnounceCreateDialog.CustomListener{
                        override fun onConfirm(content: String) {
                            dismiss()
                            CustomConfirmDialog("공지사항 생성", "해당 공지사항을 생성합니다.").apply {
                                setCustomListener(object: CustomConfirmDialog.CustomListener{
                                    override fun onConfirm() {
                                        viewModel.createAnnounce(AnnounceCreate(content, groupIdx))
                                        dismiss()
                                    }
                                })
                            }.show(supportFragmentManager, "공지사항 생성")
                        }
                    })
                }.show(supportFragmentManager, "announceCreate") }
            }
        }
    }


    private fun observeViewModel() {
        setVisibility(true)
        viewModel.loadData(groupIdx)

        viewModel.announceLiveData.observe(this, Observer {
            (binding.rvAnnounce.adapter as AnnounceRVAdapter).setData(it.announces)
        })

        viewModel.mutableScreenState.observe(this, Observer{
            if(it == ScreenState.RENDER) setVisibility(false)
        })

        viewModel.deleteLiveEvent.observe(this, Observer {
            CustomAlertDialog("삭제 완료", "공지사항이 삭제 되었습니다.").show(supportFragmentManager, "삭제 완료")
            reloadData()
        })

        viewModel.mutableErrorMessage.observe(this, Observer {
            CustomAlertDialog("삭제 오류", it).show(supportFragmentManager, "삭제 오류")
        })

        viewModel.mutableErrorType.observe(this, Observer {
            Toast.makeText(applicationContext, "$it 오류", Toast.LENGTH_SHORT).show()
        })

        viewModel.createLiveEvent.observe(this, Observer {
            CustomAlertDialog("생성 완료", "공지사항이 생성 되었습니다.").show(supportFragmentManager, "생성 완료")
            reloadData()
        })
    }

    private fun setVisibility(beforeLoad: Boolean){
        with(binding){
            svAnnounce.visibility = if(beforeLoad) View.INVISIBLE else View.VISIBLE
            announceCircularIndicator.visibility = if (beforeLoad) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun reloadData(){
        // 새로고침<리로드> (로직 검토 필요)
        setVisibility(true)
        viewModel.loadData(groupIdx)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}