package com.example.swith.ui.study.create

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.studywith.ConfirmDialog
import com.example.swith.R
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyResponse
import com.example.swith.databinding.ActivityStudyCreateBinding
import com.example.swith.repository.RetrofitService
import com.example.swith.repository.StudyCreateRetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Types.NULL
import java.time.LocalDate
import java.time.LocalDateTime
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

    var meet_idx:Int= -1
    var frequency_content:Int = -1
    var periods_content:String = ""
    var online_idx:Int = -1
    var topic_content:String =""

    var group_content:String=""

    // spinner 선택되는 값들 매칭
    var interest_idx:Int = -1
    var memberLimit_content:Int=-1
    var applicationMethod_idx : Int=-1
    var attendanceVaildTime_content : Int=-1

    // 날짜 입력값들
    lateinit var recruitmentEndDate:LocalDate
    lateinit var groupStart:LocalDate
    lateinit var groupEnd:LocalDate
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intent
        val selectPlace_intent = Intent(this, SelectPlaceActivity::class.java)

        val imageBtn: Button = binding.btnImage
        imageBtn.setOnClickListener {
            //스터디 개설 이미지뷰 갤러리 연동
            imageView = binding.ivStudyCreate
            openGallery()
        }

        //btn_onClickListener들
        //장소선택1
        binding.btnPlusPlace1.setOnClickListener(View.OnClickListener {
            startActivity(selectPlace_intent)
        })
        //장소선택2
        binding.btnPlusPlace2.setOnClickListener(View.OnClickListener {
            startActivity(selectPlace_intent)
        })

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
                    interest_idx=position
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
        //retrofit 연결을 위해 잠시 개설확인 dialog를 지움.
        binding.btnStudyCreate.setOnClickListener {
            with(binding)
            { //group_content= etStudyContent.text.toString()
                val recruitmentEndDate_ = btnDeadline.text.toString()
                recruitmentEndDate = LocalDate.parse(recruitmentEndDate_, formatter)
                val groupStart_ = btnStartDay.text.toString()
                groupStart = LocalDate.parse(groupStart_,formatter)
                val groupEnd_ = btnFinishDay.text.toString()
                groupEnd = LocalDate.parse(groupEnd_, formatter)
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
            var studyRequestData=StudyGroup(1,"2",title,meet_idx,frequency_content,periods_content,online_idx,1,33,interest_idx
                ,topic_content,memberLimit_content,applicationMethod_idx,recruitmentEndDate,groupStart,groupEnd
                ,attendanceVaildTime_content,group_content)

            //레트로핏 부분
                val retrofitService = RetrofitService.retrofit.create(StudyCreateRetrofitInterface::class.java)
                retrofitService.createStudy(studyRequestData).enqueue(object : Callback <StudyResponse> {
                    override fun onResponse(call: Call<StudyResponse>, response: Response<StudyResponse>) {
                        if (response.isSuccessful)
                        {
                            Log.d("전달완", response.toString())
                        }
                        else
                        {
                            Log.d("전달실패-1", "${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<StudyResponse>, t: Throwable) {
                        Log.d("전달실패-2",t.toString())
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
