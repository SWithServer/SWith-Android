package com.example.swith.ui.manage

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageStudyModifyBinding
import com.example.swith.ui.study.create.SelectPlaceActivity
import java.util.*

class ManageStudyModifyActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivityManageStudyModifyBinding
    var groupIdx : Int = -1

    var title:String=""
    //    val userid= SharedPrefManager(this@StudyCreateActivity).getLoginData()
//    val userIdx = userid?.userIdx
    var meet_idx:Int= -1
    var frequency_content:Int?=null
    var periods_content:String?=null
    // 오프라인 0 온라인 1
    var online_idx:Int = -1
    var topic_content:String =""
    var regionIdx1:String?=null
    var regionIdx2:String?=null
    var group_content:String=""

    // spinner 선택되는 값들 매칭
    var interest_idx:Int = -1
    var memberLimit_content:Int=-1
    var applicationMethod_idx : Int=-1
    var attendanceVaildTime_content : Int=-1

    // 날짜 입력값들
    var recruitmentEndDate_ : String = ""
    var groupStart_ : String =""
    var groupEnd_ : String =""

    private lateinit var startTime: Calendar //활동 시작기간
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ManageStudyModifyActivity, R.layout.activity_manage_study_modify)

        initData()
        initView(groupIdx)

        with(binding)
        {
            clickListener = this@ManageStudyModifyActivity
            etStudyContent.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyContent.text.equals("")){
                    group_content= etStudyContent.text.toString()
                    hideKeyboard(etStudyContent)
                    true
                }
                else{
                    false
                }
            }
            etCreateTopic.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etCreateTopic.text.equals("")){
                    topic_content= etCreateTopic.text.toString()
                    hideKeyboard(etCreateTopic)
                    true
                }
                else{
                    false
                }
            }
            etStudyTitle.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyTitle.text.equals("")){
                    title= etStudyTitle.text.toString()
                    hideKeyboard(etStudyTitle)
                    true
                }
                else{
                    false
                }
            }
            etStudyWeek.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyWeek.text.equals("")){
                    group_content= etStudyWeek.text.toString()
                    hideKeyboard(etStudyWeek)
                    true
                }
                else{
                    false
                }
            }
            etStudyMonth.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyMonth.text.equals("")){
                    group_content= etStudyMonth.text.toString()
                    hideKeyboard(etStudyMonth)
                    true
                }
                else{
                    false
                }
            }
            etStudyFree.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyFree.text.equals("")){
                    group_content= etStudyFree.text.toString()
                    hideKeyboard(etStudyFree)
                    true
                }
                else{
                    false
                }
            }

            btnPlusPlace1.setOnClickListener {
                var intent = Intent(this@ManageStudyModifyActivity, SelectPlaceActivity::class.java)
                intent.putExtra("번호",1)
                startActivity(intent)
            }
            btnPlusPlace2.setOnClickListener {
                var intent = Intent(this@ManageStudyModifyActivity, SelectPlaceActivity::class.java)
                intent.putExtra("번호",2)
                startActivity(intent)
            }
        }

        // 모집 마감기간 설정
        binding.btnDeadline.setOnClickListener {
            //팝업 달력
            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                if (month<10)
                { binding.btnDeadline.text =
                    year.toString() + "-0" + (month + 1).toString() + "-" + day.toString()
                    if (day<10)
                    {
                        binding.btnDeadline.text =
                            year.toString() + "-0" + (month + 1).toString() + "-0" + day.toString()
                    }
                }
                else
                { binding.btnDeadline.text =
                    year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                    if (day<10)
                    {
                        binding.btnDeadline.text =
                            year.toString() + "-" + (month + 1).toString() + "-0" + day.toString()
                    }
                }
            }, year, month, day)
            datePickerDialog.show()
        }

        //활동 시작기간 설정
        binding.btnStartDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                if (month<10)
                { binding.btnStartDay.text =
                    year.toString() + "-0" + (month + 1).toString() + "-" + day.toString()
                    if (day<10)
                    {
                        binding.btnStartDay.text =
                            year.toString() + "-0" + (month + 1).toString() + "-0" + day.toString()
                    }
                }
                else
                { binding.btnStartDay.text =
                    year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                    if (day<10)
                    {
                        binding.btnStartDay.text =
                            year.toString() + "-" + (month + 1).toString() + "-0" + day.toString()
                    }
                }
                startTime= Calendar.getInstance()
                startTime.apply { set(year, month, day) }
            }, year, month, day).apply {
            }
            datePickerDialog.show()
        }

        // 활동 끝나는기간 설정
        binding.btnFinishDay.setOnClickListener {
            if (binding.btnStartDay.text.toString() != "+") {
                val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                    if (month<10)
                    { binding.btnFinishDay.text =
                        year.toString() + "-0" + (month + 1).toString() + "-" + day.toString()
                        if (day<10)
                        {
                            binding.btnFinishDay.text =
                                year.toString() + "-0" + (month + 1).toString() + "-0" + day.toString()
                        }
                    }
                    else
                    { binding.btnFinishDay.text =
                        year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                        if (day<10)
                        {
                            binding.btnFinishDay.text =
                                year.toString() + "-" + (month + 1).toString() + "-0" + day.toString()
                        }
                    }
                }, year, month, day).apply {
                    datePicker.minDate = startTime.timeInMillis
                }
                datePickerDialog.show()
            } else {
                Toast.makeText(this, "시작날짜부터 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
        setupSinner()
        with(binding)
        {
            //spinner
            spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    interest_idx = position + 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    memberLimit_content = "${spinnerPeople.getItemAtPosition(position)}".toInt()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    applicationMethod_idx = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerAttendTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    attendanceVaildTime_content =
                        "${spinnerAttendTime.getItemAtPosition(position)}".toInt()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            //on,offline 장소선택
            val listener_online = CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                with(binding) {
                    if (isChecked)
                        when (checkbox.id) {
                            R.id.btn_online -> {
                                btnOnline.isChecked = true
                                btnOffline.isChecked = false
                                tvStudyRegion.visibility = View.GONE
                                layoutCreateRegionBtns.visibility = View.GONE
                                lineRegion.visibility = View.GONE
                                online_idx = 1
                            }
                            R.id.btn_offline -> {
                                btnOnline.isChecked = false
                                btnOffline.isChecked = true
                                tvStudyRegion.visibility = View.VISIBLE
                                binding.layoutCreateRegionBtns.visibility = View.VISIBLE
                                lineRegion.visibility = View.VISIBLE
                                online_idx = 0
                            }
                        }
                }
            }
            binding.btnOnline.setOnCheckedChangeListener(listener_online)
            binding.btnOffline.setOnCheckedChangeListener(listener_online)

            //시간 선택
            val listener = CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                with(binding) {
                    if (isChecked)
                        when (checkbox.id) {
                            R.id.check_week -> {
                                etStudyFree.setText("")
                                etStudyMonth.setText("")
                                checkMonth.isChecked = false
                                checkFree.isChecked = false
                                etStudyWeek.isEnabled = true
                                etStudyMonth.isEnabled = false
                                etStudyFree.isEnabled = false
                                meet_idx = 0
                            }
                            R.id.check_month -> {
                                etStudyWeek.setText("")
                                etStudyFree.setText("")
                                checkWeek.isChecked = false
                                checkFree.isChecked = false
                                etStudyMonth.isEnabled = true
                                etStudyWeek.isEnabled = false
                                etStudyFree.isEnabled = false
                                meet_idx = 1
                            }
                            R.id.check_free -> {
                                etStudyWeek.setText("")
                                etStudyMonth.setText("")
                                checkMonth.isChecked = false
                                checkWeek.isChecked = false
                                etStudyFree.isEnabled = true
                                etStudyWeek.isEnabled = false
                                etStudyMonth.isEnabled = false
                                meet_idx = 2
                                periods_content = etStudyFree.text.toString()
                            }
                        }
                }
            }
            binding.checkWeek.setOnCheckedChangeListener(listener)
            binding.checkMonth.setOnCheckedChangeListener(listener)
            binding.checkFree.setOnCheckedChangeListener(listener)
        }

        with(binding)
        {
            btnStudyModify.setOnClickListener {
                Log.e("title값","${title}")
                modifyStudy(title)
            }
        }
    }

    fun initData()
    {
        (intent.hasExtra("groupIdx")).let { groupIdx = intent.getIntExtra("groupIdx", 0) }
        Log.e("summer","groupIdx = ${groupIdx}")
    }

    // 본래 스터디 정보 가져오기 retrofit 함수
    fun initView(groupIdx : Int)
    {
        with(binding)
        {
            etStudyTitle.setText("알고리즘 스터디입니다")
            title = etStudyTitle.text.toString()
        }
    }

    //스터디 수정 값들 가져와서 전송 retrofit 함수
    fun modifyStudy(title:String)
    {
        Log.e("title","${title}")
    }
        fun setupSinner(){
            val interest_spinner = binding.spinnerCategory
            val memberLimit_spinner = binding.spinnerPeople
            val applicationMethod_spinner = binding.spinnerMethod
            val attendanceVaildTime_spinner = binding.spinnerAttendTime

            interest_spinner.adapter = ArrayAdapter.createFromResource(
                this.applicationContext,R.array.intersting,R.layout.item_create_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
            }

            applicationMethod_spinner.adapter = ArrayAdapter.createFromResource(
                this.applicationContext,
                R.array.methodList,
                R.layout.item_create_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
            }
            attendanceVaildTime_spinner.adapter = ArrayAdapter.createFromResource(
                this.applicationContext,
                R.array.attendTimeList,
                R.layout.item_create_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
            }
            memberLimit_spinner.adapter = ArrayAdapter.createFromResource(
                this.applicationContext,
                R.array.peopleList,
                R.layout.item_create_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
            }
        }
        fun hideKeyboard(editText: EditText){
            val  mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(
                editText.getWindowToken(),
                0
            )
        }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_basic_toolbar_back -> finish()
        }
    }
}