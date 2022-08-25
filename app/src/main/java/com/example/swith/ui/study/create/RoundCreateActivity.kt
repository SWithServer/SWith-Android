package com.example.swith.ui.study.create

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.SwithApplication
import com.example.swith.data.DateTime
import com.example.swith.data.Session
import com.example.swith.databinding.ActivityRoundCreateBinding
import com.example.swith.databinding.DialogTimepickerBinding
import com.example.swith.ui.dialog.BottomSheet
import com.example.swith.ui.dialog.CustomAlertDialog
import com.example.swith.ui.dialog.CustomTimePickerDialog
import com.example.swith.viewmodel.RoundUpdateViewModel
import java.lang.Integer.max
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*


open class RoundCreateActivity : AppCompatActivity(), View.OnClickListener {
    protected val viewModel : RoundUpdateViewModel by viewModels()
    // 회차 최소시간(분단위)
    private val minuteMin by lazy {
        intent.getIntExtra("minuteMin", 10)
    }

    // 회차 최대시간(시간단위)
    private val hourMax = 8
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    protected var startTime: DateTime? = null
    protected var endTime: DateTime? = null

    private val groupIdx by lazy {
        intent.getLongExtra("groupIdx", 0)
    }

    protected lateinit var binding: ActivityRoundCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_round_create)

        initPlaceCheck()
        initListener()
        observeViewModel()
    }

    private fun initPlaceCheck(){
        with(binding){
            etCreatePlace.isEnabled = false
            btnCreateOnline.setOnClickListener {
                btnCreateOnline.isSelected = true
                btnCreateOffline.isSelected = false
                etCreatePlace.apply {
                    isEnabled = false
                    setText("온라인")
                }
            }
            btnCreateOffline.setOnClickListener {
                btnCreateOnline.isSelected = false
                btnCreateOffline.isSelected = true
                etCreatePlace.apply{
                    isEnabled = true
                    hint = resources.getString(R.string.create_place_hint2)
                    setText("")
                }
            }
        }
    }

    protected open fun initListener(){
        // 시간 설정 후에 추가해줘야 함
        with(binding){
            binding.clickListener = this@RoundCreateActivity
            btnCreateStartDate.setOnClickListener {
                if (startTime != null && endTime != null) tvRoundCreateAlert.visibility = View.VISIBLE
                else initDateTimePicker(true)
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
            toolbarCreate.tvRoundTitle.visibility = View.INVISIBLE
            tvCreateReset.setOnClickListener {
                // 입력 데이터 초기화
                startTime = null
                endTime = null
                btnCreateStartDate.text = "시작 시간"
                btnCreateStartDate.isClickable = true
                btnCreateEndDate.text = "종료 시간"
                tvRoundCreateAlert.visibility = View.GONE
                btnCreateAdd.visibility = View.INVISIBLE
            }
            btnCreateAdd.setOnClickListener {
                val startTimeToString : String = String.format("%4d-%02d-%02dT%02d:%02d", startTime?.year, startTime?.month, startTime?.day, startTime?.hourOfDay, startTime?.minute)
                val endTimeToString : String = String.format("%4d-%02d-%02dT%02d:%02d", endTime?.year, endTime?.month, endTime?.day, endTime?.hourOfDay, endTime?.minute)
                val online : Int = if(btnCreateOnline.isSelected) 1 else 0
                val userId: Long = if (SwithApplication.spfManager.getLoginData() != null) SwithApplication.spfManager.getLoginData()?.userIdx!! else 1
                if (checkCondition()) {
                    BottomSheet("회차 생성", null, resources.getString(R.string.bottom_round_create_guide), "생성").apply {
                        setCustomListener(object: BottomSheet.customClickListener{
                            override fun onCheckClick() {
                                dismiss()
                                viewModel.postRound(Session(
                                    groupIdx, online, etCreatePlace.text.toString(), etCreateDetail.text.toString(), endTimeToString, startTimeToString, userId))
                            }
                        })
                    }.show(supportFragmentManager, "roundCreate")
                }
            }
        }
    }

    protected open fun observeViewModel(){
        viewModel.sessionLiveEvent.observe(this, Observer{
            Toast.makeText(applicationContext, "회차 생성이 완료되었습니다", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
        })
        viewModel.mutableErrorMessage.observe(this, Observer {
            CustomAlertDialog("생성 오류", it).show(supportFragmentManager, "회차 생성 오류")
        })
    }

    private fun initDateTimePicker(isStart: Boolean){
        with(binding) {
            DatePickerDialog(this@RoundCreateActivity, R.style.DatePickerTheme, { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
            }, year, month, day).apply {
                // 지금보다 이전 날짜(과거 날짜) 비활성화
                // displayed value를 젤 먼저 바꿔야함!!
                setOnDateSetListener { _, dateYear, monthOfYear, dayOfMonth ->
                    val dialog = CustomTimePickerDialog(this@RoundCreateActivity)
                    val dialogBinding = dialog.dialogBinding
                    dialogBinding.tvTimePickerTitle.text = if (isStart) "시작시간" else "종료시간"
                    dialogBinding.tvDialogGuide.text = "해당 스터디의 회차 최소시간은 ${minuteMin}분 입니다."
                    dialogBinding.tvDialogTimelimitGuide.text = "최대 ${hourMax}시간 생성 가능"
                    dialogBinding.npHourPicker.apply {
                        wrapSelectorWheel = false
                        descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                        with(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))) {
                            val minuteIdx = minute / 10
                            // 시작 시간보다 종료 시간이 더 뒤에 있어야 함
                            if (!isStart && startTime != null && startTime?.year == dateYear && startTime?.month == monthOfYear + 1 && startTime?.day == dayOfMonth) {
                                // 종료시간 선택시 시작시간과 동일한 날짜를 골랐을 때
                                minValue = if (startTime?.minute!! >= 60 - minuteMin) startTime?.hourOfDay!! + 1 else startTime?.hourOfDay!!
                                value = minValue
                            }else if (year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth) {
                                if (!isStart && startTime == null){
                                    // 현재 시각이 40분이상인 경우 시작 시간이 50분 이상이기 때문에 회차의 최소시간을 고려하여 다음 시간으로 넘겨야함
                                    if (minuteIdx + minuteMin / 10 >= 5) {
                                        minValue = hour + 1.also {
                                            value = hour + 1
                                        }
                                    }
                                    else  minValue = hour .also {
                                            value = hour
                                        }
                                }
                                else if (isStart && endTime != null){
                                    minValue = if (dayOfMonth == endTime?.day) {
                                        if (minuteIdx >= 5) {
                                            max(hour + 1, endTime?.hourOfDay!! - hourMax)
                                        } else if (endTime?.hourOfDay == hour + 1 && endTime?.minute!! <= minuteMin){
                                            max(hour + 1, endTime?.hourOfDay!! - hourMax)
                                        } else max(hour, endTime?.hourOfDay!! - hourMax)
                                    } else {
                                        max(hour, endTime?.hourOfDay!! + 24 - hourMax)
                                    }
                                } else if (isStart && minuteIdx >= 5){
                                    minValue = hour + 1 .also {
                                        value = hour + 1
                                    }
                                }
                                else if (!isStart && minute + minuteMin + 10 >= 60){
                                    minValue = hour + 1 .also {
                                        value = hour + 1
                                    }
                                } else minValue = hour .also {
                                    value = hour
                                }
                            } else {
                                minValue = if (isStart && endTime != null){
                                    if (dayOfMonth == endTime?.day) max(0, endTime?.hourOfDay!! - hourMax)
                                    else endTime?.hourOfDay!! + 24 - hourMax
                                } else 0
                                value = minValue
                            }
                            // maxValue 부분
                            maxValue = if (isStart && endTime != null && endTime?.year == dateYear && endTime?.month == monthOfYear + 1 && endTime?.day == dayOfMonth){
                                if (endTime?.minute!! < minuteMin) {
                                    endTime?.hourOfDay!! - 1
                                } else {
                                    endTime?.hourOfDay!!
                                }
                            } else if (!isStart && startTime != null && startTime?.year == dateYear && startTime?.month == monthOfYear + 1 && startTime?.day == dayOfMonth){
                                if (startTime?.hourOfDay!! + hourMax > 23) 23
                                else startTime?.hourOfDay!! + hourMax
                            } else if (!isStart && startTime != null && startTime?.hourOfDay!! + hourMax > 23){
                                (startTime?.hourOfDay !! + hourMax)% 24
                            }else {
                                23
                            }

                            setOnValueChangedListener { _, _, newVal ->
                                dialogBinding.npMinutePicker.apply{
                                    value = minValue
                                    if (!isStart && startTime != null){
                                        if(startTime?.day == dayOfMonth) when (newVal) {
                                            startTime?.hourOfDay -> {
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50"
                                                ).sliceArray((startTime?.minute!! + minuteMin) / 10..5)
                                                minValue = (startTime?.minute!! + minuteMin)/ 10
                                                maxValue = 5
                                            }
                                            startTime?.hourOfDay!! + 1 -> {
                                                if (startTime?.minute!! + minuteMin >= 60){
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(((startTime?.minute!! + minuteMin - 60) / 10) % 6..5)
                                                    minValue = ((startTime?.minute!! + minuteMin - 60) / 10) % 6
                                                } else {
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                    minValue = 0
                                                }

                                                value = 0
                                                maxValue = 5
                                            }
                                            startTime?.hourOfDay!! + hourMax -> {
                                                value = 0
                                                if (startTime?.minute == 0){
                                                    displayedValues = null
                                                    minValue = 0
                                                    maxValue = 0
                                                } else {
                                                    displayedValues = arrayOf(
                                                        "0", "10", "20", "30", "40", "50"
                                                    ).sliceArray(0..startTime?.minute!! / 10)
                                                    minValue = 0
                                                    maxValue = startTime?.minute!! / 10
                                                }
                                            }
                                            else -> {
                                                value = 0
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                minValue = 0
                                                maxValue = 5
                                            }
                                        } else when (newVal) {
                                            (startTime?.hourOfDay!! + hourMax) % 24 -> {
                                                if (startTime?.minute == 0){
                                                    displayedValues = null
                                                    minValue = 0
                                                    maxValue = 0
                                                }
                                                else {
                                                    displayedValues = arrayOf(
                                                        "0", "10", "20", "30", "40", "50"
                                                    ).sliceArray(0..startTime?.minute!! / 10)
                                                    minValue = 0
                                                    maxValue = startTime?.minute!! / 10
                                                }
                                            }
                                            else -> {
                                                if (newVal == 0 && startTime?.hourOfDay == 23 && startTime?.minute!! + minuteMin >= 60){
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                        .sliceArray((startTime?.minute!! + minuteMin - 60) / 10..5)
                                                    minValue = (startTime?.minute!! + minuteMin - 60) / 10
                                                }
                                                else {
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                    minValue = 0
                                                }
                                                maxValue = 5
                                            }
                                        }
                                    }else if (isStart && endTime != null) {
                                        if (endTime?.day == dayOfMonth){
                                            when(newVal){
                                                endTime?.hourOfDay!! -> {
                                                    if (endTime?.minute == minuteMin){
                                                        displayedValues = null
                                                        minValue = 0
                                                        maxValue = 0
                                                    }else {
                                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(0..(endTime?.minute!! - minuteMin) / 10)
                                                        minValue = 0
                                                        maxValue = (endTime?.minute!! - minuteMin) / 10
                                                    }
                                                }
                                                endTime?.hourOfDay!! -1 -> {
                                                    if (endTime?.minute!! < minuteMin){
                                                        if (day == dayOfMonth && newVal == hour){
                                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minuteIdx + 1..(endTime?.minute!! + 60 - minuteMin) / 10)
                                                            minValue = minuteIdx + 1
                                                        }
                                                        else {
                                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(0..(endTime?.minute!! + 60 - minuteMin) / 10)
                                                            minValue = 0
                                                        }
                                                        maxValue = (endTime?.minute!! + 60 - minuteMin) / 10
                                                    }
                                                    else{
                                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                        minValue = 0
                                                        maxValue = 5
                                                        Log.e("test12", "발동 ${displayedValues.size}")
                                                    }
                                                }
                                                endTime?.hourOfDay!! - hourMax -> {
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50"
                                                    ).sliceArray((endTime?.minute!!) / 10..5)
                                                    minValue = (endTime?.minute!!) / 10
                                                    maxValue = 5
                                                }
                                                else -> {
                                                    if (newVal == hour){
                                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray((minute/10)+ 1..5)
                                                        minValue = (minute/10)+ 1
                                                    }
                                                    else {
                                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                        minValue = 0
                                                    }
                                                    maxValue = 5
                                                }
                                            }
                                        }
                                        else {
                                            when(newVal){
                                                (endTime?.hourOfDay!! + 24 - hourMax) % 24 ->{
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50"
                                                    ).sliceArray(endTime?.minute!! / 10..5)
                                                    minValue = endTime?.minute!! / 10
                                                    maxValue = 5
                                                }
                                                else -> {
                                                    if (newVal == 23 && endTime?.hourOfDay == 0 && endTime?.minute!! < minuteMin){
                                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(0..(endTime?.minute!! - minuteMin + 60) / 10)
                                                        maxValue = (endTime?.minute!! - minuteMin + 60) / 10
                                                    }
                                                    else {
                                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                        maxValue = 5
                                                    }
                                                    minValue = 0
                                                }
                                            }
                                        }
                                    }
                                    else if (year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth) {
                                        if (newVal.toString() == hour.toString()) {
                                            if (minuteIdx == 4){
                                                displayedValues = arrayOf("50")
                                                minValue = 5
                                            } else {
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minuteIdx + 1..5)
                                                minValue = minuteIdx + 1
                                            }
                                        } else {
                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                            value = 0
                                            minValue = 0
                                        }
                                        maxValue = 5
                                    }

                                }
                            }
                        }
                    }

                    dialogBinding.npMinutePicker.apply {
                        wrapSelectorWheel = false
                        descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                        with(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))) {
                            if (!isStart && startTime != null) {
                                // 시작 시간의 날짜와 종료 시간의 날짜가 동일한 경우
                                if (startTime?.day == dayOfMonth){
                                    if (startTime?.hourOfDay.toString() == dialogBinding.npHourPicker.value.toString()) {
                                        minValue = (startTime?.minute!! / 10) + (minuteMin / 10)
                                        maxValue = 5
                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minValue..5)
                                    } else if (startTime?.minute!! + minuteMin >= 60 && (startTime?.hourOfDay!! + 1).toString() == dialogBinding.npHourPicker.value.toString()){
                                        minValue = ((startTime?.minute!! / 10) + (minuteMin / 10)) % 6
                                        maxValue = 5
                                        displayedValues =
                                            arrayOf("0", "10", "20", "30", "40", "50").sliceArray(
                                                minValue..5
                                            )
                                    } else {
                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                        minValue = 0
                                        maxValue = 5
                                    }
                                } else {
                                    minValue = if (startTime?.hourOfDay == 23 && startTime?.minute!! >= 60 - minuteMin){
                                        (startTime?.minute!! + minuteMin - 60) / 10
                                    } else 0
                                    maxValue = if (dialogBinding.npHourPicker.value == startTime?.hourOfDay!! + hourMax - 24)
                                        startTime?.minute!! / 10
                                    else 5
                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(
                                        minValue..maxValue
                                    )
                                }
                            }else if (isStart && endTime != null && dateYear == endTime?.year && monthOfYear + 1 == endTime?.month) {
                                // 종료 시간 먼저 설정 후 시작 시간 설정하는 경우
                                // 현재 시간 보다는 뒤어야 함
                                if (dayOfMonth == endTime?.day) {
                                    if (endTime?.hourOfDay.toString() == dialogBinding.npHourPicker.value.toString()) {
                                        if (dayOfMonth == day && dateYear == year && monthValue == monthOfYear + 1) {
                                            if (hour == dialogBinding.npHourPicker.value){
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(max(0, minute / 10 + 1)..(endTime?.minute!! / 10) - (minuteMin / 10))
                                                minValue = max(0, minute / 10 + 1)
                                            } else {
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray((minute / 10 + 1) % 6..(endTime?.minute!! / 10) - (minuteMin / 10))
                                                minValue = (minute / 10 + 1) % 6
                                            }
                                            maxValue = (endTime?.minute!! / 10) - (minuteMin / 10)
                                        } else {
                                            displayedValues =
                                                arrayOf("0", "10", "20", "30", "40", "50")
                                            minValue = 0
                                            maxValue = 5
                                        }
                                    } else if ((endTime?.hourOfDay!! - hourMax) % 24 == dialogBinding.npHourPicker.value) {
                                        displayedValues =
                                            arrayOf("0", "10", "20", "30", "40", "50").sliceArray(
                                                endTime?.minute!! / 10..5
                                            )
                                        minValue = endTime?.minute!! / 10
                                        maxValue = 5

                                    } else if (dialogBinding.npHourPicker.value == hour){
                                        displayedValues =
                                            arrayOf("0", "10", "20", "30", "40", "50").sliceArray(
                                                minute/10 + 1..5
                                            )
                                        minValue = minute/10 + 1
                                        maxValue = 5
                                    } else {
                                        // 직전 시간
                                        if (dialogBinding.npHourPicker.value + 1 == endTime?.hourOfDay && endTime?.minute!! < minuteMin){
                                            if (dialogBinding.npHourPicker.value == hour){
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minute / 10 + 1..(endTime?.minute!! - minuteMin + 60)/10)
                                                minValue = minute / 10 + 1
                                            }
                                            else {
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(0..(endTime?.minute!! - minuteMin + 60)/10)
                                                minValue = 0
                                            }
                                            maxValue = (endTime?.minute!! - minuteMin + 60)/10
                                        }
                                        else {
                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                            minValue = 0
                                            maxValue = 5
                                        }
                                    }
                                } else {
                                    if ((endTime?.hourOfDay!! + 24 - hourMax) % 24 == dialogBinding.npHourPicker.value){
                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(endTime?.minute!! / 10..5)
                                        minValue = endTime?.minute!! / 10
                                        maxValue = 5
                                    } else {
                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                        minValue = 0
                                        maxValue = 5
                                    }
                                }
                            }
                            // 오늘이 아닌 경우
                            else if (year != dateYear || monthValue != monthOfYear + 1 || day != dayOfMonth) {
                                // 다음 날과 확인해보기
                                if (!isStart && year == dateYear && monthValue == monthOfYear && hour == 23 && minute >= 60 - minuteMin){
                                    calendar.add(Calendar.DATE, 1)
                                    if (calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth){
                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray((minute + minuteMin - 60)/ 10 .. 5)
                                        minValue = (minute + minuteMin - 60)/ 10
                                    } else {
                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                        minValue = 0
                                    }
                                    maxValue = 5
                                } else {
                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                    minValue = 0
                                    maxValue = 5
                                }
                            }
                            else if (year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth && hour.toString() == dialogBinding.npHourPicker.value.toString()) {
                                minValue = if (isStart) minute / 10 + 1 else ((minuteMin + minute) / 10 + 1) % 6
                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minValue..5)
                                maxValue = 5
                            }else {
                                minValue = if (!isStart && year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth && hour.plus(1).toString() == dialogBinding.npHourPicker.value.toString() ){
                                    (minute + minuteMin - 50) / 10
                                }else 0
                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minValue..5)
                                maxValue = 5
                            }
                        }
                    }
                    dialog.apply {
                        setCustomListener(object: CustomTimePickerDialog.ClickListener{
                            override fun roundCreate() {
                                if (isStart) {
                                    btnCreateStartDate.text = String.format(
                                        "시작 : ${dateYear}.${monthOfYear + 1}.${dayOfMonth} ${dialogBinding.npHourPicker.value}:%02d",
                                        (dialogBinding.npMinutePicker.value) * 10
                                    )
                                    startTime = DateTime(dateYear, monthOfYear + 1, dayOfMonth, dialogBinding.npHourPicker.value, dialogBinding.npMinutePicker.value * 10)

                                    // 종료 시간이 설정 되어 있을 때 새롭게 설정된 시작 시간이 종료 시간 보다 같거나 느린 경우(종료 시간이 더 빠름) 종료 시간 초기화
                                    if (compareStartEndTime() == 2 || compareStartEndTime() == 3) {
                                        btnCreateEndDate.text = "종료 시간"
                                        endTime = null
                                    }
                                    setAddButton()
                                } else {
                                    btnCreateEndDate.text = String.format(
                                        "종료 : ${dateYear}.${monthOfYear + 1}.${dayOfMonth} ${dialogBinding.npHourPicker.value}:%02d",
                                        (dialogBinding.npMinutePicker.value) * 10
                                    )
                                    endTime = DateTime(dateYear, monthOfYear + 1, dayOfMonth, dialogBinding.npHourPicker.value, dialogBinding.npMinutePicker.value * 10
                                    )

                                    // 종료 시간이 설정 되어 있을 때 새롭게 설정된 시작 시간이 종료 시간 보다 같거나 느린 경우(종료 시간이 더 빠름) 종료 시간 초기화
                                    if (compareStartEndTime() == 2 || compareStartEndTime() == 3) {
                                        btnCreateStartDate.text = "시작 시간 "
                                        startTime = null
                                    }
                                    setAddButton()
                                }
                                dismiss()
                            }
                        })
                    }.show(supportFragmentManager, "timepicker")
                }
                if(isStart || startTime == null){
                    with(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))) {
                        if (hour == 23 && minute >= 60 - minuteMin)
                            datePicker.minDate = System.currentTimeMillis() + 86400000
                        else datePicker.minDate = System.currentTimeMillis() - 1000
                    }
                } else {
                    startTime?.let{
                        // startTime의 시간이 23시 50분이면 다음날로 minDate
                        if (startTime?.hourOfDay == 23 && startTime?.minute!! >= 60 - minuteMin){
                            calendar.set(startTime?.year!!, startTime?.month!! - 1, startTime?.day!!)
                            datePicker.minDate = calendar.timeInMillis + 86400000
                            datePicker.maxDate = datePicker.minDate
                        } else {
                            calendar.set(startTime?.year!!, startTime?.month!! - 1, startTime?.day!!)
                            if (startTime?.hourOfDay!! + hourMax > 23){
                                datePicker.maxDate = calendar.timeInMillis + 86400000
                            } else datePicker.maxDate = calendar.timeInMillis
                            datePicker.minDate = calendar.timeInMillis
                        }
                    }
                }

                if(isStart){
                    endTime?.let{
                        // endtime의 시간이 0시 0분이면 maxDate하루 전날로
                        if (endTime?.hourOfDay == 0 && endTime?.minute!! <= minuteMin - 10){
                            calendar.set(endTime?.year!!, endTime?.month!! - 1, endTime?.day!!)
                            datePicker.maxDate = calendar.timeInMillis - 86400000
                        }else {
                            calendar.set(endTime?.year!!, endTime?.month!! - 1, endTime?.day!!)
                            if (endTime?.hourOfDay !! - hourMax < 0){
                                datePicker.minDate = calendar.timeInMillis - 86400000
                            } else datePicker.minDate = calendar.timeInMillis
                            datePicker.maxDate = calendar.timeInMillis
                        }
                    }
                }
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

    // 코드로 가능한 경우 다 막음
    private fun compareStartEndTime() : Int {
        // 0 : 둘다 null
        // 1 : startTime 이 더 빠름 (startTime 의 값이 더 작음)
        // 2 : endTime 이 더 빠름 (이 경우 로직 수정해야 함)
        // 3 : 둘이 같음
        startTime?.let{
            endTime?.let{
                val startTimeToLong = String.format("%4d%02d%02d%02d%02d", startTime?.year, startTime?.month, startTime?.day, startTime?.hourOfDay, startTime?.minute).toLong()
                val endTimeToLong = String.format("%4d%02d%02d%02d%02d", endTime?.year, endTime?.month, endTime?.day, endTime?.hourOfDay, endTime?.minute).toLong()
                return if (startTimeToLong < endTimeToLong) 1
                else if (startTimeToLong > endTimeToLong) 2
                else 3
            }
        }
        return 0
    }

    // 현재 시간과 비교 후 창 띄우기
    private fun checkCondition() : Boolean {
        startTime?.let {
            with(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))){
                val startTimeToLong = String.format("%4d%02d%02d%02d%02d", startTime?.year, startTime?.month, startTime?.day, startTime?.hourOfDay, startTime?.minute).toLong()
                val nowTimeToLong = String.format("%4d%02d%02d%02d%02d", year, monthValue, day, hour, minute).toLong()
                if (startTimeToLong <= nowTimeToLong){
                    CustomAlertDialog("시작시간 오류", "시작시간이 현재 시간보다 전입니다.\n시간 초기화 후 다시 설정해주세요.").show(supportFragmentManager, "startTimeAlert")

                    return false
                } else {
                    endTime?.let {
                        val endTimeToLong = String.format("%4d%02d%02d%02d%02d", endTime?.year, endTime?.month, endTime?.day, endTime?.hourOfDay, endTime?.minute).toLong()
                        if (startTime?.day == endTime?.day && endTimeToLong - startTimeToLong > 800){
                            CustomAlertDialog("회차시간 오류", "회차 시간이 8시간 보다 깁니다. \n시간 초기화 후 다시 설정해주세요.").show(supportFragmentManager, "endTimeAlert")
                            return false
                        }
                    }
                }
            }
        }
        return true
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

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_round_toolbar_back -> finish()
        }
    }
}
