package com.cetcme.radiostation.DialogView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import com.cetcme.radiostation.R
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView
import kotlinx.android.synthetic.main.activity_pow_select.*

class PowSelectActivity : Activity() {

    var powState:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pow_select)

        initView()
        setRadioData()
    }

    fun initView() {
        titleView.setBackgroundResource(R.drawable.top_select)
        titleView.setTitle("POW设置")
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

        powState = this.intent.getIntExtra("powState",0)
        Log.i("kt", " MNumber: $powState")

        when (powState) {
            0 -> radioButton0.toggle()
            1 -> radioButton1.toggle()
            2 -> radioButton2.toggle()
        }


    }

    fun confirmButtonTapped(v: View) {
        var powNewState: Int = -1
        if (radioButton0.isChecked) {
            powNewState = 0
        } else if (radioButton1.isChecked) {
            powNewState = 1
        } else if (radioButton2.isChecked) {
            powNewState = 2
        }

        val mIntent = Intent()
        mIntent.putExtra("powState", powNewState)

        // 设置结果，并进行传送
        this.setResult(4, mIntent)
        onBackPressed()
    }

    fun cancelButtonTapped(v: View) {
//        this.setResult(0, null)
        onBackPressed()
    }
}
