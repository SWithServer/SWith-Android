package com.example.swith.ui.study.create

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.CityResponse
import com.example.swith.data.Location
import com.example.swith.databinding.ActivitySelectPlaceBinding
import com.example.swith.repository.ApiService
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.adapter.LocationAdapter
import com.example.swith.ui.adapter.RegionAdapter
import com.example.swith.ui.profile.ProfileModifyActivity
import com.example.swith.ui.region.CityActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var cityDataList: ArrayList<String> = arrayListOf()
var cityHash = HashMap<String, String>()

var guDataList: ArrayList<String> = arrayListOf()
var guHash = HashMap<String, String>()

var dongDataList: ArrayList<String> = arrayListOf()
var dongHash = HashMap<String, String>()

lateinit var resultData:String

lateinit  var sharedPreference:SharedPreferences
lateinit var editor:SharedPreferences.Editor


class SelectPlaceActivity :  AppCompatActivity(),View.OnClickListener {
    lateinit var binding: ActivitySelectPlaceBinding
    lateinit var locationAdapter: LocationAdapter
    var placeNum:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_place)
        initView()
        initData()
        Log.e("번호","$placeNum")
        test("*00000000", 1)
    }

    private fun initView() {
        placeNum = intent.getIntExtra("번호",-1)
        binding.clickListener = this@SelectPlaceActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_back -> {
                finish()
            }
        }
    }
    fun test(code: String, num: Int) {
        var dataList : ArrayList<String> = arrayListOf()
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitService.REG_CODE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val regionService = retrofit.create(ApiService::class.java)
        regionService.getCityCode(code).enqueue(object : Callback<CityResponse> {
            override fun onResponse(
                call: Call<CityResponse>,
                response: Response<CityResponse>
            ) {
                Log.e("doori", response.toString())
                response.body()?.apply {
                    val regResponse = this as CityResponse
                    val regcodes = regResponse.regcodes
                    Log.e("doori", response.toString())
                    when (num) {
                        1 -> {
                            cityDataList.clear()
                            for (a in regcodes) {
                                Log.e("doori", "이름 : ${a.name} , 코드 : ${a.code}")
                                cityDataList.add(a.name)
                                cityHash.put(a.name, a.code)
                            }
                            adapter_create(cityDataList,1)
                        }
                        2 -> {
                            guDataList.clear()
                            for (a in regcodes) {
                                Log.e("doori", "이름 : ${a.name} , 코드 : ${a.code}")
                                guDataList.add(a.name)
                                guHash.put(a.name, a.code)
                            }
                            adapter_create(guDataList,2)
                        }
                        3 -> {
                            dongDataList.clear()
                            for (a in regcodes) {
                                Log.e("doori", "이름 : ${a.name} , 코드 : ${a.code}")
                                dongDataList.add(a.name)
                                dongHash.put(a.name, a.code)
                            }
                            adapter_create(dongDataList,3)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                Log.e("doori", t.toString())
            }
        })
    }
    fun adapter_create(dataList: ArrayList<String>, num: Int){
        val adapter = LocationAdapter(dataList)
        var code: String? = ""
        when (num) {
            1 -> {
                binding.rvCity.adapter = adapter
                binding.rvCity.layoutManager = LinearLayoutManager(this@SelectPlaceActivity)
                adapter.setItemClickListener(object : LocationAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        val city = adapter.getName(position)
                        Log.e("doori", "선택한 값은 $city")
                        code = cityHash.get(city)
                        Log.e("doori", "코드 값은 $code")
                        binding.rvGu.visibility=View.VISIBLE
                        test("${code}".substring(0,2)+"*000000",2)
                    }
                })
            }
            2 -> {
                binding.rvGu.adapter = adapter
                binding.rvGu.layoutManager = LinearLayoutManager(this@SelectPlaceActivity)
                adapter.setItemClickListener(object : LocationAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        val city = adapter.getName(position)
                        Log.e("doori", "선택한 값은 $city")
                        code = guHash.get(city)
                        Log.e("doori", "코드 값은 $code")
                        binding.rvDong.visibility=View.VISIBLE
                        test("${code}".substring(0,4)+"*",3)
                    }
                })
            }
            3 -> {
                binding.rvDong.adapter = adapter
                binding.rvDong.layoutManager = LinearLayoutManager(this@SelectPlaceActivity)
                adapter.setItemClickListener(object : LocationAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        val city = adapter.getName(position)
                        Log.e("doori", "선택한 값은 $city")
                        code = dongHash.get(city)
                        Log.e("doori", "코드 값은 $code")
                        sharedPreference = getSharedPreferences("result${placeNum}",0)
                        editor= sharedPreference.edit()
                        editor.putString("이름${placeNum}", "${city}")
                        editor.putString("코드${placeNum}","${code}")
                        editor.apply()
                        finish()
                    }
                })
            }
        }
    }
}
