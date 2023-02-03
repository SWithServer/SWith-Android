package com.example.swith.ui.manage

import android.app.DatePickerDialog
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
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageStudyModifyBinding
import com.example.swith.databinding.DialogCreateBinding
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.viewmodel.StudyModifyViewModel
import java.io.File
import java.util.*
import com.example.swith.domain.entity.*
import com.example.swith.ui.dialog.CustomDialog
import com.google.android.material.internal.ViewUtils.hideKeyboard


class ManageStudyModifyActivity : AppCompatActivity() {
    lateinit var binding:ActivityManageStudyModifyBinding
    private val viewModel:StudyModifyViewModel by viewModels()
    private var studyInfo : StudyDetailResult?
    private var groupIdx : Long = -1
    private var meetIdx:Int= -1

    private lateinit var startTime: Calendar //활동 시작기간
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    private val GALLERY=1
    private var file=File("")

    init
    {
        intent.hasExtra("groupIdx").let{
            groupIdx = intent.getLongExtra("groupIdx", 0)
            Log.e("summer","groupIdx = $groupIdx")
        }
        studyInfo = viewModel.getStudyInfo(groupIdx)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ManageStudyModifyActivity, R.layout.activity_manage_study_modify)
        initView()
        initListener()
        initEditText()
        setUpSpinner()
    }

    private fun initView() {
        with(binding){
            viewModel?.loadingView?.observe(this@ManageStudyModifyActivity) {
                if(it) flLoadingLayout.visibility=View.VISIBLE
                else  flLoadingLayout.visibility=View.GONE
            }
            studyInfo?.apply{
                when(this.meet){
                    0-> {
                        etStudyWeek.setText(this.frequency.toString())
                        checkWeek.isChecked=true
                    }
                    1-> {
                        etStudyMonth.setText(this.frequency.toString())
                        checkMonth.isChecked=true
                    }
                    2-> {
                        etStudyFree.setText(this.periods.toString())
                        checkFree.isChecked=true
                    }
                }
                if(this.online==0){
                    layoutCreateRegion.visibility=View.VISIBLE
                    btnPlusPlace1.text = this.regionIdx1.toString()
                    btnPlusPlace2.text=this.regionIdx1.toString()
                }
                else{
                    layoutCreateRegion.visibility=View.GONE
                }
            }
        }

    }

    private fun initListener(){
        with(binding)
        {
            btnPlusPlace1.setOnClickListener {
                Intent(this@ManageStudyModifyActivity, SelectPlaceActivity::class.java).apply{
                    this.putExtra("번호",1)
                    startActivity(this)
                }
            }
            btnPlusPlace2.setOnClickListener {
                Intent(this@ManageStudyModifyActivity, SelectPlaceActivity::class.java).apply{
                    this.putExtra("번호",2)
                    startActivity(this)
                }
            }
            // 모집 마감기간 설정
            btnDeadline.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@ManageStudyModifyActivity, { _, year, month, day ->
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
            btnStartDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@ManageStudyModifyActivity, { _, year, month, day ->
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
            }, year, month, day)
            datePickerDialog.show()
            }

            // 활동 끝나는기간 설정
            btnFinishDay.setOnClickListener {
            if (binding.btnStartDay.text.toString() != "+") {
                val datePickerDialog = DatePickerDialog(this@ManageStudyModifyActivity, { _, year, month, day ->
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
                Toast.makeText(this@ManageStudyModifyActivity,
                    "시작날짜부터 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
            //on,offline 장소선택
            val listenerOnline = CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
                    if (isChecked)
                        when (checkbox.id) {
                            R.id.btn_online -> {
                                btnOnline.isChecked = true
                                btnOffline.isChecked = false
                                layoutCreateRegion.visibility=View.GONE
                                lineRegion.visibility = View.GONE
                            }
                            R.id.btn_offline -> {
                                btnOnline.isChecked = false
                                btnOffline.isChecked = true
                                layoutCreateRegion.visibility=View.VISIBLE
                                lineRegion.visibility = View.VISIBLE
                            }
                        }
            }
            btnOnline.setOnCheckedChangeListener(listenerOnline)
            btnOffline.setOnCheckedChangeListener(listenerOnline)

            //시간 선택
            val listenerTime = CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
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
                            meetIdx = 0
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
                            meetIdx = 1
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
                            meetIdx = 2
                        }
                    }
            }
            checkWeek.setOnCheckedChangeListener(listenerTime)
            checkMonth.setOnCheckedChangeListener(listenerTime)
            checkFree.setOnCheckedChangeListener(listenerTime)

            btnImage.setOnClickListener {
                openGallery()
            }

            btnStudyModify.setOnClickListener {
                var frequencyContent:Int? = null
                var periodsContent:String? = null
                var onlineIdx = if(btnOnline.isChecked) 1 else 0

                when(meetIdx) {
                        0->{if (!etStudyWeek.text.isNullOrBlank())
                            frequencyContent=etStudyWeek.text.toString().toIntOrNull()
                        }
                        1->{if (!etStudyMonth.text.isNullOrBlank())
                            frequencyContent=etStudyMonth.text.toString().toIntOrNull()}
                        2->{
                            if (!etStudyFree.text.isNullOrBlank())
                            periodsContent=etStudyFree.text.toString()
                        }
                    }

                var studyRequestData= StudyGroup(
                        1,
                        null,
                        etStudyTitle.text.toString(),
                        meetIdx,
                        frequencyContent,
                        periodsContent,
                        onlineIdx,
                        if(onlineIdx==0) btnPlusPlace1.text.toString() else null,
                        if(onlineIdx==0) btnPlusPlace2.text.toString() else null,
                        spinnerCategory.selectedItemPosition+1,
                        etCreateTopic.text.toString(),
                        spinnerPeople.selectedItem.toString().toInt(),
                        if(btnApplicationApply.isChecked) 1 else 0,
                        btnDeadline.text.toString(),
                        btnStartDay.text.toString(),
                        btnFinishDay.text.toString(),
                        spinnerAttendTime.selectedItem.toString().toInt(),
                        etStudyContent.text.toString()
                    )
                    Log.e("summer", "USER DATA = $studyRequestData")

                    //Toast Message 설정
                    if (etStudyTitle.text.equals("")||meetIdx==-1||onlineIdx==-1 ||
                        spinnerCategory.selectedItemPosition==0 || etCreateTopic.text.equals("") ||
                        btnDeadline.text.equals("+") || btnStartDay.text.equals("+") ||
                        btnFinishDay.text.equals("+") || etStudyContent.text.equals(""))
                    {
                        Toast.makeText(this@ManageStudyModifyActivity,
                            "모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        when(meetIdx)
                        {
                            0,1->{
                                if (onlineIdx==0 && (btnPlusPlace1.text.equals("+") && btnPlusPlace2.text.equals("+")))
                                {
                                    Toast.makeText(this@ManageStudyModifyActivity,
                                        "모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                                }
                                else if (frequencyContent==null)
                                {
                                    Toast.makeText(this@ManageStudyModifyActivity,
                                        "모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    saveDialog(studyRequestData)
                                }
                            }
                            2->{
                                if (onlineIdx==0 && (btnPlusPlace1.text.equals("+") && btnPlusPlace2.text.equals("+")))
                                {
                                    Toast.makeText(this@ManageStudyModifyActivity,
                                        "모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()
                                }
                                else if (periodsContent==null)
                                {
                                    Toast.makeText(this@ManageStudyModifyActivity,
                                        "모든 항목을 작성해주세요!",Toast.LENGTH_SHORT).show()

                                }
                                else{
                                   saveDialog(studyRequestData)
                                }
                            }
                        }
                    }
                }

        }
    }

    private fun saveDialog(studyRequestData : StudyGroup) {
        Log.e("summer", "saveDialog 함수")
        DataBindingUtil.inflate<DialogCreateBinding>(
            LayoutInflater.from(this@ManageStudyModifyActivity),
            R.layout.dialog_create, null, false
        ).apply {
            this.tvTitle.text = "수정하시겠습니까?"
            this.createDialog = CustomDialog(
                this@ManageStudyModifyActivity,
                root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            ).apply {
                this.setClickListener(object : CustomDialog.DialogClickListener {
                    override fun onConfirm() {
                        Log.e("summer", "onConfirm()")
                        Log.e("summer","$file")
                        if (!file.equals("")) {
                            studyRequestData.groupImgUri = viewModel.postStudyImage(file)
                        }
                        viewModel.modifyStudy(groupIdx,studyRequestData).apply{
                            if (this!=(-1).toLong()){
                                finish()
                            }
                            else{
                                Toast.makeText(this@ManageStudyModifyActivity,
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

    private fun initEditText(){
        with(binding) {
            etStudyContent.setOnKeyListener { view, code, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyContent.text.equals("")) {
                    hideKeyboard(etStudyContent)
                    true
                } else false
            }
            etCreateTopic.setOnKeyListener { view, code, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etCreateTopic.text.equals("")) {
                    hideKeyboard(etStudyContent)
                    true
                } else false
            }
            etStudyTitle.setOnKeyListener { view, code, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyTitle.text.equals("")) {
                    hideKeyboard(etStudyContent)
                    true
                } else false
            }
            etStudyWeek.setOnKeyListener { view, code, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyWeek.text.equals("")) {
                    hideKeyboard(etStudyContent)
                    true
                } else false
            }
            etStudyMonth.setOnKeyListener { view, code, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyMonth.text.equals("")) {
                    hideKeyboard(etStudyContent)
                    true
                } else false
            }
            etStudyFree.setOnKeyListener { view, code, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudyFree.text.equals("")) {
                    hideKeyboard(etStudyContent)
                    true
                } else false
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
                            binding.ivStudyCreate?.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            binding.ivStudyCreate?.setImageBitmap(bitmap)
                        }
                        val path= getRealPathFromURI(currentImageUri)
                        Log.e("path 값","$path")
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

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        cursor.close()
        return path
    }

    private fun setUpSpinner(){
        with(binding){
            spinnerCategory.adapter = ArrayAdapter.createFromResource(
                this@ManageStudyModifyActivity.applicationContext,R.array.intersting,R.layout.item_create_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_create_spinner_dropdown)
            }
            spinnerPeople.adapter = ArrayAdapter.createFromResource(
                this@ManageStudyModifyActivity.applicationContext,
                R.array.peopleList,
                R.layout.item_create_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_create_spinner_dropdown)
            }
            spinnerAttendTime.adapter = ArrayAdapter.createFromResource(
                this@ManageStudyModifyActivity.applicationContext,
                R.array.attendTimeList,
                R.layout.item_create_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_create_spinner_dropdown)
            }
        }
    }

    private fun openGallery(){
        Intent(Intent.ACTION_PICK).apply{
            type = "image/*"
            startActivityForResult(this,GALLERY)
        }
    }

}