package com.example.swith.ui.study.find

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.data.StudyFindData
import com.example.swith.databinding.FragmentStudyFindBinding
import com.example.swith.ui.adapter.StudyFindRVAdapter
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.StudyFindViewModel


class StudyFindFragment : BaseFragment<FragmentStudyFindBinding>(R.layout.fragment_study_find) {
    private val viewModel: StudyFindViewModel by viewModels()
    // lateinit var studyFindAdapter : StudyFindRVAdapter

    var regionCode : Long = -1
    var regionName : String =""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStudyFind.apply{
            val mDatas = ArrayList<StudyFindData>()
            mDatas.apply{
                add( StudyFindData("부천 인천 중급 영어","인원 모집합니다","인천광역시 남동구 만수동","2022-08-10",3))
                add( StudyFindData("인천 초급 영어","인원 모집합니다","인천광역시 미추홀구 용현동","2022-08-15",5))
                add( StudyFindData("서울 알고리즘 스터디","인원 모집합니다","서울특별시 강서구","2022-09-10",3))
                add( StudyFindData("김해 중국어 스터디","인원 모집합니다","경상남도 김해시 구산동","2022-10-15",4))
                add( StudyFindData("부산 전기기사 스터디 ","인원 모집합니다","부산광역시 해운대구","2022-08-13",9))
            }
            adapter = StudyFindRVAdapter(mDatas)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                val region = data!!.getCharSequenceExtra("지역")
                val code = data!!.getCharSequenceExtra("코드")
                binding.tvSelectRegion.text = region
                regionName = region.toString()
                regionCode = code.toString().toLong()
            }
        }
        with(binding)
        {
            tvSelectRegion.setOnClickListener {
                var intent = Intent(requireActivity(), SelectPlaceActivity::class.java)
                intent.putExtra("번호",3)
                activityResultLauncher.launch(intent)
            }
            spinnerInterest1.adapter = ArrayAdapter.createFromResource(activity!!.applicationContext,
                R.array.intersting,
                android.R.layout.simple_spinner_item
            )

            spinnerInterest2.adapter = ArrayAdapter.createFromResource(activity!!.applicationContext,
                R.array.intersting,
                android.R.layout.simple_spinner_item
            )

            spinnerDeadline.adapter = ArrayAdapter.createFromResource(activity!!.applicationContext,
                R.array.deadLineOrLatest,
                android.R.layout.simple_spinner_item
            )
        }
    }
    override fun onResume() {
        super.onResume()
        Log.e("resume","True")
        Log.e("regionCode","${regionCode}")
    }

}