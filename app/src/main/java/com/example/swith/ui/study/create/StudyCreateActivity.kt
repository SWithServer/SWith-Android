package com.example.swith.ui.study.create

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyResponse
import com.example.swith.databinding.ActivityStudyCreateBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter
import java.util.*


class StudyCreateActivity :AppCompatActivity(),View.OnClickListener {
    lateinit var binding: ActivityStudyCreateBinding

    private val GALLERY=1
    private var imageView: ImageView? = null

    private lateinit var startTime: Calendar //활동 시작기간
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    //입력되는 값들 변수모음
    var title:String=""
//    val userid= SharedPrefManager(this@StudyCreateActivity).getLoginData()
//    val userIdx = userid?.userIdx
    var meet_idx:Int= -1
    var frequency_content:Int?=null
    var periods_content:String?=null
    // 오프라인 0 온라인 1
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
     var recruitmentEndDate_ : String = ""
     var groupStart_ : String =""
    var groupEnd_ : String =""

    var placeNum: String="-1"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    lateinit var dialog_ :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_study_create)
        binding.clickListener = this@StudyCreateActivity


        dialog_ = Dialog(this@StudyCreateActivity)
        dialog_.setContentView(R.layout.fragment_dialog)

        Log.e("create","true")
        getSharedPreferences("result1",0).apply{
            if(this!=null) {
                val shPref1 = this
                val editor1 = this.edit()
                editor1.clear()
                editor1.apply()
            } }
        getSharedPreferences("result2",0).apply{
            if(this!=null){
                val shPref2 = this
                val editor2 =this.edit()
                editor2.clear()
                editor2.apply()
            }
        }

        val imageBtn: Button = binding.btnImage
        imageBtn.setOnClickListener {
            //스터디 개설 이미지뷰 갤러리 연동
            imageView = binding.ivStudyCreate
            openGallery()
        }

        //btn_onClickListener들
        with(binding)
        {
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
                var intent = Intent(this@StudyCreateActivity,SelectPlaceActivity::class.java)
                intent.putExtra("번호",1)
                startActivity(intent)
            }
            btnPlusPlace2.setOnClickListener {
                var intent = Intent(this@StudyCreateActivity,SelectPlaceActivity::class.java)
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

        setupSinner()
        with(binding)
        {
            //spinner
            spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    interest_idx=position+1
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    memberLimit_content = "${spinnerPeople.getItemAtPosition(position)}".toInt()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerMethod.onItemSelectedListener= object:AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    applicationMethod_idx = position
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerAttendTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    attendanceVaildTime_content = "${spinnerAttendTime.getItemAtPosition(position)}".toInt()
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
                                tvStudyRegion.visibility=View.GONE
                                layoutCreateRegionBtns.visibility=View.GONE
                                lineRegion.visibility=View.GONE
                                online_idx = 1
                            }
                            R.id.btn_offline -> {
                                btnOnline.isChecked = false
                                btnOffline.isChecked =true
                                tvStudyRegion.visibility=View.VISIBLE
                                binding.layoutCreateRegionBtns.visibility = View.VISIBLE
                                lineRegion.visibility=View.VISIBLE
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
                            meet_idx=0
                        }
                        R.id.check_month -> {
                            etStudyWeek.setText("")
                            etStudyFree.setText("")
                            checkWeek.isChecked = false
                            checkFree.isChecked = false
                            etStudyMonth.isEnabled = true
                            etStudyWeek.isEnabled = false
                            etStudyFree.isEnabled = false
                            meet_idx=1
                        }
                        R.id.check_free -> {
                            etStudyWeek.setText("")
                            etStudyMonth.setText("")
                            checkMonth.isChecked = false
                            checkWeek.isChecked = false
                            etStudyFree.isEnabled = true
                            etStudyWeek.isEnabled = false
                            etStudyMonth.isEnabled = false
                            meet_idx=2
                            periods_content=etStudyFree.text.toString()
                        }
                    }
            }
        }
        binding.checkWeek.setOnCheckedChangeListener(listener)
        binding.checkMonth.setOnCheckedChangeListener(listener)
        binding.checkFree.setOnCheckedChangeListener(listener)

        //스터디 개설버튼
        binding.btnStudyCreate.setOnClickListener {
            with(binding)
            { recruitmentEndDate_ = btnDeadline.text.toString()
                groupStart_ = btnStartDay.text.toString()
                groupEnd_ = btnFinishDay.text.toString()
                when(meet_idx) {
                0->{if (!etStudyWeek.text.isNullOrBlank())
                    frequency_content=etStudyWeek.text.toString().toIntOrNull()
                    }
                1->{if (!etStudyMonth.text.isNullOrBlank())
                frequency_content=etStudyMonth.text.toString().toIntOrNull()}
                2->{if (!etStudyFree.text.isNullOrBlank())
                periods_content=etStudyFree.text.toString()}
                }

                var studyRequestData=StudyGroup(1,"2",title,meet_idx,frequency_content,periods_content,online_idx,regionIdx1,regionIdx2,interest_idx
                    ,topic_content,memberLimit_content,applicationMethod_idx,recruitmentEndDate_,groupStart_,groupEnd_
                    ,attendanceVaildTime_content,group_content)
                Log.e("summer", "USER DATA = ${studyRequestData.toString()}")

                //Toast Message 설정
                if (title.equals("")||meet_idx==-1||online_idx==-1||interest_idx==-1||topic_content.equals("")||recruitmentEndDate_.equals("+") ||groupStart_.equals("+") ||groupEnd_.equals("+") || attendanceVaildTime_content==-1 ||group_content.equals(""))
                {
                    Toast.makeText(this@StudyCreateActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                }
                else{
                    when(meet_idx)
                    {
                        0,1->{
                            if (online_idx==0 && regionIdx1==null&&regionIdx2==null)
                            {
                                Toast.makeText(this@StudyCreateActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                            }
                            else if (frequency_content==null)
                            {
                                Toast.makeText(this@StudyCreateActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                createStudy(studyRequestData,"개설하시겠습니까?")
                            }
                        }
                        2->{
                            if (online_idx==0 && (regionIdx1==null&&regionIdx2==null))
                            {
                                Toast.makeText(this@StudyCreateActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                            }
                            else if (periods_content==null)
                            {
                                Toast.makeText(this@StudyCreateActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()

                            }
                            else{
                                createStudy(studyRequestData,"개설하시겠습니까?")
                            }
                        }
                    }
                }
            }
        }

        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("resume","true")
        if(!getSharedPreferences("result1",0).getString("이름1","").toString().equals("")) {
           val shPref1 = getSharedPreferences("result1",0)
            val editor1 = getSharedPreferences("result1",0).edit()
            binding.btnPlusPlace1.text = shPref1.getString("이름1", "")
            if(!shPref1.getString("코드1","").toString().equals(""))
            {regionIdx1 =shPref1.getString("코드1", "").toString().toLong()}
        }
        if(!getSharedPreferences("result2",0).getString("이름2","").toString().equals("")){
            val shPref2 = getSharedPreferences("result2",0)
            val editor2 = getSharedPreferences("result2",0).edit()
            binding.btnPlusPlace2.text = shPref2.getString("이름2", "")
            if(!shPref2.getString("코드2","").toString().equals(""))
            {regionIdx2 = shPref2.getString("코드2", "").toString().toLong()}
        }
    }
    fun createStudy(studyRequestData : StudyGroup,content_text:String){
        //레트로핏 부분
        dialog_.findViewById<TextView>(R.id.tv_confirm).text = content_text
        dialog_.show()

        dialog_.findViewById<Button>(R.id.btn_no).setOnClickListener {
            dialog_.dismiss()
        }
        dialog_.findViewById<Button>(R.id.btn_yes).setOnClickListener {
            Log.e("summer","retrofit 함수 in")
            Log.e("summer", "USER DATA = ${studyRequestData.toString()}")
            val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
            retrofitService.createStudy(studyRequestData).enqueue(object : Callback <StudyResponse> {
                override fun onResponse(call: Call<StudyResponse>, response: Response<StudyResponse>) {
                    if (response.isSuccessful)
                    {
                        Log.e("summer", "성공${response.toString()}")
                        response.body()?.apply {
                            val studyResp = this as StudyResponse
                            Log.e("summer","body = $studyResp")
                        }
                        finish()
                    }
                    else
                    {
                        Log.e("summer", "전달실패 code = ${response.code()}")
                        Log.e("summer", "전달실패 msg = ${response.message()}")
                        Toast.makeText(this@StudyCreateActivity,"다시 시도해주세요",Toast.LENGTH_SHORT).show()
                    dialog_.dismiss()
                    }
                }
                override fun onFailure(call: Call<StudyResponse>, t: Throwable) {
                    Log.e("summer","onFailure t = ${t.toString()}")
                    Log.e("summer","onFailure msg = ${t.message}")
                    Toast.makeText(this@StudyCreateActivity,"다시 시도해주세요",Toast.LENGTH_SHORT).show()
                    dialog_.dismiss()
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
        val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(
            editText.getWindowToken(),
            0
        )
    }
}
