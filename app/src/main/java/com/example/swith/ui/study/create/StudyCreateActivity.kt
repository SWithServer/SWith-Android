package com.example.swith.ui.study.create

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.SwithApplication.Companion.spfManager
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyImageReq
import com.example.swith.data.StudyImageRes
import com.example.swith.data.StudyResponse
import com.example.swith.databinding.ActivityStudyCreateBinding
import com.example.swith.databinding.DialogCreateBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.utils.SharedPrefManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
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

    //val userid = SharedPrefManager(this@StudyCreateActivity).getLoginData()
    private val userIdx = spfManager.getLoginData()?.userIdx
    private var meet_idx = -1
    private var frequency_content:Int?=null
    private var periods_content:String?=null
    private var online_idx:Int = -1 //오프라인 0, 온라인 1
    private var regionIdx1:String?=null
    private var regionIdx2:String?=null

    // spinner 선택되는 값들 매칭
    var interest_idx=-1
    var memberLimit_content=-1
    var applicationMethod_idx =-1
    var attendanceVaildTime_content=-1

    var ImgUri:String?=null
    var path:String?= ""
    var file =File("")

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_study_create)
        binding.clickListener = this@StudyCreateActivity

        setUpSpinner()
        initStudyDate()
        initEditText()
        initListener()

        getSharedPreferences("result1",0).apply{
            if(this!=null) {
                val editor1 = this.edit()
                editor1.clear()
                editor1.apply()
            } }
        getSharedPreferences("result2",0).apply{
            if(this!=null){
                val editor2 =this.edit()
                editor2.clear()
                editor2.apply()
            }
        }

        //스터디 개설버튼
        with(binding) {
            btnStudyCreate.setOnClickListener {
                    when (meet_idx) {
                        0 -> {
                            if (!etStudyWeek.text.isNullOrBlank())
                                frequency_content = etStudyWeek.text.toString().toIntOrNull()
                        }
                        1 -> {
                            if (!etStudyMonth.text.isNullOrBlank())
                                frequency_content = etStudyMonth.text.toString().toIntOrNull()
                        }
                        2 -> {
                            if (!etStudyFree.text.isNullOrBlank())
                                periods_content = etStudyFree.text.toString()
                        }
                    }

                    var studyRequestData = StudyGroup(
                        1,
                        ImgUri,
                        etStudyTitle.text.toString(),
                        meet_idx,
                        frequency_content,
                        periods_content,
                        online_idx,
                        regionIdx1,
                        regionIdx2,
                        interest_idx,
                        etCreateTopic.text.toString(),
                        memberLimit_content,
                        applicationMethod_idx,
                        tvDeadline.text.toString(),
                        tvStartDay.text.toString(),
                        tvFinishDay.text.toString(),
                        attendanceVaildTime_content,
                        etStudyContent.text.toString()
                    )
                    Log.e("summer", "USER DATA = ${studyRequestData.toString()}")

                    //Toast Message 설정
                    if (etStudyTitle.text.equals("") || meet_idx == -1 || online_idx == -1 || interest_idx == -1 || etCreateTopic.text.equals(
                            ""
                        ) || tvDeadline.text.equals("+") || tvStartDay.text.equals("+") || tvFinishDay.text.equals(
                            "+"
                        ) || attendanceVaildTime_content == -1 || etStudyContent.text.equals("")
                    ) {
                        Toast.makeText(
                            this@StudyCreateActivity,
                            "모든 항목을 작성해주세요!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        when (meet_idx) {
                            0, 1 -> {
                                if (online_idx == 0 && regionIdx1 == null && regionIdx2 == null) {
                                    Toast.makeText(
                                        this@StudyCreateActivity,
                                        "모든 항목을 작성해주세요!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (frequency_content == null) {
                                    Toast.makeText(
                                        this@StudyCreateActivity,
                                        "모든 항목을 작성해주세요!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    saveDialog(studyRequestData)
                                }
                            }
                            2 -> {
                                if (online_idx == 0 && (regionIdx1 == null && regionIdx2 == null)) {
                                    Toast.makeText(
                                        this@StudyCreateActivity,
                                        "모든 항목을 작성해주세요!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (periods_content == null) {
                                    Toast.makeText(
                                        this@StudyCreateActivity,
                                        "모든 항목을 작성해주세요!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    saveDialog(studyRequestData)
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
            if(!(shPref1.getString("이름1","").equals("")) && !(shPref1.getString("이름1","").equals("+")))
            {Log.e("진입됨","true")
                regionIdx1 =shPref1.getString("이름1", "").toString()
                binding.btnPlusPlace1.background = ContextCompat.getDrawable(this,R.drawable.bg_create_select_blue) }
            else{
                regionIdx1 =null
                binding.btnPlusPlace1.background = ContextCompat.getDrawable(this,R.drawable.bg_create_skyblue)
            }
        }
        if(!getSharedPreferences("result2",0).getString("이름2","").toString().equals("")){
            val shPref2 = getSharedPreferences("result2",0)
            val editor2 = getSharedPreferences("result2",0).edit()
            binding.btnPlusPlace2.text = shPref2.getString("이름2", "")
            if(!(shPref2.getString("이름2","").equals("")) && !(shPref2.getString("이름2","").equals("+")))
            {Log.e("진입됨","true")
                regionIdx2 =shPref2.getString("이름2", "").toString()
                binding.btnPlusPlace2.background = ContextCompat.getDrawable(this,R.drawable.bg_create_select_blue) }
            else{
                regionIdx2 =null
                binding.btnPlusPlace2.background = ContextCompat.getDrawable(this,R.drawable.bg_create_skyblue)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY)
        {
            if(resultCode == RESULT_OK)
            {
                var currentImageUri = data?.data
                Log.e("현재 이미지 url", "${currentImageUri}")
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
                        path= getRealPathFromURI(currentImageUri)
                        Log.e("ImgUri 값","${ImgUri}")
                        Log.e("path 값","${path}")
                        file = File(path)
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

    private fun initStudyDate(){
        // 모집 마감기간 설정
        binding.btnDeadline.setOnClickListener {
            //팝업 달력
            val datePickerDialog = DatePickerDialog(this@StudyCreateActivity, R.style.DatePickerTheme, { _, year, month, day ->
                if (month<10)
                { binding.tvDeadline.text =
                    year.toString() + "-0" + (month + 1).toString() + "-" + day.toString()
                    if (day<10)
                    {
                        binding.tvDeadline.text =
                            year.toString() + "-0" + (month + 1).toString() + "-0" + day.toString()
                    }
                }
                else
                { binding.tvDeadline.text =
                    year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                    if (day<10)
                    {
                        binding.tvDeadline.text =
                            year.toString() + "-" + (month + 1).toString() + "-0" + day.toString()
                    }
                }
            }, year, month, day).apply{
                datePicker.minDate= System.currentTimeMillis() - 1000;
            }
            datePickerDialog.show()

        }

        //활동 시작기간 설정
        binding.btnStartDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@StudyCreateActivity, R.style.DatePickerTheme, { _, year, month, day ->
                if (month<10)
                { binding.tvStartDay.text =
                    year.toString() + "-0" + (month + 1).toString() + "-" + day.toString()
                    if (day<10)
                    {
                        binding.tvStartDay.text =
                            year.toString() + "-0" + (month + 1).toString() + "-0" + day.toString()
                    }
                }
                else
                { binding.tvStartDay.text =
                    year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                    if (day<10)
                    {
                        binding.tvStartDay.text =
                            year.toString() + "-" + (month + 1).toString() + "-0" + day.toString()
                    }
                }
                startTime=Calendar.getInstance()
                startTime.apply { set(year, month, day) }
            }, year, month, day).apply {
                datePicker.minDate= System.currentTimeMillis() - 1000;
            }
            datePickerDialog.show()
        }

        // 활동 끝나는기간 설정
        binding.btnFinishDay.setOnClickListener {
            if (binding.tvStartDay.text.toString() != "시작 날짜") {
                val datePickerDialog = DatePickerDialog(this@StudyCreateActivity, R.style.DatePickerTheme,{ _, year, month, day ->
                    if (month<10)
                    { binding.tvFinishDay.text =
                        year.toString() + "-0" + (month + 1).toString() + "-" + day.toString()
                        if (day<10)
                        {
                            binding.tvFinishDay.text =
                                year.toString() + "-0" + (month + 1).toString() + "-0" + day.toString()
                        }
                    }
                    else
                    { binding.tvFinishDay.text =
                        year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                        if (day<10)
                        {
                            binding.tvFinishDay.text =
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
    }

    private fun initEditText(){
        with(binding)
        {
            etStudyContent.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyContent.text.equals("")){
                    hideKeyboard(etStudyContent)
                    true
                }
                else{
                    false
                }
            }
            etCreateTopic.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etCreateTopic.text.equals("")){
                    hideKeyboard(etCreateTopic)
                    true
                }
                else{
                    false
                }
            }
            etStudyTitle.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyTitle.text.equals("")){
                    hideKeyboard(etStudyTitle)
                    true
                }
                else{
                    false
                }
            }
            etStudyWeek.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyWeek.text.equals("")){
                    hideKeyboard(etStudyWeek)
                    true
                }
                else{
                    false
                }
            }
            etStudyMonth.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyMonth.text.equals("")){
                    hideKeyboard(etStudyMonth)
                    true
                }
                else{
                    false
                }
            }
            etStudyFree.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyFree.text.equals("")){
                    hideKeyboard(etStudyFree)
                    true
                }
                else{
                    false
                }
            }
        }
    }

    private fun initListener(){
        with(binding)
        {
            //button
            btnImage.setOnClickListener {
                imageView = binding.ivStudyCreate
                openGallery()
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
            spinnerAttendTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    attendanceVaildTime_content = "${spinnerAttendTime.getItemAtPosition(position)}".toInt()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            btnApplicationApply.setOnCheckedChangeListener(
                CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                    if (isChecked) {
                        when (checkbox.id) {
                            R.id.btn_application_ff -> {
                                btnApplicationApply.isChecked = false
                                btnApplicationFf.isChecked = true
                                applicationMethod_idx = 0 //선착순
                            }
                            R.id.btn_application_apply -> {
                                btnApplicationFf.isChecked = false
                                btnApplicationApply.isChecked = true
                                applicationMethod_idx = 1 // 지원
                            }
                        }
                    }
                })

            btnApplicationFf.setOnCheckedChangeListener(
                CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                    if (isChecked) {
                        when (checkbox.id) {
                            R.id.btn_application_ff -> {
                                btnApplicationApply.isChecked = false
                                btnApplicationFf.isChecked = true
                                applicationMethod_idx = 0 //선착순
                            }
                            R.id.btn_application_apply -> {
                                btnApplicationFf.isChecked = false
                                btnApplicationApply.isChecked = true
                                applicationMethod_idx = 1 // 지원
                            }
                        }
                    }
                })

                //on,offline 장소선택
                val listener_online =
                    CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                        if (isChecked)
                            when (checkbox.id) {
                                R.id.btn_online -> {
                                    btnOnline.isChecked = true
                                    btnOffline.isChecked = false
                                    tvStudyRegion.visibility = View.GONE
                                    layoutCreateRegionBtns.visibility = View.GONE
                                    lineRegion.visibility = View.GONE
                                    online_idx = 1
                                    regionIdx1=null
                                    regionIdx2=null
                                    btnPlusPlace1.text="+"
                                    btnPlusPlace2.text="+"
                                    binding.btnPlusPlace1.background = ContextCompat.getDrawable(this@StudyCreateActivity,R.drawable.bg_create_skyblue)
                                    binding.btnPlusPlace2.background = ContextCompat.getDrawable(this@StudyCreateActivity,R.drawable.bg_create_skyblue)

                                    val pref1 = getSharedPreferences("result1", MODE_PRIVATE)
                                    val editor1 = pref1.edit()
                                    editor1.remove("이름1")
                                    editor1.commit()
                                    val pref2 = getSharedPreferences("result2", MODE_PRIVATE)
                                    val editor2 = pref2.edit()
                                    editor2.remove("이름2")
                                    editor2.commit()
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
                btnOnline.setOnCheckedChangeListener(listener_online)
                btnOffline.setOnCheckedChangeListener(listener_online)

                //시간 선택
                val listener = CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                    if (isChecked)
                        when (checkbox.id) {
                            R.id.check_week -> {
                                toggleTime(etStudyFree,etStudyMonth,tvStudyWeek,studyWeekTv2,tvStudyMonth,studyMonthTv2,checkMonth,checkFree)
                                etStudyWeek.isEnabled = true
                                etStudyMonth.isEnabled = false
                                etStudyFree.isEnabled = false
                                meet_idx = 0
                            }
                            R.id.check_month -> {
                                toggleTime(etStudyFree,etStudyWeek,tvStudyMonth,studyMonthTv2,tvStudyWeek,studyWeekTv2,checkWeek,checkFree)
                                etStudyMonth.isEnabled = true
                                etStudyWeek.isEnabled = false
                                etStudyFree.isEnabled = false
                                meet_idx = 1
                            }
                            R.id.check_free -> {
                                toggleTime(etStudyWeek,etStudyMonth,null,null,null,null,checkMonth,checkWeek)
                                etStudyFree.isEnabled = true
                                etStudyWeek.isEnabled = false
                                etStudyMonth.isEnabled = false
                                tvStudyMonth.setTextColor(Color.parseColor("#9F9F9F"))
                                studyMonthTv2.setTextColor(Color.parseColor("#9F9F9F"))
                                tvStudyWeek.setTextColor(Color.parseColor("#9F9F9F"))
                                studyWeekTv2.setTextColor(Color.parseColor("#9F9F9F"))
                                meet_idx = 2
                                periods_content = etStudyFree.text.toString()
                            }
                        }
                }
                checkWeek.setOnCheckedChangeListener(listener)
                checkMonth.setOnCheckedChangeListener(listener)
                checkFree.setOnCheckedChangeListener(listener)
        }

    }

    private fun setUpSpinner(){
        binding.spinnerCategory.adapter = ArrayAdapter.createFromResource(
            this.applicationContext,R.array.intersting,R.layout.item_create_spinner
        ).apply{
            this.setDropDownViewResource(R.layout.item_create_spinner_dropdown)
        }
        binding.spinnerPeople.adapter = ArrayAdapter.createFromResource(
            this.applicationContext,
            R.array.peopleList,
            R.layout.item_create_spinner
        ).apply{
            this.setDropDownViewResource(R.layout.item_create_spinner_dropdown)
        }
        binding.spinnerAttendTime.adapter = ArrayAdapter.createFromResource(
            this.applicationContext,
            R.array.attendTimeList,
            R.layout.item_create_spinner
        ).apply{
            this.setDropDownViewResource(R.layout.item_create_spinner_dropdown)
        }



    }

    private fun toggleTime(emptyText1: EditText,emptyText2:EditText,textChecked1: TextView?,textChecked2:TextView?,textNChecked1:TextView?,textNChecked2:TextView?,check1:CheckBox,check2:CheckBox){
        emptyText1.setText("")
        emptyText2.setText("")
        textChecked1?.setTextColor(Color.parseColor("#525252"))
        textChecked2?.setTextColor(Color.parseColor("#525252"))
        textNChecked1?.setTextColor(Color.parseColor("#9F9F9F"))
        textNChecked2?.setTextColor(Color.parseColor("#9F9F9F"))
        check1.isChecked=false
        check2.isChecked=false
    }

    private fun saveDialog(studyRequestData : StudyGroup) {
        Log.e("summer", "saveDialog 함수")
        with(binding)
        {
            DataBindingUtil.inflate<DialogCreateBinding>(
                LayoutInflater.from(this@StudyCreateActivity),
                R.layout.dialog_create, null, false
            ).apply {
                this.createDialog = CustomDialog(
                    this@StudyCreateActivity,
                    root,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                ).apply {
                    this.setClickListener(object : CustomDialog.DialogClickListener {
                        override fun onConfirm() {
                            Log.e("summer", "save dialog onConfirm()")
                            uploadImage(file, studyRequestData)
                        }
                        override fun onClose() {
                            Log.e("summer", "save dialog onClose()")
                        }
                    })
                    show()
                }
            }
        }
    }
    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        val uri = Uri.fromFile(File(path))
        cursor.close()
        return path
    }
    private fun openGallery(){
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent,GALLERY)
    }
    private fun uploadImage(file:File,studyRequestData: StudyGroup) {
        Log.e("업로드 함수","진입")
        if (!(file.name.equals("")))
        {
            var requestFile =  RequestBody.create("image"?.toMediaTypeOrNull(), file)
            var body  = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
            retrofitService.uploadImg(body).enqueue(object :
                Callback<StudyImageRes> {
                override fun onResponse(
                    call: Call<StudyImageRes>,
                    response: Response<StudyImageRes>
                ) {
                    if (response.isSuccessful) {
                        Log.e("summer", "성공${response.toString()}")
                        response.body()?.apply {
                            Log.e("summer 결과값","${this.imageUrls}")
                            ImgUri = this.imageUrls[0]
                            Log.e("Img Uri 값 변경한 부분","${ImgUri}")
                            studyRequestData.groupImgUrl=ImgUri
                            postStudy(studyRequestData)

                        }
                    }
                    else {
                        Log.e("summer", "전달실패 code = ${response.code()}")
                        Log.e("summer", "전달실패 msg = ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<StudyImageRes>, t: Throwable) {
                    Log.e("summer", "onFailure t = ${t.toString()}")
                    Log.e("summer", "onFailure msg = ${t.message}")
                }
            })
        }
        else
        {
            postStudy(studyRequestData)
        }
    }
    private fun postStudy(studyRequestData: StudyGroup) {
        Log.e("StudyReq 최종", "${studyRequestData.toString()}")
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.createStudy(studyRequestData).enqueue(object : Callback <StudyResponse> {
            override fun onResponse(call: Call<StudyResponse>, response: Response<StudyResponse>) {
                if (response.isSuccessful)
                {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply{
                        val studyResp = this as StudyResponse
                        Log.e("summer","body = $studyResp")
                    }
                    setResult(RESULT_OK)
                    finish()
                }
                else
                {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                    Toast.makeText(this@StudyCreateActivity,"다시 시도해주세요",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<StudyResponse>, t: Throwable) {
                Log.e("summer","onFailure t = ${t.toString()}")
                Log.e("summer","onFailure msg = ${t.message}")
                Toast.makeText(this@StudyCreateActivity,"다시 시도해주세요",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun hideKeyboard(editText: EditText){
        val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(
            editText.getWindowToken(),
            0
        )
    }
}
