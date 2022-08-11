package com.example.swith.ui.study.find

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.swith.R
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyResponse
import com.example.swith.databinding.FragmentStudyFindDetailBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.StudyFindViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyFindDetailFragment : BaseFragment<FragmentStudyFindDetailBinding>(R.layout.fragment_study_find_detail) {
    var title :String=""

    lateinit var dialog_ :Dialog
    lateinit var lastDialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastDialog = Dialog(requireActivity())
        lastDialog.setContentView(R.layout.fragment_dialog)
        dialog_ = Dialog(requireActivity())
        dialog_.setContentView(R.layout.fragment_dialog_application)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("fragment이동", "true")
        title = arguments?.getString("title").toString()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    with(binding)
    {
        btnStudyApply.setOnClickListener {
            createApplication()
        }
    }
    }

    fun createApplication (){
        dialog_.show()
        dialog_.findViewById<Button>(R.id.btn_application_apply).setOnClickListener {
           //신청서 작성 내용 변수
            var application_content = dialog_.findViewById<EditText>(R.id.et_application).toString()
            lastDialog.findViewById<TextView>(R.id.tv_confirm).text = "제출하시겠습니까?"
            lastDialog.show()

            lastDialog.findViewById<Button>(R.id.btn_no).setOnClickListener {
                lastDialog.dismiss()
            }
            lastDialog.findViewById<Button>(R.id.btn_yes).setOnClickListener {
                // 예스시 어떻게 처리할지.. retrofit 지원 내용, user idx post
                lastDialog.dismiss()
            }
        }
    }
}