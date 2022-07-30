package com.example.swith.ui.study.create

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
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
import java.lang.Integer.max
import java.time.LocalDateTime
import java.util.*


class RoundCreateActivity : AppCompatActivity() {
    // 회차 최소시간(분단위)
    private val minuteMin = 30
    // 회차 최대시간(시간단위)
    private val hourMax = 8
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
            tvCreateReset.setOnClickListener {
                // 입력 데이터 초기화
                startTime = null
                endTime = null
                btnCreateStartDate.text = "시작 시간"
                btnCreateEndDate.text = "종료 시간"
                cbCreateOffline.isChecked = false
                cbCreateOnline.isChecked = false
                etCreatePlace.apply {
                    isClickable = false
                    setText("")
                    hint = resources.getString(R.string.create_place_hint)
                }
                etCreateDetail.apply {
                    setText("")
                    hint = resources.getString(R.string.create_detail)
                }
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
                // displayed value를 젤 먼저 바꿔야함!!
                setOnDateSetListener { _, dateYear, monthOfYear, dayOfMonth ->
                    val dialogBinding : DialogTimepickerBinding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_timepicker, null, false)
                    dialogBinding.tvDialogGuide.text = "해당 스터디의 회차 최소시간은 ${minuteMin}분 입니다."
                    dialogBinding.npHourPicker.apply {
                        wrapSelectorWheel = false
                        descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                        with(LocalDateTime.now()) {
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
                                        } else max(hour, endTime?.hourOfDay!! - hourMax)
                                    } else {
                                        endTime?.hourOfDay!! + 24 - hourMax
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
                                                minValue = (startTime?.minute!! / 10) + minuteMin / 10
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50"
                                                ).sliceArray(minValue..5)
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
                                                minValue = 0
                                                maxValue = startTime?.minute!! / 10
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50"
                                                ).sliceArray(0..maxValue)
                                            }
                                            else -> {
                                                value = 0
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                minValue = 0
                                                maxValue = 5
                                            }
                                        } else when (newVal) {
                                            (startTime?.hourOfDay!! + hourMax) % 24 -> {
                                                maxValue = startTime?.minute!! / 10
                                                displayedValues = arrayOf(
                                                    "0", "10", "20", "30", "40", "50"
                                                ).sliceArray(0..maxValue)
                                            }
                                            else -> {
                                                minValue = 0
                                                if (newVal == 0 && startTime?.hourOfDay == 23 && startTime?.minute!! + minuteMin >= 60)
                                                    minValue = (startTime?.minute!! + minuteMin - 60) / 10
                                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minValue..5)
                                                maxValue = 5
                                            }
                                        }
                                    }else if (isStart && endTime != null) {
                                        if (endTime?.day == dayOfMonth){
                                            when(newVal){
                                                endTime?.hourOfDay!! -> {
                                                    minValue = 0
                                                    maxValue = (endTime?.minute!! - minuteMin) / 10
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50"
                                                    ).sliceArray(0..maxValue)
                                                }
                                                endTime?.hourOfDay!! - hourMax -> {
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50"
                                                    ).sliceArray(0..(endTime?.minute!!) / 10)
                                                    minValue = 0
                                                    maxValue = (endTime?.minute!!) / 10
                                                }
                                                else -> {
                                                    if (newVal == endTime?.hourOfDay!! - 1 &&  endTime?.minute!! < minuteMin){
                                                        if (day == dayOfMonth && newVal == hour){
                                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minuteIdx + 1..(endTime?.minute!! + 60 - minuteMin) / 10)
                                                            minValue = minuteIdx + 1
                                                        }
                                                        else {
                                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(0..(endTime?.minute!! + 60 - minuteMin) / 10)
                                                            minValue = 0
                                                        }
                                                        maxValue = (endTime?.minute!! + 60 - minuteMin) / 10
                                                    } else{
                                                        displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                                        maxValue = 5
                                                    }

                                                }
                                            }
                                        }
                                        else {
                                            when(newVal){
                                                (endTime?.hourOfDay!! + 24 - hourMax) % 24 ->{
                                                    minValue = endTime?.minute!! / 10
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50"
                                                    ).sliceArray(minValue..5)
                                                    maxValue = 5
                                                }
                                                else -> {
                                                    minValue = 0
                                                    maxValue = 5
                                                    if (newVal == 23 && endTime?.hourOfDay == 0 && endTime?.minute!! < minuteMin){
                                                        maxValue = (endTime?.minute!! - minuteMin + 60) / 10
                                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(0..maxValue)
                                                }
                                            }
                                        }
                                    }
                                    }else if (year == dateYear && monthValue == monthOfYear + 1 && day == dayOfMonth) {
                                        if (newVal.toString() == hour.toString()) {
                                            minValue = minuteIdx + 1
                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(minuteIdx + 1..5)
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
                        with(LocalDateTime.now()) {
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
                                    maxValue = 5
                                    displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(
                                        minValue..5
                                    )
                                }
                            }else if (isStart && endTime != null && dateYear == endTime?.year && monthOfYear + 1 == endTime?.month) {
                                // 종료 시간 먼저 설정 후 시작 시간 설정하는 경우
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
                                    } else {
                                        // 직전 시간
                                        if (dialogBinding.npHourPicker.value + 1 == endTime?.hourOfDay && endTime?.minute!! < minuteMin){
                                            displayedValues = arrayOf("0", "10", "20", "30", "40", "50").sliceArray(0..(endTime?.minute!! - minuteMin + 60)/10)
                                            minValue = 0
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
                            else if (year != dateYear || monthValue != monthOfYear + 1 || day != dayOfMonth) {
                                minValue = 0
                                displayedValues = arrayOf("0", "10", "20", "30", "40", "50")
                                maxValue = 5
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
                                startTime = DateTime(dateYear, monthOfYear + 1, dayOfMonth, dialogBinding.npHourPicker.value, dialogBinding.npMinutePicker.value * 10)

                                // 종료 시간이 설정 되어 있을 때 새롭게 설정된 시작 시간이 종료 시간 보다 같거나 느린 경우(종료 시간이 더 빠름) 종료 시간 초기화
                                if (compareStartEndTime() == 2 || compareStartEndTime() == 3){
                                    btnCreateEndDate.text = "종료 시간"
                                    endTime = null
                                }
                            }
                            else {
                                btnCreateEndDate.text = String.format(
                                    "종료 : ${dateYear}.${monthOfYear + 1}.${dayOfMonth} ${dialogBinding.npHourPicker.value}:%02d",
                                    (dialogBinding.npMinutePicker.value) * 10
                                )
                                endTime = DateTime(dateYear, monthOfYear + 1, dayOfMonth, dialogBinding.npHourPicker.value, dialogBinding.npMinutePicker.value * 10)

                                // 종료 시간이 설정 되어 있을 때 새롭게 설정된 시작 시간이 종료 시간 보다 같거나 느린 경우(종료 시간이 더 빠름) 종료 시간 초기화
                                if (compareStartEndTime() == 2 || compareStartEndTime() == 3){
                                    btnCreateStartDate.text = "시작 시간 "
                                    startTime = null
                                }
                            }
                        }
                        .setNegativeButton("취소") { _, _
                            -> this.show() }
                        .create()
                        .show()
                }
                if(isStart || startTime == null){
                    with(LocalDateTime.now()) {
                        if (hour == 23 && minute >= 50)
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
