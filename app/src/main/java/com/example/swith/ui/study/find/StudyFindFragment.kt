package com.example.swith.ui.study.find

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat.apply
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.*
import com.example.swith.databinding.FragmentStudyFindBinding
import com.example.swith.databinding.FragmentStudyFindDetailBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.MainActivity
import com.example.swith.ui.adapter.HomeStudyRVAdapter
import com.example.swith.ui.adapter.StudyFindRVAdapter
import com.example.swith.ui.profile.ProfileFragment.Companion.newInstance
import com.example.swith.ui.study.StudyActivity
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.StudyFindViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter


class StudyFindFragment : BaseFragment<FragmentStudyFindBinding>(R.layout.fragment_study_find) {
   // private val viewModel: StudyFindViewModel by viewModels()
    // lateinit var studyFindAdapter : StudyFindRVAdapter

    var activity_: MainActivity? = null
    var regionCode: Long = -1
    var regionName: String = ""
    lateinit var sort_recently : ArrayList<Long>
    lateinit var sort_deadline : ArrayList<Long>


    var search_title : String ?= null
    var select_region : Long?=null
    var select_interest1 : Int?=null
    var select_interest2 : Int?=null
    var select_sort : Int=0 // 0이면 마감일 1이면 최신순 이런식

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity_ = activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStudyFind.apply {
            val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val data = result.data
                    val region = data!!.getCharSequenceExtra("지역")
                    val code = data!!.getCharSequenceExtra("코드")
                    binding.tvSelectRegion.text = region
                    regionCode = code.toString().toLong()
                    select_region = regionCode
                    loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
                }
            }
            with(binding)
            {
                if (etStudySearch.text.isNotEmpty()){
                    search_title=etStudySearch.text.toString()
                    loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
                }

                tvSelectRegion.setOnClickListener {
                    var intent = Intent(requireActivity(), SelectPlaceActivity::class.java)
                    intent.putExtra("번호", 3)
                    activityResultLauncher.launch(intent)
                }
                spinnerInterest1.adapter = ArrayAdapter.createFromResource(
                    activity!!.applicationContext,
                    R.array.intersting,
                    android.R.layout.simple_spinner_item
                )
                spinnerInterest1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        select_interest1=position
                        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                spinnerInterest2.adapter = ArrayAdapter.createFromResource(
                    activity!!.applicationContext,
                    R.array.intersting,
                    android.R.layout.simple_spinner_item
                )
                spinnerInterest2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        select_interest2=position
                        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                spinnerSort.adapter = ArrayAdapter.createFromResource(
                    activity!!.applicationContext,
                    R.array.deadLineOrLatest,
                    android.R.layout.simple_spinner_item
                )
                spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        select_sort=position
                        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
        }
    }

    fun setAdapter(studyList: ArrayList<getStudyResponse>) {
        binding.rvStudyFind.apply{
            adapter = StudyFindRVAdapter(studyList).apply{
                setItemClickListener(object: StudyFindRVAdapter.OnItemClickListener{
                    override fun onClick(studyList: getStudyResponse) {
                        Log.e("item click","true")
                        activity_?.goDeatailPage(1,StudyFindDetailFragment())
                    }
                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun loadData(search_title:String?,region:Long?, intrest1:Int?, intrest2:Int?, sort:Int?){
        lateinit var studyList: ArrayList<getStudyResponse>
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.getSearchStudy(search_title,region,intrest1,intrest2,sort).enqueue(object : Callback<getStudyContent> {
            override fun onResponse(
                call: Call<getStudyContent>,
                response: Response<getStudyContent>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply{
                        studyList = this.result
                        var listSize = studyList.size
                        when(sort)
                        {
                            0->{
                                for (i: Int in 0..listSize-1)
                                {
                                    for (j : Int in 0..listSize-1)
                                    {
                                        if (studyList[j].deadline.compareTo(studyList[j+1].deadline)>0) //j번째 날짜가 j+1번째 날짜보다 클때
                                        {
                                            var temp = studyList[j]
                                            studyList[j]=studyList[j+1]
                                            studyList[j+1]=temp
                                        }
                                    }
                                }
                            }
                            1->{
                                for (i: Int in 0..listSize-1)
                                {
                                    for (j : Int in 0..listSize-1)
                                    {
                                        if (studyList[j].createDate.compareTo(studyList[j+1].createDate)<0) //j번째 날짜가 j+1번째 날짜보다 작을때
                                        {
                                            var temp = studyList[j]
                                            studyList[j]=studyList[j+1]
                                            studyList[j+1]=temp
                                        }
                                    }
                                }
                            }
                        }
                        setAdapter(studyList)
                        Log.e("summer", "User Data = $studyList")
                    }
                } else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                    Log.e("summer","$search_title $region $intrest1 $intrest2 $sort")
                }
            }
            override fun onFailure(call: Call<getStudyContent>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
    }
    override fun onResume() {
        super.onResume()
        Log.e("resume", "True")
        Log.e("regionCode", "${regionCode}")
    }
    override fun onDetach() {
        super.onDetach()
    }

}