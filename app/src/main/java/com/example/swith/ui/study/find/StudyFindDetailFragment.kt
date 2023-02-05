package com.example.swith.ui.study.find

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.swith.R
import com.example.swith.SwithApplication
import com.example.swith.data.api.RetrofitService
import com.example.swith.databinding.DialogApplicationBinding
import com.example.swith.databinding.DialogCreateBinding
import com.example.swith.databinding.FragmentStudyFindDetailBinding
import com.example.swith.domain.entity.StudyApplicationResponse
import com.example.swith.domain.entity.StudyDetailResponse
import com.example.swith.domain.entity.postApplicationReq
import com.example.swith.ui.MainActivity
import com.example.swith.ui.dialog.CustomDialog
import com.example.swith.ui.manage.ManageUserProfileActivity
import com.example.swith.utils.CustomBinder
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.StudyFindDetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyFindDetailFragment :
    BaseFragment<FragmentStudyFindDetailBinding>(R.layout.fragment_study_find_detail),
    MainActivity.onBackPressedListener {
    private val viewModel:StudyFindDetailViewModel by viewModels()
    private var groupIdx: Long? = -1
    private var applicationMethod: Int? = -1
    private var adminIdx: Long? = -1
    private var mainActivity: MainActivity? = null
    private val userIdx: Long =
        if (SwithApplication.spfManager.getLoginData() != null) SwithApplication.spfManager.getLoginData()?.userIdx!! else 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupIdx = arguments?.getLong("groupIdx", 0)
        applicationMethod = arguments?.getInt("applicationMethod", 0)
        Log.e("summer", "groupIdx = $groupIdx")
        Log.e("summer", "applicationMethod = $applicationMethod")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStudyInfo(groupIdx)
        setData()
        setVisiblebar(true, false, "", "")

        binding.btnStudyApply.setOnClickListener {
            if(applicationMethod==0){
                showLastDialog(null)
            }
            else{
                applicationDialog()
            }
        }

        binding.btnAdmin.setOnClickListener {
            //TODO API 물어보기
        }

    }
    override fun onBackPressed() {
        mainActivity?.goSearchPage()
    }

    private fun setData() {
        viewModel.studyInfo.observe(viewLifecycleOwner) {
            with(binding) {
                adminIdx = it.adminIdx
                flLoadingLayout.visibility = View.GONE
                if (it.applicationMethod == 0) tvStudySetApplyMethod.text = "선착순"
                else tvStudySetApplyMethod.text = "지원"

                tvStudySetApplicationStatus.text = "${it.numOfApplicants}/${it.memberLimit}"

                if (it.regionIdx1 == null) {
                    tvStudySetPlace1.text = "온라인"
                    tvStudySetPlace2.visibility = View.INVISIBLE
                } else tvStudySetPlace2.visibility = View.VISIBLE

                when (it.meet) {
                    0 -> tvStudySetTime.text = "주 ${it.frequency}회"
                    1 -> tvStudySetTime.text = "월 ${it.frequency}회"
                    2 -> tvStudySetTime.text = "${it.periods}"
                }
                Glide.with(requireActivity())
                    .load(it.groupImgURL)
                    .fitCenter()
                    .fallback(R.drawable.bg_create_sample)
                    .error(R.drawable.bg_create_sample)

                when (it.interest) {
                    1 -> tvStudySetCategory.text = "자격증/시험"
                    2 -> tvStudySetCategory.text = "어학"
                    3 -> tvStudySetCategory.text = "청소년/입시"
                    4 -> tvStudySetCategory.text = "취업/창업"
                    5 -> tvStudySetCategory.text = "컴퓨터/IT"
                    6 -> tvStudySetCategory.text = "취미/문화"
                    7 -> tvStudySetCategory.text = "면접"
                }
                tvStudySetPlaytime.text = it.groupStart + " ~ " + it.groupEnd
                tvStudySetDeadline.text = it.recruitmentEndDate + " 까지"
            }
        }
    }

    private fun applicationDialog(){
        DataBindingUtil.inflate<DialogApplicationBinding>(
            LayoutInflater.from(requireContext()),R.layout.dialog_application,null,false
        ).apply{
            this.dialog = CustomDialog(requireActivity(),
                root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            ).apply{
                this.window?.attributes?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                this.setClickListener(object:CustomDialog.DialogClickListener {
                    override fun onConfirm() {
                        var applicationContent = findViewById<EditText>(R.id.et_application).toString()
                        showLastDialog(applicationContent)
                    }

                    override fun onClose() {
                        TODO("Not yet implemented")
                    }
                })
                show()
            }
        }
    }

    //최종 제출 Dialog
    private fun showLastDialog(applyContent: String?) {
        Log.e("신청서 내용", "${applyContent}")
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
                        postData(groupIdx,applyContent,userIdx)
                    }
                })
        }
    }

    // 신청서 내용 보내기
    private fun postData(groupIdx: Long?, applyContent: String?, UserIdx: Long) {
        viewModel.postApplication(groupIdx,postApplicationReq(UserIdx, applyContent)).apply{
            if(this==-1){
                Toast.makeText(requireActivity(),"다시 시도해주세요",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireActivity(),"신청이 완료되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}