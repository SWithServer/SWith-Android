package com.example.swith.ui.study.announce

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.R
import com.example.data.databinding.ActivityAnnounceBinding
import com.example.data.ui.adapter.AnnounceRVAdapter
import com.example.data.ui.dialog.CustomAlertDialog
import com.example.data.ui.dialog.CustomAnnounceCreateDialog
import com.example.data.ui.dialog.CustomAnnounceModifyDialog
import com.example.data.ui.dialog.CustomConfirmDialog
import com.example.data.utils.SwipeController
import com.example.data.utils.error.ScreenState
import com.example.data.viewmodel.AnnounceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnounceActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel: AnnounceViewModel by viewModels()

    // 매니저
    private val isManager: Boolean by lazy {
        if (intent.hasExtra("manager"))
            intent.getBooleanExtra("manager", false)
        else false
    }

    // group Idx
    private val groupIdx: Long by lazy {
        if (intent.hasExtra("groupIdx"))
            intent.getLongExtra("groupIdx", 0)
        else 0
    }

    lateinit var binding: ActivityAnnounceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_announce)

        initView()
        observeViewModel()
    }

    private fun initView() {
        with(binding) {
            clickListener = this@AnnounceActivity
            rvAnnounce.apply {
                adapter = AnnounceRVAdapter().apply {
                    setListener(object : AnnounceRVAdapter.CustomListener {
                        override fun onDelete(announce: com.example.data.entity.Announce) {
                            CustomConfirmDialog(
                                "공지사항 삭제",
                                "해당 공지사항을 삭제하시겠습니까?\n내용 : ${announce.announcementContent}"
                            ).apply {
                                setCustomListener(object : CustomConfirmDialog.CustomListener {
                                    override fun onConfirm() {
                                        dismiss()
                                        viewModel.deleteAnnounce(announce.announcementIdx)
                                    }
                                })
                            }.show(supportFragmentManager, "공지사항 삭제")
                        }

                        override fun onItemClick(announce: com.example.data.entity.Announce) {
                            // 매니저인 경우 수정 화면 뜨도록
                            if (isManager) {
                                CustomAnnounceModifyDialog(announce.announcementContent).apply {
                                    setCustomListener(object :
                                        CustomAnnounceModifyDialog.CustomListener {
                                        override fun onConfirm(content: String) {
                                            dismiss()
                                            CustomConfirmDialog(
                                                "공지사항 수정",
                                                "공지사항 내용을 수정합니다."
                                            ).apply {
                                                setCustomListener(object :
                                                    CustomConfirmDialog.CustomListener {
                                                    override fun onConfirm() {
                                                        dismiss()
                                                        viewModel.updateAnnounce(
                                                            com.example.data.entity.AnnounceModify(
                                                                content,
                                                                announce.announcementIdx
                                                            )
                                                        )
                                                    }
                                                })
                                            }.show(supportFragmentManager, "수정 확인")
                                        }
                                    })
                                }.show(supportFragmentManager, "공지사항 수정")
                            } else {
                                // 매니저가 아닌 경우 내용 팝업 다이얼로그
                                CustomAlertDialog("공지사항", "${announce.announcementContent}").apply {
                                    show(supportFragmentManager, "공지사항")
                                }
                            }
                        }
                    })
                }
                layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                if (isManager) {
                    val itemTouchHelper =
                        ItemTouchHelper(SwipeController(this.adapter as AnnounceRVAdapter))
                    itemTouchHelper.attachToRecyclerView(this)
                }
            }
            ivAnnounceCreate.apply {
                visibility = if (isManager) View.VISIBLE else View.INVISIBLE
                setOnClickListener {
                    CustomAnnounceCreateDialog().apply {
                        setCustomListener(object : CustomAnnounceCreateDialog.CustomListener {
                            override fun onConfirm(content: String) {
                                dismiss()
                                CustomConfirmDialog("공지사항 생성", "해당 공지사항을 생성합니다.").apply {
                                    setCustomListener(object : CustomConfirmDialog.CustomListener {
                                        override fun onConfirm() {
                                            viewModel.createAnnounce(
                                                com.example.data.entity.AnnounceCreate(
                                                    content,
                                                    groupIdx
                                                )
                                            )
                                            dismiss()
                                        }
                                    })
                                }.show(supportFragmentManager, "공지사항 생성")
                            }
                        })
                    }.show(supportFragmentManager, "announceCreate")
                }
            }
        }
    }


    private fun observeViewModel() {
        setVisibility(true)
        viewModel.loadData(groupIdx)

        viewModel.announceLiveData.observe(this, Observer {
            (binding.rvAnnounce.adapter as AnnounceRVAdapter).setData(it.announces)
        })

        viewModel.mutableScreenState.observe(this, Observer {
            if (it == ScreenState.RENDER) setVisibility(false)
        })

        viewModel.mutableErrorType.observe(this, Observer {
            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
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

        viewModel.updateLiveEvent.observe(this, Observer {
            CustomAlertDialog("수정 완료", "공지사항이 수정 되었습니다.").show(supportFragmentManager, "수정 완료")
            reloadData()
        })

    }

    private fun setVisibility(beforeLoad: Boolean) {
        with(binding) {
            svAnnounce.visibility = if (beforeLoad) View.INVISIBLE else View.VISIBLE
            announceCircularIndicator.visibility = if (beforeLoad) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun reloadData() {
        // 새로고침<리로드> (로직 검토 필요)
        setVisibility(true)
        viewModel.loadData(groupIdx)
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_basic_toolbar_back -> {
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}