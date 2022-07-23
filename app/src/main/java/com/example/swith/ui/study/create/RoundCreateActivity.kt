package com.example.swith.ui.study.create

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.data.DateTime
import com.example.swith.databinding.ActivityRoundCreateBinding
import com.example.swith.databinding.DialogTimepickerBinding
import com.example.swith.utils.ToolBarManager
import java.time.LocalDateTime
import java.util.*


class RoundCreateActivity : AppCompatActivity() {
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    private var startTime: DateTime? = null
    private var endTime: DateTime? = null

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
                    val dialogBinding : DialogTimepickerBinding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_timepicker, null, false)
                    dialogBinding.npHourPicker.apply {
                        wrapSelectorWheel = false
                        descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                        with(LocalDateTime.now()) {
                            // 오늘인 경우
                            val minuteIdx = minute / 10
                            minValue = if (year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth) {
                                if (minuteIdx == 5 && minute % 10 > 0){
                                    hour + 1 .also {
                                        value = hour + 1
                                    }
                                }
                                else hour .also {
                                    value = hour
                                }
                            } else 0
                            maxValue = 23

                            setOnValueChangedListener { _, _, newVal ->
                                dialogBinding.npMinutePicker.apply{
                                    value = minValue
                                    if (year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth) {
                                        maxValue = 5
                                        if (newVal.toString() == hour.toString()) {
                                            minValue = minuteIdx + 1
                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minuteIdx + 1..5)
                                        } else {
                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                            value = 0
                                            minValue = 0
                                        }
                                    }
                                }
                            }
                        }
                    }

                    dialogBinding.npMinutePicker.apply {
                        wrapSelectorWheel = false
                        descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                        with(LocalDateTime.now()) {
                            if (year != dateYear || monthValue != monthOfYear + 1 || day != dayOfMonth) {
                                minValue = 0
                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                            } else if (year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth && hour.toString() == dialogBinding.npHourPicker.value.toString()){
                                minValue = (minute / 10) + 1
                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minute / 10 + 1..5)
                            }
                            maxValue = 5
                        }
                    }
                    AlertDialog.Builder(this@RoundCreateActivity)
                        .setTitle(if (isStart) "시작 시간" else "종료 시간")
                        .setView(dialogBinding.root)
                        .setPositiveButton("확인") { dialog, which
                            ->
                            if (isStart) {
                                btnCreateStartDate.text = String.format(
                                    "시작 : ${dateYear}.${monthOfYear + 1}.${dayOfMonth} ${dialogBinding.npHourPicker.value}:%02d",
                                    (dialogBinding.npMinutePicker.value) * 10
                                )
                                startTime = DateTime(dateYear, monthOfYear, dayOfMonth, dialogBinding.npHourPicker.value, dialogBinding.npMinutePicker.value * 10)
                            }
                            else {
                                btnCreateEndDate.text = String.format(
                                    "종료 : ${dateYear}.${monthOfYear + 1}.${dayOfMonth} ${dialogBinding.npHourPicker.value}:%02d",
                                    (dialogBinding.npMinutePicker.value) * 10
                                )
                                endTime = DateTime(dateYear, monthOfYear, dayOfMonth, dialogBinding.npHourPicker.value, dialogBinding.npMinutePicker.value * 10)
                            }
                        }
                        .setNegativeButton("취소") { _, _
                            -> this.show() }
                        .create()
                        .show()
                }
                datePicker.minDate = System.currentTimeMillis() - 1000
                show()
            }
        }

    }

    private fun setAddButton(){
        with(binding) {
            btnCreateAdd.apply {
                visibility = if (!etCreatePlace.text.isNullOrBlank() && !etCreateDetail.text.isNullOrBlank() && startTime != null && endTime != null) View.VISIBLE else View.INVISIBLE
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
