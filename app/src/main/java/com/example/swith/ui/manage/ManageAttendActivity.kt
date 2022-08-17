package com.example.swith.ui.manage

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityManageAttendBinding
import com.example.swith.databinding.ItemManageAttendSpinnerBinding

class ManageAttendActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityManageAttendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_attend)
        initView()
    }

    private fun initView(){
        binding.clickListener = this
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

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_basic_toolbar_back -> finish()
        }
    }

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }
}