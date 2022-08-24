package com.example.swith.ui.manage

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.swith.R
import com.example.swith.data.StudyDetailResponse
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyImageRes
import com.example.swith.data.StudyModifyResponse
import com.example.swith.databinding.ActivityManageStudyModifyBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.study.create.SelectPlaceActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class ManageStudyModifyActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivityManageStudyModifyBinding
    var groupIdx : Long = -1

    var title:String=""
//    val userid= SharedPrefManager(this@ManageStudyModifyActivity).getLoginData()
//    val userIdx = userid?.userIdx

    val userIdx = 1

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
    lateinit var dialog_ :Dialog

    private val GALLERY=1
    private var imageView: ImageView? = null
    var ImgUri : String? ="" // 이미지 uri 받아오는 변수
    var path : String? = "" // 파일로 변환할때 필요한 변수
    var file=File("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ManageStudyModifyActivity, R.layout.activity_manage_study_modify)

        initData()
        binding.flLoadingLayout.visibility=View.VISIBLE
        initView(groupIdx)
        customDialog()

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
                    interest_idx = position
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
                if (isChecked)
                    when (checkbox.id) {
                        R.id.check_week -> {
                            etStudyFree.setText("")
                            etStudyMonth.setText("")
                            tvStudyMonth.setTextColor(Color.parseColor("#9F9F9F"))
                            studyMonthTv2.setTextColor(Color.parseColor("#9F9F9F"))
                            tvStudyWeek.setTextColor(Color.parseColor("#525252"))
                            studyWeekTv2.setTextColor(Color.parseColor("#525252"))
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
                            tvStudyMonth.setTextColor(Color.parseColor("#525252"))
                            studyMonthTv2.setTextColor(Color.parseColor("#525252"))
                            tvStudyWeek.setTextColor(Color.parseColor("#9F9F9F"))
                            studyWeekTv2.setTextColor(Color.parseColor("#9F9F9F"))
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
                            tvStudyMonth.setTextColor(Color.parseColor("#9F9F9F"))
                            studyMonthTv2.setTextColor(Color.parseColor("#9F9F9F"))
                            tvStudyWeek.setTextColor(Color.parseColor("#9F9F9F"))
                            studyWeekTv2.setTextColor(Color.parseColor("#9F9F9F"))
                            meet_idx = 2
                            periods_content = etStudyFree.text.toString()
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
                with(binding)
                { title = etStudyTitle.text.toString()
                    group_content = etStudyContent.text.toString()
                    topic_content = etCreateTopic.text.toString()
                    recruitmentEndDate_ = btnDeadline.text.toString()
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

                    var studyRequestData= StudyGroup(userIdx?.toLong(),ImgUri,title,meet_idx,frequency_content,periods_content,online_idx,regionIdx1,regionIdx2,interest_idx
                        ,topic_content,memberLimit_content,applicationMethod_idx,recruitmentEndDate_,groupStart_,groupEnd_
                        ,attendanceVaildTime_content,group_content)
                    Log.e("summer", "USER DATA = ${studyRequestData.toString()}")

                    //Toast Message 설정
                    if (title.equals("")||meet_idx==-1||online_idx==-1||interest_idx==-1||topic_content.equals("")||recruitmentEndDate_.equals("+") ||groupStart_.equals("+") ||groupEnd_.equals("+") || attendanceVaildTime_content==-1 ||group_content.equals(""))
                    {
                        Toast.makeText(this@ManageStudyModifyActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        when(meet_idx)
                        {
                            0,1->{
                                if (online_idx==0 && regionIdx1==null&&regionIdx2==null)
                                {
                                    Toast.makeText(this@ManageStudyModifyActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                                }
                                else if (frequency_content==null)
                                {
                                    Toast.makeText(this@ManageStudyModifyActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    modifyStudy(studyRequestData,"수정하시겠습니까?")
                                }
                            }
                            2->{
                                if (online_idx==0 && (regionIdx1==null&&regionIdx2==null))
                                {
                                    Toast.makeText(this@ManageStudyModifyActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                                }
                                else if (periods_content==null)
                                {
                                    Toast.makeText(this@ManageStudyModifyActivity,"모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()

                                }
                                else{
                                    modifyStudy(studyRequestData,"수정하시겠습니까?")
                                }
                            }
                        }
                    }
                }

            }
        }

        val imageBtn: Button = binding.btnImage
        imageBtn.setOnClickListener {
            //스터디 개설 이미지뷰 갤러리 연동
            imageView = binding.ivStudyCreate
            openGallery()
        }

    }

    fun initData()
    {
        (intent.hasExtra("groupIdx")).let { groupIdx = intent.getLongExtra("groupIdx", 0) }
        Log.e("summer","groupIdx = ${groupIdx}")
    }

    // 본래 스터디 정보 가져오기 retrofit 함수
    fun initView(groupIdx : Long)
    {
        Log.e("summer","데이터 set true")
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.getStudyDetail(groupIdx.toLong()).enqueue(object : Callback<StudyDetailResponse> {
            override fun onResponse(
                call: Call<StudyDetailResponse>,
                response: Response<StudyDetailResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        binding.flLoadingLayout.visibility=View.GONE
                        var result = this.result
                        ImgUri = this.result.groupImgURL
                        Log.e("user data = ", "${this.result.toString()}")

                        if (ImgUri == null || ImgUri.equals(""))
                        {
                            Log.e("주소 없음 진입","true")
                            binding.ivStudyCreate.setImageDrawable(getDrawable(R.drawable.bg_create_sample))
                        }
                        else
                        {
                            Log.e("주소 있음 진입","true")
                            Glide.with(this@ManageStudyModifyActivity).load(ImgUri).into(binding.ivStudyCreate)
                        }
                        with(binding)
                        {
                            etStudyTitle.setText(result.title)
                            etCreateTopic.setText(result.topic)
                            when(result.meet)
                            {
                                0->{
                                    checkWeek.isChecked=true
                                    checkFree.isChecked=false
                                    checkMonth.isChecked=false
                                   etStudyWeek.setText(result.frequency.toString())
                                }
                                1->{
                                    checkWeek.isChecked=false
                                    checkMonth.isChecked=true
                                    checkFree.isChecked=false
                                    etStudyMonth.setText(result.frequency.toString())
                                }
                                2->{
                                    checkWeek.isChecked=false
                                    checkFree.isChecked=true
                                    checkMonth.isChecked=false
                                    etStudyFree.setText(result.periods.toString())
                                }
                            }
                            when(result.online)
                            {
                                0->{ btnOnline.isChecked= true
                                btnOffline.isChecked=false
                                    layoutCreateRegion.visibility=View.GONE
                                    binding.btnPlusPlace1.background = ContextCompat.getDrawable(this@ManageStudyModifyActivity,R.drawable.bg_create_skyblue)
                                    binding.btnPlusPlace2.background = ContextCompat.getDrawable(this@ManageStudyModifyActivity,R.drawable.bg_create_skyblue)}
                                1->{btnOnline.isChecked= true
                                    btnOffline.isChecked=false
                                    layoutCreateRegion.visibility=View.VISIBLE
                                    if (result.regionIdx1!= null)
                                    {
                                        btnPlusPlace1.text= result.regionIdx1
                                        binding.btnPlusPlace1.background = ContextCompat.getDrawable(this@ManageStudyModifyActivity,R.drawable.bg_create_select_blue)
                                    }
                                    else
                                    {
                                        btnPlusPlace1.text = "+"
                                        binding.btnPlusPlace1.background = ContextCompat.getDrawable(this@ManageStudyModifyActivity,R.drawable.bg_create_skyblue)
                                    }

                                    if (result.regionIdx2!= null)
                                    {
                                        btnPlusPlace2.text= result.regionIdx2
                                        binding.btnPlusPlace2.background = ContextCompat.getDrawable(this@ManageStudyModifyActivity,R.drawable.bg_create_select_blue)
                                    }
                                    else
                                    {
                                        btnPlusPlace2.text = "+"
                                        binding.btnPlusPlace2.background = ContextCompat.getDrawable(this@ManageStudyModifyActivity,R.drawable.bg_create_skyblue)
                                    }
                                }
                            }
                            when(result.interest)
                            {
                                1->{spinnerCategory.setSelection(1)}
                                2->{spinnerCategory.setSelection(2)}
                                3->{spinnerCategory.setSelection(3)}
                                4->{spinnerCategory.setSelection(4)}
                                5->{spinnerCategory.setSelection(5)}
                                6->{spinnerCategory.setSelection(6)}
                                7->{spinnerCategory.setSelection(7)}
                            }
                            btnDeadline.text = result.recruitmentEndDate
                            btnStartDay.text = result.groupStart
                            btnFinishDay.text = result.groupEnd
                            when(result.applicationMethod)
                            {
                                0->{btnApplicationFf.isChecked=true
                                btnApplicationApply.isChecked=false
                                    applicationMethod_idx =0
                                }
                                1->{btnApplicationFf.isChecked=false
                                    btnApplicationApply.isChecked=true
                                    applicationMethod_idx =1}
                            }
                            var selectedPeople = result.memberLimit
                            spinnerPeople.setSelection(selectedPeople-2)
                            var selectedTime = result.attendanceValidTime
                            spinnerAttendTime.setSelection((selectedTime/10)-1)
                            etStudyContent.setText(result.groupContent)
                        }
                    }
                }
                else {
                    Log.e("summer init view ", "전달실패")
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StudyDetailResponse>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
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

    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        val uri = Uri.fromFile(File(path))
        cursor.close()
        return path
    }

    //갤러리에서 이미지 선택
    private fun openGallery(){
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent,GALLERY)
    }

    fun uploadImage(file:File,studyRequestData: StudyGroup)
    {
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
                        retrofitModify(studyRequestData)
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
        else{
            retrofitModify(studyRequestData)
        }
    }

    //스터디 수정 값들 가져와서 전송 retrofit 함수
    fun modifyStudy(studyRequestData : StudyGroup,content_text:String){
        //레트로핏 부분
        dialog_.findViewById<TextView>(R.id.tv_title).text = content_text
        dialog_.show()
        dialog_.findViewById<Button>(R.id.btn_no).setOnClickListener {
            dialog_.dismiss()
        }
        dialog_.findViewById<Button>(R.id.btn_yes).setOnClickListener {
            uploadImage(file,studyRequestData)
            Log.e("summer","retrofit 함수 in")
            Log.e("summer", "USER DATA = ${studyRequestData.toString()}")
        }
    }

    fun retrofitModify(studyRequestData: StudyGroup)
    {
        Log.e("StudyReq 최종", "${studyRequestData.toString()}")
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.modifyStudy(groupIdx.toLong(),studyRequestData).enqueue(object : Callback <StudyModifyResponse> {
            override fun onResponse(call: Call<StudyModifyResponse>, response: Response<StudyModifyResponse>) {
                if (response.isSuccessful)
                {
                    response.body()?.apply {
                        dialog_.dismiss()
                        finish()
                    }
                }
                else
                {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                    Toast.makeText(this@ManageStudyModifyActivity,"다시 시도해주세요",Toast.LENGTH_SHORT).show()
                    dialog_.dismiss()
                }
            }
            override fun onFailure(call: Call<StudyModifyResponse>, t: Throwable) {
                Log.e("summer","onFailure t = ${t.toString()}")
                Log.e("summer","onFailure msg = ${t.message}")
                Toast.makeText(this@ManageStudyModifyActivity,"다시 시도해주세요",Toast.LENGTH_SHORT).show()
                dialog_.dismiss()
            }
        })
    }
    fun setupSinner(){
            val interest_spinner = binding.spinnerCategory
            val memberLimit_spinner = binding.spinnerPeople
            val attendanceVaildTime_spinner = binding.spinnerAttendTime

            interest_spinner.adapter = ArrayAdapter.createFromResource(
                this.applicationContext,R.array.intersting,R.layout.item_create_spinner
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

    fun customDialog()
    {
        dialog_ = Dialog(this@ManageStudyModifyActivity)
        dialog_.setContentView(R.layout.dialog_create)
        var params = dialog_.window?.attributes
        dialog_.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        params?.width= WindowManager.LayoutParams.MATCH_PARENT
        params?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        dialog_.window?.attributes=params
    }

}