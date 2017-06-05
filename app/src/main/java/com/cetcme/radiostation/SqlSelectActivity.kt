package com.cetcme.radiostation

import android.app.Activity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sql_select.*
import android.content.Intent
import com.cetcme.radiostation.Fragment.HomepageFragment


class SqlSelectActivity : Activity() {

    var radioNumber: Int = 0
    var MNumber: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sql_select)

        initView()
        setRadioData()
    }

    fun initView() {
        titleView.setBackgroundResource(R.drawable.top_select)
        titleView.setTitle("SQL设置")


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


        if (radioNumber == 0) {
            radioButton0.isChecked
            editText.visibility = View.INVISIBLE
        } else if (radioNumber == 1) {
            radioButton1.isChecked
            editText.visibility = View.INVISIBLE
        } else if (radioNumber == 2) {
            radioButton2.isChecked
            editText.visibility = View.VISIBLE
            editText.setText(MNumber)
        }
    }

    fun confirmButtonTapped(v: View) {
        var radioNumber: Int = -1
        var MNumberStr = ""
        if (radioButton0.isChecked) {
            radioNumber = 0
        } else if (radioButton1.isChecked) {
            radioNumber = 1
        } else if (radioButton2.isChecked) {
            if (editText.text.toString().isEmpty()) {
                return
            }

            MNumberStr = editText.text.toString()
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
        this.setResult(0, mIntent)
        onBackPressed()
    }

    fun cancelButtonTapped(v: View) {
        this.setResult(0, null)
        onBackPressed()
    }


}
