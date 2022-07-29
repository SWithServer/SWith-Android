package com.example.swith.ui.study.create

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyResponse
import com.example.swith.databinding.ActivityStudyCreateBinding
import com.example.swith.repository.RetrofitService
import com.example.swith.repository.StudyCreateRetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter
import java.util.*


class StudyCreateActivity :AppCompatActivity() {
    lateinit var binding: ActivityStudyCreateBinding

    private val GALLERY=1
    private var imageView: ImageView? = null

    private lateinit var startTime: Calendar //활동 시작기간
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    //입력되는 값들 변수모음
    var title:String=""

//    val userid=SharedPrefManager(this@StudyCreateActivity).getLoginData()
//    val userIdx = userid?.userIdx

    var meet_idx:Int= -1
    var frequency_content:Int?=null
    var periods_content:String?=null
    var online_idx:Int = -1
    var topic_content:String =""

    var regionIdx1:Long?=null
    var regionIdx2:Long?=null

    var group_content:String=""

    // spinner 선택되는 값들 매칭
    var interest_idx:Int = -1
    var memberLimit_content:Int=-1
    var applicationMethod_idx : Int=-1
    var attendanceVaildTime_content : Int=-1

    // 날짜 입력값들
    lateinit var recruitmentEndDate_ : String
    lateinit var groupStart_ : String
    lateinit var groupEnd_ : String

    lateinit var prefs1:SharedPreferences
    lateinit var prefs2:SharedPreferences


    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_study_create)

        val imageBtn: Button = binding.btnImage
        imageBtn.setOnClickListener {
            //스터디 개설 이미지뷰 갤러리 연동
            imageView = binding.ivStudyCreate
            openGallery()
        }

        //btn_onClickListener들
        with(binding)
        {
            btnPlusPlace1.setOnClickListener {
               var intent = Intent(this@StudyCreateActivity, SelectPlaceActivity::class.java)
            //    intent.putExtra("번호",1)
                startActivity(intent)

                //미완성
//                prefs1=getSharedPreferences("result1",0)
//                val name1 = prefs1.getString("이름"," ")
//                btnPlusPlace1.setText("${name1}")
//                regionIdx1 = prefs1.getString("코드","")?.toLong()
                }
            btnPlusPlace2.setOnClickListener {
                var intent_ = Intent(this@StudyCreateActivity, SelectPlaceActivity::class.java)
           //     intent_.putExtra("번호",2)
                startActivity(intent_)

                //미완성
//                prefs2=getSharedPreferences("result2",0)
//                val name2 = prefs2.getString("이름"," ")
//                btnPlusPlace2.setText("${name2}")
//                regionIdx1 = prefs2.getString("코드","")?.toLong()
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
                startTime=Calendar.getInstance()
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

        //spinner
        val interest_spinner = binding.spinnerCategory
        interest_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.categoryList,
            android.R.layout.simple_spinner_item
        )
        val memberLimit_spinner = binding.spinnerPeople
        memberLimit_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.peopleList,
            android.R.layout.simple_spinner_item
        )
        val applicationMethod_spinner = binding.spinnerMethod
        applicationMethod_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.methodList,
            android.R.layout.simple_spinner_item
        )
        val attendanceVaildTime_spinner = binding.spinnerAttendTime
        attendanceVaildTime_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.attendTimeList,
            android.R.layout.simple_spinner_item
        )

        with(binding)
        {
            interest_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    interest_idx=position
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            memberLimit_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    memberLimit_content = "${memberLimit_spinner.getItemAtPosition(position)}".toInt()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            applicationMethod_spinner.onItemSelectedListener= object:AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    applicationMethod_idx = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            interest_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    interest_idx=position+1
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            attendanceVaildTime_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    attendanceVaildTime_content = "${attendanceVaildTime_spinner.getItemAtPosition(position)}".toInt()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

        //on,offline 장소선택
        binding.placeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_offline -> {binding.studyCreateOfflineLayout.visibility = View.VISIBLE
                online_idx = 0}
                R.id.btn_online -> {binding.studyCreateOfflineLayout.visibility = View.GONE
                online_idx =1}
            }
        }

        //시간 선택
        val listener = CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
            with(binding) {
                if (isChecked)
                    when (checkbox.id) {
                        R.id.check_week -> {
                            tvStudyFree.setText("")
                            tvStudyMonth.setText("")
                            checkMonth.isChecked = false
                            checkFree.isChecked = false
                            tvStudyWeek.isEnabled = true
                            tvStudyMonth.isEnabled = false
                            tvStudyFree.isEnabled = false
                            meet_idx=0
                        }
                        R.id.check_month -> {
                            tvStudyWeek.setText("")
                            tvStudyFree.setText("")
                            checkWeek.isChecked = false
                            checkFree.isChecked = false
                            tvStudyMonth.isEnabled = true
                            tvStudyWeek.isEnabled = false
                            tvStudyFree.isEnabled = false
                            meet_idx=1
                        }
                        R.id.check_free -> {
                            tvStudyWeek.setText("")
                            tvStudyMonth.setText("")
                            checkMonth.isChecked = false
                            checkWeek.isChecked = false
                            tvStudyFree.isEnabled = true
                            tvStudyWeek.isEnabled = false
                            tvStudyMonth.isEnabled = false
                            meet_idx=2
                            periods_content=tvStudyFree.text.toString()
                        }
                    }
            }
        }
        binding.checkWeek.setOnCheckedChangeListener(listener)
        binding.checkMonth.setOnCheckedChangeListener(listener)
        binding.checkFree.setOnCheckedChangeListener(listener)


        //스터디 개설버튼
        //retrofit 연결을 위해 잠시 개설확인 dialog를 지운상태.
        binding.btnStudyCreate.setOnClickListener {
            with(binding)
            {   group_content= etStudyContent.text.toString()
                recruitmentEndDate_ = btnDeadline.text.toString()
            //    recruitmentEndDate = LocalDate.parse(recruitmentEndDate_, formatter)
                 groupStart_ = btnStartDay.text.toString()
            //    groupStart = LocalDate.parse(groupStart_,formatter)
                 groupEnd_ = btnFinishDay.text.toString()
              //  groupEnd = LocalDate.parse(groupEnd_, formatter)
                title = etStudyTitle.text.toString()
                topic_content = etCreateTopic.text.toString()
            }
            when(meet_idx)
            {
                0->{frequency_content=binding.tvStudyWeek.text.toString().toInt()}
                1->{frequency_content=binding.tvStudyMonth.text.toString().toInt()}
                2->{periods_content=binding.tvStudyFree.text.toString()}
            }
            //시범 스터디 개설 데이터들
            // userIdx = adminIndex  우선은 기본 숫자값 넣어둠 (SharedPrf 선언은 해둠)
            var studyRequestData=StudyGroup(1,"2",title,meet_idx,frequency_content,periods_content,online_idx,regionIdx1,regionIdx2,interest_idx
                ,topic_content,memberLimit_content,applicationMethod_idx,recruitmentEndDate_,groupStart_,groupEnd_
                ,attendanceVaildTime_content,group_content)
            Log.e("summer", "USER DATA = ${studyRequestData.toString()}")
            //레트로핏 부분
                val retrofitService = RetrofitService.retrofit.create(StudyCreateRetrofitInterface::class.java)
                retrofitService.createStudy(studyRequestData).enqueue(object : Callback <StudyResponse> {
                    override fun onResponse(call: Call<StudyResponse>, response: Response<StudyResponse>) {
                        if (response.isSuccessful)
                        {
                            Log.e("summer", "성공${response.toString()}")
                            response.body()?.apply {
                                val studyResp = this as StudyResponse
                                Log.e("summer","body = $studyResp")
                            }
                        }
                        else
                        {
                            Log.e("summer", "전달실패 code = ${response.code()}")
                            Log.e("summer", "전달실패 msg = ${response.message()}")
                        }
                    }
                    override fun onFailure(call: Call<StudyResponse>, t: Throwable) {
                        Log.e("summer","onFailure t = ${t.toString()}")
                        Log.e("summer","onFailure msg = ${t.message}")
                    }
                })
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY)
        {
            if(resultCode == RESULT_OK)
            {
                var currentImageUri = data?.data
                try{
                    currentImageUri?.let{
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            imageView?.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            imageView?.setImageBitmap(bitmap)
                        }
                    }
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    //갤러리에서 이미지 선택
    private fun openGallery(){
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent,GALLERY)
    }
}
