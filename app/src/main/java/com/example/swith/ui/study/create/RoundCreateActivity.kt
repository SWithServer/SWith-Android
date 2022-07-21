package com.example.swith.ui.study.create

import android.app.DatePickerDialog
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.akexorcist.snaptimepicker.TimeRange
import com.akexorcist.snaptimepicker.TimeValue
import com.example.swith.R
import com.example.swith.databinding.ActivityRoundCreateBinding
import com.example.swith.utils.ToolBarManager
import java.time.LocalDateTime
import java.util.*


class RoundCreateActivity : AppCompatActivity() {
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    lateinit var binding: ActivityRoundCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_round_create)

        ToolBarManager(this).initToolBar(binding.toolbarCreate,
            titleVisible = false,
            backVisible = true
        )
        initCheckBox()
        initListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initCheckBox(){
        with(binding){
            etCreatePlace.isEnabled = false
            cbCreateOnline.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked) {
                    cbCreateOffline.isChecked = false
                    etCreatePlace.apply {
                        isEnabled = false
                        setText("온라인")
                    }
                } else{
                    etCreatePlace.apply{
                        if (cbCreateOffline.isChecked) isEnabled = true
                        hint = "장소(텍스트 입력)"
                        setText("")
                    }
                }
            }
            cbCreateOffline.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked) {
                    cbCreateOnline.isChecked = false
                    etCreatePlace.apply {
                        isEnabled = true
                    }
                }
                else {
                    etCreatePlace.apply{
                        isEnabled = false
                        setText("")
                    }
                }
            }
        }
    }

    private fun initListener(){
        // 시간 설정 후에 추가해줘야 함
        with(binding){
            btnCreateStartDate.setOnClickListener {
                initDateTimePicker(true)
            }
            btnCreateEndDate.setOnClickListener {
                initDateTimePicker(false)
            }
            etCreateDetail.doAfterTextChanged {
                // 세 개의 뷰 텍스트가 전부 empty가 아닐 때 -> 버튼 활성화
                setAddButton()
            }
            etCreatePlace.doAfterTextChanged {
                setAddButton()
            }
            btnCreateAdd.setOnClickListener {
                // Todo : ViewModel 에서 post
            }
        }
    }

    private fun initDateTimePicker(isStart: Boolean){
        with(binding) {
            DatePickerDialog(this@RoundCreateActivity, { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
            }, year, month, day).apply {
                // 지금보다 이전 날짜(과거 날짜) 비활성화
                // start date, end date 나눠서 구현해야 할듯
                setOnDateSetListener { _, dateYear, monthOfYear, dayOfMonth ->
                    SnapTimePickerDialog.Builder().apply {
                        if (isStart) setTitle(R.string.create_round_start_time)
                        else setTitle(R.string.create_round_end_time)
                        setTitleColor(R.color.white)
                        setThemeColor(R.color.teal_700)
                        setPositiveButtonColor(R.color.black)
                        setNegativeButtonColor(R.color.black)
                        setButtonTextAllCaps(false)
                        with(LocalDateTime.now()){
                            setPreselectedTime(TimeValue(hour, minute))
                            if (year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth)
                                setSelectableTimeRange(TimeRange(TimeValue(hour, minute + 1), TimeValue(23, 59)))
                        }
                    }.build().apply {
                        setListener{ hour, minute ->
                            if (isStart) {
                                btnCreateStartDate.text =
                                    String.format("시작 : ${dateYear}.${monthOfYear+1}.${dayOfMonth} ${hour}:%02d", minute)
                            } else {
                                btnCreateEndDate.text =
                                    String.format("종료 : ${dateYear}.${monthOfYear+1}.${dayOfMonth} ${hour}:%02d", minute)
                            }
                            setAddButton()
                        }
                    }.show(supportFragmentManager, "Snap")
                }
                datePicker.minDate = System.currentTimeMillis() - 1000
                show()
            }
        }

    }

    private fun setAddButton(){
        with(binding) {
            btnCreateAdd.apply {
                visibility = if (!etCreatePlace.text.isNullOrBlank() && !etCreateDetail.text.isNullOrBlank() && btnCreateStartDate.text != "시작 시간" && btnCreateEndDate.text != "종료 시간") View.VISIBLE else View.INVISIBLE
            }
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val rect = Rect()
            currentFocus?.getGlobalVisibleRect(rect)
            val x = ev!!.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                currentFocus?.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
