package com.cetcme.radiostation.DialogView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import com.cetcme.radiostation.R
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView
import kotlinx.android.synthetic.main.activity_ssb_select.*

class SsbSelectActivity : Activity() {

    var ssbState:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ssb_select)

        initView()
        setRadioData()
    }

    fun initView() {
        titleView.setBackgroundResource(R.drawable.top_select)
        titleView.setTitle("模式选择")
        titleView.setClickCallback(object : QHTitleView.ClickCallback {
            override fun onBackClick() {
                //
            }
            override fun onRightClick() {
                //
            }
        })
    }

    fun setRadioData() {

        ssbState = this.intent.getIntExtra("ssbState",0)
        Log.i("kt", " MNumber: $ssbState")

        when (ssbState) {
            0 -> radioButton0.toggle()
            1 -> radioButton1.toggle()
            2 -> radioButton2.toggle()
            3 -> radioButton3.toggle()
        }


    }

    fun confirmButtonTapped(v: View) {
        var ssbNewState: Int = -1
        if (radioButton0.isChecked) {
            ssbNewState = 0
        } else if (radioButton1.isChecked) {
            ssbNewState = 1
        } else if (radioButton2.isChecked) {
            ssbNewState = 2
        } else if (radioButton3.isChecked) {
            ssbNewState = 3
        }

        val mIntent = Intent()
        mIntent.putExtra("ssbState", ssbNewState)

        // 设置结果，并进行传送
        this.setResult(1, mIntent)
        onBackPressed()
    }

    fun cancelButtonTapped(v: View) {
//        this.setResult(0, null)
        onBackPressed()
    }
}
