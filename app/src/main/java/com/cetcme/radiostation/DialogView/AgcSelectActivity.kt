package com.cetcme.radiostation.DialogView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.cetcme.radiostation.Fragment.HomepageFragment

import com.cetcme.radiostation.R
import kotlinx.android.synthetic.main.activity_sql_select.*

class AgcSelectActivity : Activity() {

    var radioNumber: Int = 0
    var MNumber: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agc_select)
        initView()
        setRadioData()
    }

    fun initView() {
        titleView.setBackgroundResource(R.drawable.top_select)
        titleView.setTitle("AGC设置")


        radioButton2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editText.visibility = View.VISIBLE
            } else {
                editText.visibility = View.INVISIBLE
            }
        }


    }

    fun setRadioData() {

        radioNumber = this.intent.getIntExtra("radioNumber",0)
        MNumber = this.intent.getStringExtra("MNumber")
        Log.i("kt", " MNumber: $MNumber")

        editText.setText(MNumber)
        when (radioNumber) {
            0 -> {
                radioButton0.toggle()
                editText.visibility = View.INVISIBLE
            }
            1 -> {
                radioButton1.toggle()
                editText.visibility = View.INVISIBLE
            }
            2 -> {
                radioButton2.toggle()
                editText.visibility = View.VISIBLE
            }
        }


    }

    fun confirmButtonTapped(v: View) {
        var radioNumber: Int = -1
        var MNumberStr = ""
        if (radioButton0.isChecked) {
            radioNumber = 0
            MNumberStr = MNumber
        } else if (radioButton1.isChecked) {
            radioNumber = 1
            MNumberStr = MNumber
        } else if (radioButton2.isChecked) {


            MNumberStr = editText.text.toString()

            if (MNumberStr.isEmpty()) {
                return
            }

            if (HomepageFragment.isNum(MNumberStr)) {
                var MNumberInt:Int = editText.text.toString().toInt()
                if (MNumberInt > 20 || MNumberInt <= 0) {
                    editText.text.clear()
                    return
                } else {
                    radioNumber = 2

                    if (MNumberStr.length == 1) {
                        MNumberStr = "0" + MNumberStr
                    }
                }

            } else {
                editText.text.clear()
                return
            }


        }

        val mIntent = Intent()
        mIntent.putExtra("radioNumber", radioNumber)
        mIntent.putExtra("MNumber", MNumberStr)
        // 设置结果，并进行传送
        this.setResult(2, mIntent)
        onBackPressed()
    }

    fun cancelButtonTapped(v: View) {
//        this.setResult(0, null)
        onBackPressed()
    }
}
