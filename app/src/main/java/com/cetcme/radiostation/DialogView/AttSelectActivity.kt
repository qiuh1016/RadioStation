package com.cetcme.radiostation.DialogView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import com.cetcme.radiostation.R
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView
import kotlinx.android.synthetic.main.activity_att_select.*

class AttSelectActivity : Activity() {

    var attState:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_att_select)

        initView()
        setRadioData()
    }

    fun initView() {
        titleView.setBackgroundResource(R.drawable.top_select)
        titleView.setTitle("ATT设置")
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

        attState = this.intent.getIntExtra("attState",0)
        Log.i("kt", " MNumber: $attState")

        when (attState) {
            0 -> radioButton0.toggle()
            1 -> radioButton1.toggle()
            2 -> radioButton2.toggle()
        }


    }

    fun confirmButtonTapped(v: View) {
        var attNewState: Int = -1
        if (radioButton0.isChecked) {
            attNewState = 0
        } else if (radioButton1.isChecked) {
            attNewState = 1
        } else if (radioButton2.isChecked) {
            attNewState = 2
        }

        val mIntent = Intent()
        mIntent.putExtra("attState", attNewState)

        // 设置结果，并进行传送
        this.setResult(3, mIntent)
        onBackPressed()
    }

    fun cancelButtonTapped(v: View) {
//        this.setResult(0, null)
        onBackPressed()
    }
}
