package com.example.swith.ui.study.find

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.data.StudyApplicationResponse
import com.example.swith.data.StudyDetailResponse
import com.example.swith.data.postApplicationReq
import com.example.swith.databinding.DialogCreateBinding
import com.example.swith.databinding.FragmentStudyFindDetailBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.MainActivity
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.ui.manage.ManageUserProfileActivity
import com.example.swith.utils.CustomBinder
import com.example.swith.utils.SharedPrefManager
import com.example.swith.utils.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyFindDetailFragment : BaseFragment<FragmentStudyFindDetailBinding>(R.layout.fragment_study_find_detail),MainActivity.onBackPressedListener {
    var groupIdx : Long? = -1
//    val userid= SharedPrefManager(requireActivity()).getLoginData()
//    val userIdx = userid?.userIdx

    var adminIdx : Long? = -1
    var activity_:MainActivity? =null
    lateinit var dialog_ :Dialog
    var applicationMethod : Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("summer", "fragment이동 true")
        groupIdx = arguments?.getLong("groupIdx",0)
        applicationMethod = arguments?.getInt("applicationMethod",0)
        Log.e("summer","groupIdx = $groupIdx")
        Log.e("summer","applicationMethod = $applicationMethod")
        setData(groupIdx)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customDialog()
        binding.flLoadingLayout.visibility=View.VISIBLE
        setVisiblebar(true,false,"","")

        with(binding)
        {
            btnStudyApply.setOnClickListener {
                 when(applicationMethod)
                {
                     0->{
                       showLastDialog(0,null) //선착순(신청서 필요 x)
                     }
                     1->{
                        createApplication(1) //지원(신청서 -> 최종 dialog)
                    }
                 }
             }

            btnAdmin.setOnClickListener {
                Log.e("클릭이벤트 발생","true")
                val intent = Intent(requireActivity(), ManageUserProfileActivity::class.java)
                intent.putExtra("userIdx", adminIdx)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        activity_?.goSearchPage()
    }
    //신청서 Dialog
    fun createApplication (applicationMethod: Int){
        dialog_.show()
        var dialog_et = dialog_.findViewById<EditText>(R.id.et_application)
        var applyContent = ""
        dialog_et.setOnKeyListener { view, code, event ->
            if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER)){
                applyContent = dialog_et.text.toString()
                hideKeyboard(dialog_et)
                true
            }
            else
                false
            }
        dialog_.findViewById<Button>(R.id.btn_application_apply).setOnClickListener {
           //신청서 작성 내용 변수
            applyContent = dialog_et.text.toString()
            showLastDialog(1,applyContent)
            dialog_.dismiss()
        }
    }

    //최종 제출하기 Dialog
    fun showLastDialog(applicationMethod:Int,applyContent : String?){
        Log.e("신청서 내용","${applyContent}")
        DataBindingUtil.inflate<DialogCreateBinding>(
            LayoutInflater.from(requireActivity()), R.layout.dialog_create, null, false
        ).apply {
            this.createDialog = CustomBinder.showCustomDialog(requireActivity(),
                root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                object : CustomDialog.DialogClickListener {
                    override fun onClose() {
                    }

                    override fun onConfirm() {
                        postData(applicationMethod,groupIdx,applyContent,1)
                    }
                })
        }
    }

    //groupIdx로 study정보 가져오는 retrofit 부분 (API 나오면 작성하기)
    fun setData(groupIdx: Long?)
    {
        Log.e("summer","데이터 set true")
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.getStudyDetail(groupIdx!!).enqueue(object : Callback<StudyDetailResponse> {
            override fun onResponse(
                call: Call<StudyDetailResponse>,
                response: Response<StudyDetailResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        var result = this.result
                        Log.e("스터디 정보 값들",result.toString())
                        adminIdx = result.adminIdx
                        with(binding)
                        {
                            tvStudyDetailTitle.text= result.title
                            when(result.meet)
                            {
                                0->{
                                    tvStudySetTime.text = "주 ${result.frequency}회"
                                }
                                1->{
                                    tvStudySetTime.text = "월 ${result.frequency}회"
                                }
                                2->{
                                    tvStudySetTime.text ="${result.periods}"
                                }
                            }
                            when(result.online)
                            {
                                1->{ tvStudySetPlace1.text ="온라인"
                                tvStudySetPlace2.visibility = View.GONE}
                                0->{tvStudySetPlace1.text ="${result.regionIdx1}"
                                    tvStudySetPlace2.text ="${result.regionIdx2}"
                                tvStudySetPlace2.visibility=View.VISIBLE
                                tvStudySetPlace1.visibility=View.VISIBLE}
                            }
                            when(result.interest)
                            {
                                1->{tvStudySetCategory.text ="자격증/시험"}
                                2->{tvStudySetCategory.text ="어학"}
                                3->{tvStudySetCategory.text ="청소년/입시"}
                                4->{tvStudySetCategory.text ="취업/창업"}
                                5->{tvStudySetCategory.text ="컴퓨터/IT"}
                                6->{tvStudySetCategory.text ="취미/문화"}
                                7->{tvStudySetCategory.text ="면접"}
                            }
                            tvStudySetPlaytime.text = result.groupStart + " ~ " + result.groupEnd
                            when(result.applicationMethod)
                            {
                                0->{tvStudySetApplyMethod.text ="선착순"}
                                1->{tvStudySetApplyMethod.text ="지원"}
                            }
                            tvStudySetDeadline.text = "${result.recruitmentEndDate} " + "까지"
                            tvStudySetApplicationStatus.text = "${result.numOfApplicants} / ${result.memberLimit}"
                            tvStudySetDetailContent.text= result.groupContent
                        }
                        binding.flLoadingLayout.visibility=View.GONE
                    }
                }
                else {
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

    // 신청서 내용 보내기 retrofit 부분 (API 나오면 작성)
    fun postData(applicationMethod:Int,groupIdx:Long?, applyContent:String?, UserIdx:Long)
    {
        Log.e("신청서 내용 보내는 부분","${applyContent.toString()}")
        var postApplicationReq = postApplicationReq(UserIdx,applyContent)
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.postApplication(groupIdx!!,applicationMethod,postApplicationReq).enqueue(object : Callback <StudyApplicationResponse> {
            override fun onResponse(call: Call<StudyApplicationResponse>, response: Response<StudyApplicationResponse>) {
                if (response.isSuccessful)
                {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        Log.e("post data =","${groupIdx},${applicationMethod},${postApplicationReq.toString()}")
                    }
                }
                else
                {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                    Toast.makeText(requireActivity(),"다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    dialog_.dismiss()
                }
            }
            override fun onFailure(call: Call<StudyApplicationResponse>, t: Throwable) {
                Log.e("summer","onFailure t = ${t.toString()}")
                Log.e("summer","onFailure msg = ${t.message}")
                Toast.makeText(requireActivity(),"다시 시도해주세요", Toast.LENGTH_SHORT).show()
                dialog_.dismiss()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity_ = activity as MainActivity
    }

    private fun hideKeyboard(editText: EditText){
        val mInputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(
            editText.getWindowToken(),
            0
        )
    }

    fun customDialog()
    {
        dialog_ = Dialog(requireActivity())
        dialog_.setContentView(R.layout.dialog_application)
        var params = dialog_.window?.attributes
        dialog_.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        params?.width=WindowManager.LayoutParams.MATCH_PARENT
        params?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        dialog_.window?.attributes=params
    }
}