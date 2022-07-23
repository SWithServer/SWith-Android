package com.example.swith.ui.study.create

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.studywith.ConfirmDialog
import com.example.studywith.ConfirmDialogInterface
import com.example.swith.R
import com.example.swith.databinding.ActivityStudyCreateBinding
import java.util.*

class StudyCreateActivity :AppCompatActivity(),ConfirmDialogInterface{
    lateinit var binding: ActivityStudyCreateBinding

    private val GALLERY=1
    private var imageView: ImageView? = null

    private lateinit var startTime: Calendar //활동 시작기간
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //intent
        val selectPlace_intent = Intent(this, SelectPlaceActivity::class.java)

        val imageBtn: Button = binding.btnImage
        imageBtn.setOnClickListener {
            //스터디 개설 이미지뷰 갤러리 연동
            imageView =binding.ivStudyCreate
            openGallery()
        }

        //btn_onClickListener들
        binding.btnStudyCreate.setOnClickListener {
            createStudy()
        }
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
                binding.btnDeadline.text =
                    year.toString() + "/" + (month + 1).toString() + "/" + day.toString()
            }, year, month, day)
            datePickerDialog.show()
        }

        //활동 시작기간 설정
        binding.btnStartDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                binding.btnStartDay.text =
                    year.toString() + "/" + (month + 1).toString() + "/" + day.toString()
                startTime = Calendar.getInstance().apply { set(year, month, day) }
            }, year, month, day).apply {
            }
            datePickerDialog.show()
        }
        // 활동 끝나는기간 설정
        binding.btnFinishDay.setOnClickListener {
            if (binding.btnStartDay.text.toString() != "+") {
                val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                    binding.btnFinishDay.text =
                        year.toString() + "/" + (month + 1).toString() + "/" + day.toString()
                }, year, month, day).apply {
                    datePicker.minDate = startTime.timeInMillis
                }
                datePickerDialog.show()
            } else {
                Toast.makeText(this, "시작날짜부터 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        //spinner
        val category_spinner = binding.spinnerCategory
        category_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.categoryList,
            android.R.layout.simple_spinner_item
        )
        val people_spinner = binding.spinnerPeople
        people_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.peopleList,
            android.R.layout.simple_spinner_item
        )
        val attendmethod_spinner = binding.spinnerMethod
        attendmethod_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.methodList,
            android.R.layout.simple_spinner_item
        )
        val time_spinner = binding.spinnerAttendTime
        time_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.attendTimeList,
            android.R.layout.simple_spinner_item
        )

        //on,offline 장소선택
        binding.placeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_online -> binding.studyCreateOfflineLayout.visibility = View.GONE
                R.id.btn_offline -> binding.studyCreateOfflineLayout.visibility = View.VISIBLE
            }
        }

        //시간 선택
        val listener = CompoundButton.OnCheckedChangeListener { checkbox, isChecked ->
            val tv_study_month = binding.tvStudyMonth
            val tv_study_week = binding.tvStudyWeek
            val tv_study_free = binding.tvStudyFree
            if (isChecked)
                when (checkbox.id) {

                    R.id.check_week -> {
                        tv_study_month.setText("")
                        tv_study_free.setText("")
                        tv_study_week.isEnabled = true
                        tv_study_month.isEnabled = false
                        tv_study_free.isEnabled = false
                    }
                    R.id.check_month -> {
                        tv_study_week.setText("")
                        tv_study_free.setText("")
                        tv_study_month.isEnabled = true
                        tv_study_week.isEnabled = false
                        tv_study_free.isEnabled = false
                    }
                    R.id.check_free -> {
                        tv_study_week.setText("")
                        tv_study_month.setText("")
                        tv_study_free.isEnabled = true
                        tv_study_month.isEnabled = false
                        tv_study_week.isEnabled = false
                    }
                }
        }
        binding.checkWeek.setOnCheckedChangeListener(listener)
        binding.checkMonth.setOnCheckedChangeListener(listener)
        binding.checkFree.setOnCheckedChangeListener(listener)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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

    //개설버튼 클릭시 dialog
    lateinit var dialog: ConfirmDialog
    fun createStudy() {
        dialog = ConfirmDialog("개설하시겠습니까?")
        dialog.show(supportFragmentManager, "ConfirmDialog")
    }
    override fun onYesButtonClick() {
        finish()
    }
}
