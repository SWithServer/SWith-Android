package com.example.swith.ui.study.create

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.SwithApplication.Companion.spfManager
import com.example.swith.data.api.RetrofitService
import com.example.swith.data.api.SwithService
import com.example.swith.databinding.ActivityStudyCreateBinding
import com.example.swith.databinding.DialogCreateBinding
import com.example.swith.domain.entity.StudyGroup
import com.example.swith.domain.entity.StudyImageRes
import com.example.swith.domain.entity.StudyResponse
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.viewmodel.StudyCreateViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class StudyCreateActivity :AppCompatActivity(),View.OnClickListener {
    lateinit var binding: ActivityStudyCreateBinding
    private val viewModel : StudyCreateViewModel by viewModels()
    private val GALLERY=1
    var file=File("")

    private lateinit var startTime: Calendar //활동 시작기간
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    //val userid = SharedPrefManager(this@StudyCreateActivity).getLoginData()
    private val userIdx = spfManager.getLoginData()?.userIdx
    private var meetIdx = -1
    private var frequencyContent:Int?=null
    private var periodsContent:String?=null
    private var onlineIdx:Int = -1 //오프라인 0, 온라인 1

    // spinner 선택되는 값들 매칭
    var interestIdx=-1
    var memberLimitContent=-1
    var applicationMethodIdx =-1
    var attendanceValidTimeContent=-1

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
            }
        }

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
                when (meetIdx) {
                    0 -> {
                        if (!etStudyWeek.text.isNullOrBlank())
                            frequencyContent = etStudyWeek.text.toString().toIntOrNull()
                    }
                    1 -> {
                        if (!etStudyMonth.text.isNullOrBlank())
                            frequencyContent = etStudyMonth.text.toString().toIntOrNull()
                    }
                    2 -> {
                        if (!etStudyFree.text.isNullOrBlank())
                            periodsContent = etStudyFree.text.toString()
                    }
                }

                var studyRequestData = StudyGroup(
                    1,
                    null,
                    etStudyTitle.text.toString(),
                    meetIdx,
                    frequencyContent,
                    periodsContent,
                    onlineIdx,

                    if (onlineIdx==0) btnPlusPlace1.text.toString() else null,
                    if (onlineIdx==0) btnPlusPlace2.text.toString() else null,
                    interestIdx,
                    etCreateTopic.text.toString(),
                    memberLimitContent,
                    applicationMethodIdx,
                    tvDeadline.text.toString(),
                    tvStartDay.text.toString(),
                    tvFinishDay.text.toString(),
                    attendanceValidTimeContent,
                    etStudyContent.text.toString()
                )
                Log.e("summer", "USER DATA = $studyRequestData")

                //Toast Message 설정
                if (etStudyTitle.text.equals("") || meetIdx == -1 || onlineIdx == -1 ||
                    interestIdx == -1 || etCreateTopic.text.equals("") ||
                    tvDeadline.text.equals("+") || tvStartDay.text.equals("+") || tvFinishDay.text.equals("+") ||
                    attendanceValidTimeContent == -1 || etStudyContent.text.equals("")
                ) {
                    Toast.makeText(
                        this@StudyCreateActivity,
                        "모든 항목을 작성해주세요!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    when (meetIdx) {
                        0, 1 -> {
                            if (onlineIdx == 0 && btnPlusPlace1.text.equals("+") && btnPlusPlace2.text.equals("+")) {
                                Toast.makeText(
                                    this@StudyCreateActivity,
                                    "모든 항목을 작성해주세요!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (frequencyContent == null) {
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
                            if (onlineIdx == 0 && btnPlusPlace1.text.equals("+") && btnPlusPlace2.text.equals("+")) {
                                Toast.makeText(
                                    this@StudyCreateActivity,
                                    "모든 항목을 작성해주세요!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (periodsContent == null) {
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

        with(binding){
            if(getSharedPreferences("result1",0).getString("이름1","").toString() != "") {
                val shPref1 = getSharedPreferences("result1",0)
                btnPlusPlace1.text = shPref1.getString("이름1", "")
                if(!(shPref1.getString("이름1","").equals("")) && !(shPref1.getString("이름1","").equals("+"))) {
                    btnPlusPlace1.background = ContextCompat.getDrawable(
                        this@StudyCreateActivity,R.drawable.bg_create_select_blue)
                }
                else{
                    btnPlusPlace1.background = ContextCompat.getDrawable(
                        this@StudyCreateActivity,R.drawable.bg_create_skyblue)
                }
            }

            if(getSharedPreferences("result2",0).getString("이름2","").toString() != ""){
                val shPref2 = getSharedPreferences("result2",0)
                btnPlusPlace2.text = shPref2.getString("이름2", "")
                if(!(shPref2.getString("이름2","").equals("")) && !(shPref2.getString("이름2","").equals("+")))
                {
                    btnPlusPlace2.background = ContextCompat.getDrawable(
                        this@StudyCreateActivity, R.drawable.bg_create_select_blue)
                }
                else{
                    btnPlusPlace2.background = ContextCompat.getDrawable(
                        this@StudyCreateActivity,R.drawable.bg_create_skyblue)
                }
            }
        }
    }

    private fun saveDialog(studyRequestData : StudyGroup) {
        Log.e("summer", "saveDialog 함수")
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
                        Log.e("summer", "onConfirm()")
                        Log.e("summer","$file")
                        if (!file.equals("")) {
                            studyRequestData.groupImgUri = viewModel.postStudyImage(file,studyRequestData)
                        }
                        viewModel.postStudyInfo(studyRequestData).apply{
                            if (this!= (-1).toLong()){
                                setResult(RESULT_OK)
                                finish()
                            }
                            else {
                                Toast.makeText(this@StudyCreateActivity,
                                    "다시 시도해주세요",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onClose() {
                        Log.e("summer", "onClose()")
                    }
                })
                show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY)
        {
            if(resultCode == RESULT_OK)
            {
                var currentImageUri = data?.data
                Log.e("현재 이미지 url", "$currentImageUri")
                try{
                    currentImageUri?.let{
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            binding.ivStudyCreate.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            binding.ivStudyCreate.setImageBitmap(bitmap)
                        }
                        Log.e("path 값","${getRealPathFromURI(currentImageUri)}")
                        file = File(getRealPathFromURI(currentImageUri))
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
            val datePickerDialog = DatePickerDialog(this@StudyCreateActivity, R.style.DatePickerTheme,
                { _, year, month, day ->
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
            val datePickerDialog = DatePickerDialog(this@StudyCreateActivity, R.style.DatePickerTheme,
                { _, year, month, day ->
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
                val datePickerDialog = DatePickerDialog(this@StudyCreateActivity, R.style.DatePickerTheme,
                    { _, year, month, day ->
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
                    interestIdx=position+1
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    memberLimitContent = "${spinnerPeople.getItemAtPosition(position)}".toInt()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerAttendTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    attendanceValidTimeContent = "${spinnerAttendTime.getItemAtPosition(position)}".toInt()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            btnApplicationApply.setOnCheckedChangeListener { checkbox, isChecked ->
                if (isChecked) {
                    when (checkbox.id) {
                        R.id.btn_application_ff -> {
                            btnApplicationApply.isChecked = false
                            btnApplicationFf.isChecked = true
                            applicationMethodIdx = 0 //선착순
                        }
                        R.id.btn_application_apply -> {
                            btnApplicationFf.isChecked = false
                            btnApplicationApply.isChecked = true
                            applicationMethodIdx = 1 // 지원
                        }
                    }
                }
            }

            btnApplicationFf.setOnCheckedChangeListener{ checkbox, isChecked ->
                if (isChecked) {
                    when (checkbox.id) {
                        R.id.btn_application_ff -> {
                            btnApplicationApply.isChecked = false
                            btnApplicationFf.isChecked = true
                            applicationMethodIdx = 0 //선착순
                        }
                        R.id.btn_application_apply -> {
                            btnApplicationFf.isChecked = false
                            btnApplicationApply.isChecked = true
                            applicationMethodIdx = 1 // 지원
                        }
                    }
                }
            }

            //on,offline 장소선택
            val listenerOnline =
                CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                    if (isChecked)
                        when (checkbox.id) {
                            R.id.btn_online -> {
                                btnOnline.isChecked = true
                                btnOffline.isChecked = false
                                tvStudyRegion.visibility = View.GONE
                                layoutCreateRegionBtns.visibility = View.GONE
                                lineRegion.visibility = View.GONE
                                onlineIdx = 1
                                btnPlusPlace1.text="+"
                                btnPlusPlace2.text="+"
                                btnPlusPlace1.background = ContextCompat.getDrawable(
                                    this@StudyCreateActivity,R.drawable.bg_create_skyblue)
                                btnPlusPlace2.background = ContextCompat.getDrawable(
                                    this@StudyCreateActivity,R.drawable.bg_create_skyblue)

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
                                onlineIdx = 0
                            }
                        }
                }
            btnOnline.setOnCheckedChangeListener(listenerOnline)
            btnOffline.setOnCheckedChangeListener(listenerOnline)

            //시간 선택
            val listener = CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                if (isChecked)
                    when (checkbox.id) {
                        R.id.check_week -> {
                            toggleTime(etStudyFree,etStudyMonth,tvStudyWeek,studyWeekTv2,tvStudyMonth,studyMonthTv2,checkMonth,checkFree)
                            etStudyWeek.isEnabled = true
                            etStudyMonth.isEnabled = false
                            etStudyFree.isEnabled = false
                            etStudyFree.setTextColor(Color.parseColor("#9F9F9F"))
                            meetIdx = 0
                        }
                        R.id.check_month -> {
                            toggleTime(etStudyFree,etStudyWeek,tvStudyMonth,studyMonthTv2,tvStudyWeek,studyWeekTv2,checkWeek,checkFree)
                            etStudyMonth.isEnabled = true
                            etStudyWeek.isEnabled = false
                            etStudyFree.isEnabled = false
                            etStudyFree.setTextColor(Color.parseColor("#9F9F9F"))
                            meetIdx = 1
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
                            etStudyFree.setTextColor(Color.parseColor("#525252"))
                            meetIdx = 2
                            periodsContent = etStudyFree.text.toString()
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

    @SuppressLint("Range")
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
        val intent= Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,GALLERY)
    }

    private fun hideKeyboard(editText: EditText){
        val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(
            editText.getWindowToken(),
            0
        )
    }

    // TODO 서버 연결 후 ViewModel 코드 잘 실행되면 함수 삭제 하기
    private fun uploadImage(file:File,studyRequestData: StudyGroup) {
        Log.e("업로드 함수","진입")
        if (!(file.name.equals("")))
        {
            val requestFile = file.asRequestBody("image".toMediaTypeOrNull())
            var body  = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val retrofitService = RetrofitService.retrofit.create(SwithService::class.java)
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
                            //imgUri = this.imageUrls[0]
                            //  Log.e("Img Uri 값 변경한 부분","$imgUri")
                            //  studyRequestData.groupImgUri=imgUri
                            //postStudy(studyRequestData)
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
            //postStudy(studyRequestData)
        }
    }
    private fun postStudy(studyRequestData: StudyGroup) {
        Log.e("StudyReq 최종", "${studyRequestData.toString()}")
        val retrofitService = RetrofitService.retrofit.create(SwithService::class.java)
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

}