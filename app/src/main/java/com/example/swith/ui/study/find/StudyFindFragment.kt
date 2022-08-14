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


class StudyFindFragment() : BaseFragment<FragmentStudyFindBinding>(R.layout.fragment_study_find) {

   private var totalCount = 0 // 전체 아이템 개수
    private var isNext = false // 다음 페이지 유무
    private var page =0 // 현재 페이지
    private var limit =6 // 한번에 가져올 아이템 개수
    private lateinit var adapter : StudyFindRVAdapter

    var activity_: MainActivity? = null
    var regionCode: Long = -1
    var regionName: String = ""

    var search_title : String ?= null
    var select_region : Long?=null
    var select_interest1 : Int?=null
    var select_interest2 : Int?=null
    var select_sort : Int=0 // 0이면 마감일 1이면 최신순 이런식


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        loadData(search_title,select_region, select_interest1, select_interest2, select_sort)
//        initScrollListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StudyFindRVAdapter()
        binding.rvStudyFind.adapter = adapter
        binding.rvStudyFind.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setData(setData())

        adapter.setItemClickListener(object:StudyFindRVAdapter.OnItemClickListener{
            override fun onClick(view: View, pos:Int) {
                Log.e("클릭이벤트 발생","true")
                activity_?.goDeatailPage(1,StudyFindDetailFragment())
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
                    regionCode = code.toString().toLong()
                    select_region = regionCode
                   // loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
                }
            }
            with(binding)
            {
                etStudySearch.setOnKeyListener { view, code, event ->
                    if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER) && !etStudySearch.text.equals("")){
                        search_title = etStudySearch.text.toString()
                        hideKeyboard(etStudySearch)
//                        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
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
//                        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
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
                        select_interest1=position
//                        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
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
                        select_interest2=position
//                        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
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
//                        loadData(search_title,select_region,select_interest1,select_interest2,select_sort)
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
        }
    }

    //최초로 넣어줄 데이터 load
    fun loadData(title:String?,region:Long?, intrest1:Int?, intrest2:Int?, sort:Int?){
        lateinit var studyList: ArrayList<getStudyResponse>
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        retrofitService.getSearchStudy(getPage(),limit,title,region,intrest1,intrest2,sort).enqueue(object : Callback<getStudyContent> {
            override fun onResponse(
                call: Call<getStudyContent>,
                response: Response<getStudyContent>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        studyList = this.result
                        totalCount = studyList.size
                        isNext = this.is_Next
                        adapter.setData(studyList)
                    }
                }
                else {
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

    fun loadMoreData(title:String?,region:Long?, intrest1:Int?, intrest2:Int?, sort:Int?)
    {
        adapter.setLoadingView(true)
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        val handler = android.os.Handler()
        handler.postDelayed({
            retrofitService.getSearchStudy(getPage(), limit,title,region, intrest1, intrest2, sort,)
                .enqueue(object : Callback<getStudyContent> {
                    override fun onResponse(call: Call<getStudyContent>, response: Response<getStudyContent>) {
                        val body = response.body()
                        if (body != null && response.isSuccessful) {
                            totalCount = body.total_count
                            isNext = body.is_Next
                            adapter.run {
                                setLoadingView(false)
                                addData(body.result)
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

                // hasNextPage() -> 다음 페이지가 있는 경우
                if (hasNextPage()) {
                    val lastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()

                    // 마지막으로 보여진 아이템 position 이
                    // 전체 아이템 개수보다 5개 모자란 경우, 데이터를 loadMore 한다
                    if (layoutManager.itemCount <= lastVisibleItem + 5) {
                        loadMoreData(search_title,select_region, select_interest1, select_interest2, select_sort)
                        setHasNextPage(false)
                    }
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

    fun setData():ArrayList<getStudyResponse>
    {
        var mDatas = ArrayList<getStudyResponse>()
        mDatas.apply{
            add( getStudyResponse("부천 인천 중급 영어",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("김해 중국어",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("서울 강서구 알고리즘",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("인천 만수동 면접스터디",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("부천 인천 중급 영어",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("김해 중국어",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("서울 강서구 알고리즘",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("인천 만수동 면접스터디",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("부천 인천 중급 영어",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("김해 중국어",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("서울 강서구 알고리즘",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
            add( getStudyResponse("인천 만수동 면접스터디",12345,12345,"2022-08-10","2022-08-10",5,"인원모집합니다"))
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