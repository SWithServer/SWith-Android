package com.example.swith.ui.manage

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageAttendBinding
import com.example.swith.databinding.ItemManageAttendSpinnerBinding
import com.example.swith.utils.ToolBarManager

class ManageAttendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageAttendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_attend)
        initView()
    }

    private fun initView(){
        ToolBarManager(this).initToolBar(binding.toolbarManageAttend, false, backVisible = true)
        val stringList = ArrayList<String>()
        stringList.apply {
            add("1회차")
            add("2회차")
            add("3회차")
            add("-- 회차 선택 --")
        }

        binding.spinnerManageAttend.apply {
            adapter = object: ArrayAdapter<String>(this@ManageAttendActivity, R.layout.item_manage_attend_spinner){
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)

                    val spinnerBinding: ItemManageAttendSpinnerBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_manage_attend_spinner, parent, false)
                    // 마지막 포지션의 텍스트를 hint로 사용
                    if (position == count){
                        with(spinnerBinding.spinnerSessionNum) {
                            text = ""
                            hint = getItem(count)
                            text
                        }
                    }
                    return view
                }

                override fun getCount(): Int {
                    return super.getCount() - 1
                }
            }.apply {
                addAll(stringList)
            }
            setSelection(adapter.count)
            // droplist를 spinner와 간격을 두고 나오도록
            dropDownVerticalOffset = dipToPixels(41f).toInt()
            // 스피너 선택시 변하는 로직
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }
}