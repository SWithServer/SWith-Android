import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.api.RetrofitService
import com.example.swith.data.api.SwithService
import com.example.swith.databinding.FragmentStudyFindBinding
import com.example.swith.domain.entity.StudyFindFilter
import com.example.swith.domain.entity.StudyFindResponse
import com.example.swith.domain.entity.StudyFindReq
import com.example.swith.ui.MainActivity
import com.example.swith.ui.adapter.StudyFindRVAdapter
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.ui.study.find.StudyFindDetailFragment
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.StudyFindViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class StudyFindFragment() : BaseFragment<FragmentStudyFindBinding>(R.layout.fragment_study_find) {
    private val viewModel : StudyFindViewModel by viewModels()
    private var searchFilter = -1 // spinner 사용자 터치시에만 반응 flag
    private var totalCount = 0 // 전체 아이템 개수
    private var isNext = false // 다음 페이지 유무

    private lateinit var adapter : StudyFindRVAdapter
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private var mainActivity: MainActivity? = null

    private var _studyFilter = MutableLiveData<StudyFindFilter>().apply{
        value = StudyFindFilter(null,null,null,null,0)
    }
    private val studyFilter : LiveData<StudyFindFilter>
        get() = _studyFilter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisiblebar(backButton = false, noticeButton = true, title = "", midTitle = "")
        binding.rvStudyFind.adapter = adapter
        initListener()
        setSpinner()

        adapter.setItemClickListener(object:StudyFindRVAdapter.OnItemClickListener{
            override fun onClick(view: View, pos:Int, groupIdx:Long, applicationMethod:Int) {
                Log.e("그룹 idx 값","$groupIdx")
                mainActivity?.goDetailPage(groupIdx,applicationMethod, StudyFindDetailFragment())
            }
        })

        viewModel.contentLiveData.observe(viewLifecycleOwner){
            totalCount=it.numberOfElements
            isNext = !(it.last)
            adapter.setData(it.content)
        }

        studyFilter.observe(viewLifecycleOwner){
          viewModel.loadData(it.title,it.regionIdx,it.interest1,it.interest2,it.sort)
          initScrollListener(it.title,it.regionIdx,it.interest1, it.interest2,it.sort)
        }
    }

    private fun loadMoreData(title:String?,region:String?, interest1:Int?, interest2:Int?, sort:Int,groupIdx:Long){
        adapter.setLoadingView(true)
        Handler().postDelayed({
             viewModel.loadMoreData(title,region,interest1,interest2,sort,groupIdx)
             if(viewModel.loadMoreFlag.value==true){
                 adapter.setLoadingView(false)
             }
        },1000)
    }

    private fun initListener() {
        with(binding)
        {
            rvStudyFind.apply {
                activityResultLauncher = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == AppCompatActivity.RESULT_OK) {
                        val region = result.data!!.getCharSequenceExtra("지역")
                        if (region!! == "선택안함")
                        {
                            binding.btnSelectRegion.text= "지역선택"
                            _studyFilter.value?.regionIdx=null
                        }
                        else{
                            binding.btnSelectRegion.text = region
                            _studyFilter.value?.regionIdx= region.toString()
                        }
                    }
                }
            }

            etStudySearch.setOnKeyListener { view, code, event ->
                if( (event.action == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER)
                    && !etStudySearch.text.equals("")){
                    _studyFilter.value?.title = etStudySearch.text.toString()
                    hideKeyboard(etStudySearch)
                    Log.e("키보드 data load","true")
                    true
                }
                else{
                    false
                }
            }

            ivSearchIcon.setOnClickListener {
                if (!etStudySearch.text.isNullOrBlank() && !etStudySearch.text.equals(""))
                {
                    Log.e("타이틀 data load","true")
                    _studyFilter.value?.title = etStudySearch.text.toString()
                    hideKeyboard(etStudySearch)
                }
            }
            btnSelectRegion.setOnClickListener {
                Intent(requireActivity(), SelectPlaceActivity::class.java)
                    .putExtra("번호", 3).apply{
                    activityResultLauncher.launch(this)
                }
            }

            spinnerInterest1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (searchFilter == 1)
                    {
                        if (position == 0) _studyFilter.value?.interest1 =null
                        else _studyFilter.value?.interest1 =position
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            spinnerInterest1.setOnTouchListener { v, _ ->
                v.performClick()
                searchFilter = 1
                false
            }
            spinnerInterest2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (searchFilter == 2)
                    {
                        if (position == 0) _studyFilter.value?.interest2 =null
                        else _studyFilter.value?.interest2 =position
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            spinnerInterest2.setOnTouchListener{ v, event ->
                v.performClick()
                searchFilter = 2
                false
            }
            spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    Log.e("정렬 data load","true")
                    _studyFilter.value?.sort=position
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun hasNextPage(): Boolean {
        return isNext
    }

    private fun setHasNextPage(b: Boolean) {
        isNext = b
    }

    private fun initScrollListener(title:String?,region:String?, interest1:Int?, interest2:Int?, sort:Int) {
        binding.rvStudyFind.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvStudyFind.layoutManager
                val lastVisibleItem = (layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()

                // hasNextPage() -> 다음 페이지가 있고, 마지막으로 보여지는 item이 리스트의 마지막일때 실행문.
                if (hasNextPage()&&lastVisibleItem==adapter.getData().size-1) {
                    Log.e("hasNextPage()","true")
                    val lastGroupIdx = adapter.getData()[adapter.getData().size-1]?.groupIdx!!.toLong()
                    Log.e("last groupIdx","$lastGroupIdx")
                    loadMoreData(title,region,interest1,interest2,sort,lastGroupIdx)
                    setHasNextPage(false)
                }
            }
        })
    }

    private fun setSpinner(){
        with(binding){
            spinnerSort.adapter = ArrayAdapter.createFromResource(
                activity!!.applicationContext,
                R.array.deadLineOrLatest,
                R.layout.item_search_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
            }
            spinnerInterest1.adapter = ArrayAdapter.createFromResource(
                activity!!.applicationContext,
                R.array.intersting,
                R.layout.item_search_spinner
            ).apply{
                this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
            }
            spinnerInterest2.adapter = ArrayAdapter.createFromResource(
                activity!!.applicationContext,
                R.array.intersting,
                R.layout.item_search_spinner
            ).apply {
                this.setDropDownViewResource(R.layout.item_search_spinner_dropdown)
            }
        }
    }

    private fun hideKeyboard(editText: EditText){
        val mInputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(
            editText.getWindowToken(),
            0
        )
    }
}