package com.example.swith.ui.study.create

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.Location
import com.example.swith.databinding.ActivitySelectPlaceBinding
import com.example.swith.ui.adapter.LocationRVAdapter

class SelectPlaceActivity :  AppCompatActivity(){
    lateinit var binding:ActivitySelectPlaceBinding
    private var cityDatas = ArrayList<Location>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySelectPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cityDatas.apply{
            add(Location("서울"))
            add(Location("인천"))
            add(Location("경기"))
            add(Location("강원"))
            add(Location("대전"))
            add(Location("세종"))
            add(Location("충남"))
            add(Location("충북"))
            add(Location("부산"))
            add(Location("울산"))
            add(Location("경남"))
            add(Location("경북"))
            add(Location("전남"))
            add(Location("전북"))
            add(Location("제주"))
        }
        val locationRVAdapter = LocationRVAdapter(cityDatas)
        binding.layoutPlaceValue1.adapter = locationRVAdapter
    }
}