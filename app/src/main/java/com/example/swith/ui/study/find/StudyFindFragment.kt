package com.example.swith.ui.study.find

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.*
import com.example.swith.databinding.FragmentStudyFindBinding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.MainActivity
import com.example.swith.ui.adapter.StudyFindRVAdapter
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.utils.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class StudyFindFragment() : BaseFragment<FragmentStudyFindBinding>(R.layout.fragment_study_find) {

    private var const = 0
    private var totalCount = 0 // 전체 아이템 개수
    private var isNext = false // 다음 페이지 유무
    private var page =0 // 현재 페이지
    private val limit =5 // 한번에 가져올 아이템 개수

    private lateinit var adapter : StudyFindRVAdapter

    var activity_: MainActivity? = null
    var regionCode: Long = -1
    var regionName: String = ""

    var search_title : String ?= null
    var select_region : String?=null
    var select_interest1 : Int?=null
    var select_interest2 : Int?=null
    var select_sort : Int=0 // 0이면 마감일 1이면 최신순 이런식
    var date = LocalDateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisiblebar(false,true,"")

        firstLoad()

        adapter = StudyFindRVAdapter()
        binding.rvStudyFind.adapter = adapter

        Log.e("시간값",date.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

        adapter.setItemClickListener(object:StudyFindRVAdapter.OnItemClickListener{
            override fun onClick(view: View, pos:Int,groupIdx:Long,applicationMethod:Int) {
                Log.e("클릭이벤트 발생","true")
                Log.e("그룹 idx 값","${groupIdx}")
                activity_?.goDeatailPage(groupIdx,applicationMethod,StudyFindDetailFragment())
            }
        })

        binding.rvStudyFind.apply {
            val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val data = result.data
                    val region = data!!.getCharSequenceExtra("지역")
                    val code = data!!.getCharSequenceExtra("코드")
                    binding.btnSelectRegion.text = region
                    regionName = region.toString()
                    select_region = regionName
                    loadData(search_title,select_region, select_interest1, select_interest2, select_sort)
                    initScrollListener()
                }
            }
            with(binding)
            {
                etStudySearch.setOnKeyListener { view, code, event ->
                    if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudySearch.text.equals("")){
                        search_title = etStudySearch.text.toString()
                        hideKeyboard(etStudySearch)
                        loadData(search_title,select_region, select_interest1, select_interest2, select_sort)
                        initScrollListener()
                        true
                    }
                    else{
                        false
                    }
                }

                ivSearchIcon.setOnClickListener {
                    if (!etStudySearch.text.isNullOrBlank() && !etStudySearch.text.equals(""))
                    {
                        search_title = etStudySearch.text.toString()
                        hideKeyboard(etStudySearch)
                        loadData(search_title,select_region, select_interest1, select_interest2, select_sort)
                        initScrollListener()
                    }
                }

                btnSelectRegion.setOnClickListener {
                    var intent = Intent(requireActivity(), SelectPlaceActivity::class.java)
                    intent.putExtra("번호", 3)
                    activityResultLauncher.launch(intent)
                }
                spinnerInterest1.adapter = ArrayAdapter.createFromResource(
                    activity!!.applicationContext,
                    R.array.intersting,
                    R.layout.item_search_spinner
                ).apply{
                    this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
                }
                spinnerInterest1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        if (position != 0)
                        {
                            select_interest1=position
                            loadData(search_title,select_region, select_interest1, select_interest2, select_sort)
                            initScrollListener()
                        }
                        else{
                            if (const != 0) //최초 실행이 아닐때(const!=0)의 선택은 분류를 null처리해서 데이터 로드 실행하기.
                            {
                                select_interest1=null
                                loadData(search_title,select_region, select_interest1, select_interest2, select_sort)
                                initScrollListener()
                            }
                        } //무한스크롤 position 이슈 수정부분
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                spinnerInterest2.adapter = ArrayAdapter.createFromResource(
                    activity!!.applicationContext,
                    R.array.intersting,
                    R.layout.item_search_spinner
                ).apply{
                    this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
                }
                spinnerInterest2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        if (position != 0)
                        {
                            select_interest1=position
                            loadData(search_title,select_region, select_interest1, select_interest2, select_sort)
                            initScrollListener()
                        } //무한스크롤 position 이슈 수정 이전
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                spinnerSort.adapter = ArrayAdapter.createFromResource(
                    activity!!.applicationContext,
                    R.array.deadLineOrLatest,
                    R.layout.item_search_spinner
                ).apply{
                    this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
                }
                spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        select_sort=position
                       loadData(search_title,select_region, select_interest1, select_interest2, select_sort)
                        initScrollListener()
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
        }
    }

    fun firstLoad()
    {
        //최초실행 확인 함수
        if (const<1)
        {
            const++
        }
    }

    //최초로 넣어줄 데이터 load
    fun loadData(title:String?,region:String?, interest1:Int?, interest2:Int?, sort:Int){
        lateinit var studyList: ArrayList<Content>
        var req = studyReqest(title,region, null,interest1, interest2,sort, LocalDateTime.now())
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.getSearchStudy(limit,title,region,interest1,interest2,null,sort,date.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).enqueue(object : Callback<StudyFindResponse> {
            override fun onResponse(
                call: Call<StudyFindResponse>,
                response: Response<StudyFindResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        Log.e("summer","${this.result.content.toString()}")
                        Log.e("summer","${this.result.toString()}")
                        totalCount = this.result.numberOfElements
                        isNext = !(this.result.last)
                        Log.e("load first isNext","$isNext")
                        adapter.setData(this.result.content)
                    }
                }
                else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                    Log.e("summer","${req.toString()}")
                }
            }
            override fun onFailure(call: Call<StudyFindResponse>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
    }

    //리사이클러뷰에 더 보여줄 데이터를 로드하는 경우
    fun loadMoreData(title:String?,region:String?, interest1:Int?, interest2:Int?, sort:Int,groupIdx:Long?)
    {
        Log.e("loadMore","true")
        var req = studyReqest(title,region, groupIdx,interest1, interest2,sort, LocalDateTime.now())
        adapter.setLoadingView(true)
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        val handler = android.os.Handler()
        handler.postDelayed({
            retrofitService.getSearchStudy(limit,title,region,interest1,interest2,groupIdx,sort, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .enqueue(object : Callback<StudyFindResponse> {
                    override fun onResponse(call: Call<StudyFindResponse>, response: Response<StudyFindResponse>) {
                        val body = response.body()
                        if (body != null && response.isSuccessful) {
                            Log.e("groupIdx 데이터 더 받을때","${groupIdx}")
                            Log.e("summer","load more data")
                            Log.e("summer","${body.result.content.toString()}")
                            totalCount = body.result.numberOfElements
                            Log.e("body last 값", "${body.result.last}")
                            isNext = !(body.result.last)
                            Log.e("(loadMore)isNext","$isNext")
                            adapter.run{
                                setLoadingView(false)
                                addData(body.result.content)
                            }
                        } else {
                            Log.e("summer","load more data")
                            Log.e("summer", "전달실패 code = ${response.code()}")
                            Log.e("summer", "전달실패 msg = ${response.message()}")
                            Log.e("summer","${req.toString()}")
                        }
                    }
                    override fun onFailure(call: Call<StudyFindResponse>, t: Throwable) {
                        Log.e("summer","load more data")
                        Log.e("summer", "onFailure t = ${t.toString()}")
                        Log.e("summer", "onFailure msg = ${t.message}")
                    }
                })
        }, 1000)
    }

    fun getPage(): Int {
        page++
        return page
    }

    fun hasNextPage(): Boolean {
        return isNext
    }

    fun setHasNextPage(b: Boolean) {
        isNext = b
    }

    private fun initScrollListener() {
        binding.rvStudyFind.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvStudyFind.layoutManager

                val lastVisibleItem = (layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()

                // hasNextPage() -> 다음 페이지가 있고, 마지막으로 보여지는 item이 리스트의 마지막일때 실행문.
                if (hasNextPage()&&lastVisibleItem==adapter.getData().size-1) {
                    Log.e("hasNextPage()","true")
                    val last_groupIdx = adapter.getData()[adapter.getData().size-1]?.groupIdx
                    Log.e("last groupIdx","$last_groupIdx")
                    Log.e("if문","true")
                        loadMoreData(search_title,select_region, select_interest1, select_interest2, select_sort,last_groupIdx)
                        setHasNextPage(false)
                }
            }
        })
    }

    fun hideKeyboard(editText: EditText){
        val mInputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(
            editText.getWindowToken(),
            0
        )
    }

    //임시데이터
    fun setData():List<Content>
    {
        var mDatas = ArrayList<Content>()
        mDatas.apply{
          add(Content(1,"인천 만수동 스터디","인원을 모집합니다","인천광역시 남동구","인천광역시 남동구",listOf(2022,8,21),0,listOf(0),0,1))
            add(Content(2,"김해 스터디","인원을 모집합니다","인천광역시 남동구","인천광역시 남동구",listOf(2022,8,21),0,listOf(0),0,0))
            add(Content(3,"서울 강서구 스터디","인원을 모집합니다","인천광역시 남동구","인천광역시 남동구",listOf(2022,8,21),0,listOf(0),0,1))
            add(Content(4,"인천 용현동 스터디","인원을 모집합니다","인천광역시 남동구","인천광역시 남동구",listOf(2022,8,21),0,listOf(0),0,1))
            add(Content(5,"미추홀구 스터디","인원을 모집합니다","인천광역시 남동구","인천광역시 남동구",listOf(2022,8,21),0,listOf(0),0,0))
        }
        return mDatas
    }

    override fun onResume() {
        super.onResume()
        Log.e("resume", "True")
        Log.e("regionCode", "${regionCode}")
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity_ = activity as MainActivity
    }
}