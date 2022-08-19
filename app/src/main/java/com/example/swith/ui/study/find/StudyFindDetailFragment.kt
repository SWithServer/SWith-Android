package com.example.swith.ui.study.find

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.DialogCreateBinding
import com.example.swith.databinding.FragmentStudyFindDetailBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.MainActivity
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.utils.CustomBinder
import com.example.swith.utils.base.BaseFragment

class StudyFindDetailFragment : BaseFragment<FragmentStudyFindDetailBinding>(R.layout.fragment_study_find_detail),MainActivity.onBackPressedListener {
    var groupIdx : Long? = -1
    var activity_:MainActivity? =null
    lateinit var dialog_ :Dialog
    var applicationMethod : Int? = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog_ = Dialog(requireActivity())
        dialog_.setContentView(R.layout.fragment_dialog_application)
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

        setVisiblebar(true,false,"")

        with(binding)
        {
            btnStudyApply.setOnClickListener {
                 when(applicationMethod)
                {
                     0->{
                       showLastDialog(null) //선착순(신청서 필요 x)
                     }
                     1->{
                    createApplication() //지원(신청서 -> 최종 dialog)
                    }
                 }
             }
        }
    }

    override fun onBackPressed() {
        Log.e("뒤로가기 눌림","true")
        activity_?.goSearchPage()
    }

    //신청서 Dialog
    fun createApplication (){
        dialog_.show()
        var dialog_et = dialog_.findViewById<EditText>(R.id.et_application)
        var applyContent = ""
        dialog_et.setOnKeyListener { view, code, event ->
            if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !dialog_et.text.equals("")){
                applyContent = dialog_et.text.toString()
                hideKeyboard(dialog_et)
                true
            }
            else
                false
            }
        dialog_.findViewById<Button>(R.id.btn_application_apply).setOnClickListener {
           //신청서 작성 내용 변수
            showLastDialog(applyContent)
            dialog_.dismiss()
        }
    }

    //최종 제출하기 Dialog
    fun showLastDialog(applyContent : String?){
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
                        postData(groupIdx,applyContent,1)
                    }
                })
        }
    }

    //groupIdx로 study정보 가져오는 retrofit 부분 (API 나오면 작성하기)
    fun setData(groupIdx: Long?)
    {
        Log.e("summer","데이터 set true")
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
//        retrofitService.getStudyDetail(groupIdx).enqueue(object : Callback<StudyFindResponse> {
//            override fun onResponse(
//                call: Call<StudyFindResponse>,
//                response: Response<StudyFindResponse>
//            ) {
//                if (response.isSuccessful) {
//                    Log.e("summer", "성공${response.toString()}")
//                    response.body()?.apply {
//                        var response = this.result
//                        with(binding)
//                        {
//                            tvStudyDetailTitle.text = response.title
//                                when(response.meetIdx)
//                                {
//                                    0->{
//                                        tvStudySetTime.text ="주"+"${response.frequency}"+"회"
//                                    }
//                                    1->{
//                                        tvStudySetTime.text ="월"+"${response.frequency}"+"회"
//                                    }
//                                    2->{
//                                        tvStudySetTime.text ="${response.periods_content}"
//                                    }
//                                }
//                            tvStudySetDetail.text = response.topic_content
        //                    tvStudySetDetailContent.text= response.groupContent
//                            }
//                        }
//                    }
//                }
//                else {
//                    Log.e("summer", "전달실패 code = ${response.code()}")
//                    Log.e("summer", "전달실패 msg = ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<StudyFindResponse>, t: Throwable) {
//                Log.e("summer", "onFailure t = ${t.toString()}")
//                Log.e("summer", "onFailure msg = ${t.message}")
//            }
//        })
    }

    // 신청서 내용 보내기 retrofit 부분 (API 나오면 작성)
    fun postData(groupIdx:Long?, applyContent:String?, UserIdx:Int)
    {


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity_ = activity as MainActivity
    }

    fun hideKeyboard(editText: EditText){
        val mInputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(
            editText.getWindowToken(),
            0
        )
    }
}