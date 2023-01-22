package com.example.swith.ui.manage

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.ActivityManageAttendBinding
import com.example.swith.domain.entity.AttendList
import com.example.swith.domain.entity.GetAttendanceInfo
import com.example.swith.ui.adapter.ManageAttendRVAdapter
import com.example.swith.ui.dialog.BottomSheet
import com.example.swith.ui.dialog.CustomAlertDialog
import com.example.swith.utils.error.ScreenState
import com.example.swith.viewmodel.AttendUpdateViewModel

class ManageAttendActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel: AttendUpdateViewModel by viewModels()
    private val groupIdx by lazy {
        intent.getLongExtra("groupId", 0)
    }
    private lateinit var binding: ActivityManageAttendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_attend)
        binding.clickListener = this
        binding.btnAttendUpdate.setOnClickListener { updateAttend() }
        observeViewModel()
        setViewVisibility(true)
        viewModel.loadData(groupIdx)
    }

    private fun observeViewModel() {
        viewModel.attendLiveData.observe(this, Observer {
            initSpinnerView(it)
        })
        viewModel.mutableScreenState.observe(this, Observer {
            if (it == ScreenState.RENDER) setViewVisibility(false) else setViewVisibility(true)
        })
        viewModel.mutableErrorType.observe(this, Observer {
            CustomAlertDialog("출석 정보 수정 실패", it.toString()).show(
                supportFragmentManager,
                "출석 정보 수정 실패"
            )
        })
        viewModel.mutableErrorMessage.observe(this, Observer {
            CustomAlertDialog("출석 정보 수정 실패", it.toString()).show(
                supportFragmentManager,
                "출석 정보 수정 실패2"
            )
        })
        viewModel.updateAttendLiveEvent.observe(this, Observer {
            CustomAlertDialog("출석 정보 수정 성공", "출석 정보 수정이 완료되었습니다.").show(
                supportFragmentManager,
                "출석 정보 수정 성공"
            )
        })
    }

    private fun setViewVisibility(beforeLoad: Boolean) {
        with(binding) {
            manageAttendCircularIndicator.visibility =
                if (beforeLoad) View.VISIBLE else View.INVISIBLE
            layoutManageAttendSpinner.visibility = if (beforeLoad) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun emptyLayoutVisible(
        empty: Boolean,
        attendList: List<GetAttendanceInfo>,
    ) {
        with(binding) {
            rvManageAttend.visibility = if (empty) View.INVISIBLE else View.VISIBLE
            tvNoAttend.visibility = if (empty) View.VISIBLE else View.INVISIBLE
            if (empty)
                btnAttendUpdate.visibility = View.INVISIBLE
            else {
                btnAttendUpdate.visibility = View.INVISIBLE
                attendList.forEach { if (it.status != 0) btnAttendUpdate.visibility = View.VISIBLE }
            }
        }
    }

    private fun initSpinnerView(attendList: AttendList) {
        val stringList = ArrayList<String>()
        attendList.attend?.let {
            it.forEach { a -> stringList.add("${a.sessionNum}회차") }
        }
        stringList.add("회차 선택")

        binding.rvManageAttend.apply {
            adapter = ManageAttendRVAdapter()
            layoutManager =
                LinearLayoutManager(this@ManageAttendActivity, LinearLayoutManager.VERTICAL, false)
        }

        binding.spinnerManageAttend.apply {
            adapter = object : ArrayAdapter<String>(
                this@ManageAttendActivity,
                R.layout.item_manage_attend_spinner
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val textView = super.getView(position, convertView, parent) as TextView
                    // 마지막 포지션의 텍스트를 hint로 사용
                    if (position == count) {
                        binding.btnAttendUpdate.visibility = View.INVISIBLE
                    }
                    textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    return textView
                }

                override fun getCount(): Int {
                    return super.getCount() - 1
                }
            }.apply {
                addAll(stringList)
            }
            setSelection(adapter.count)
            // droplist를 spinner와 간격을 두고 나오도록
            dropDownVerticalOffset = dipToPixels(41f).toInt()
            // 스피너 선택시 변하는 로직
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position != adapter.count) (binding.rvManageAttend.adapter as ManageAttendRVAdapter).setData(
                        attendList.attend[position].getAttendanceInfos.apply {
                            emptyLayoutVisible(this.isNullOrEmpty(), this)
                            binding.tvManageAttendGuide.visibility = View.INVISIBLE
                        })
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }

    private fun updateAttend() {
        BottomSheet(
            "출석 정보 변경",
            null,
            resources.getString(R.string.bottom_manage_attend),
            "변경"
        ).apply {
            setCustomListener(object : BottomSheet.customClickListener {
                override fun onCheckClick() {
                    dismiss()
                    viewModel.updateData((binding.rvManageAttend.adapter as ManageAttendRVAdapter).getData())
                }
            })
        }.show(supportFragmentManager, "출석 정보 변경")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_basic_toolbar_back -> finish()
        }
    }

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }
}