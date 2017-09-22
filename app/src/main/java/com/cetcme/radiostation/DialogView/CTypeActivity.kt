package com.cetcme.radiostation.DialogView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cetcme.radiostation.Fragment.HomepageFragment
import com.cetcme.radiostation.MyClass.NumberValidationUtils

import com.cetcme.radiostation.R
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView
import kotlinx.android.synthetic.main.activity_ctype.*
import java.text.DecimalFormat

class CTypeActivity : Activity() {

    var radioNumber: Int = 0
    var ch: Int = 0
    var tx: String = ""
    var rx: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ctype)

        initView()
        setRadioData()

    }

    fun initView() {
        titleView.setBackgroundResource(R.drawable.top_select)
        titleView.setTitle("通信方式")

        titleView.setClickCallback(object : QHTitleView.ClickCallback {
            override fun onBackClick() {
                //
            }
            override fun onRightClick() {
                //
            }
        })

        freq_radioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tx_editText.visibility = View.VISIBLE
                rx_editText.visibility = View.VISIBLE
            } else {
                tx_editText.visibility = View.INVISIBLE
                rx_editText.visibility = View.INVISIBLE
            }
        }

        ch_radioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ch_editText.visibility = View.VISIBLE
            } else {
                ch_editText.visibility = View.INVISIBLE
            }
        }

    }

    fun setRadioData() {

        radioNumber = this.intent.getIntExtra("radioNumber",0)
        ch = this.intent.getIntExtra("ch", 0)
        tx = this.intent.getStringExtra("tx")
        rx = this.intent.getStringExtra("rx")


        if (ch != 0) {
            ch_editText.setText("$ch")
        }

        if (tx != "") {
            tx_editText.setText("$tx")
        }

        if (rx != "") {
            rx_editText.setText("$rx")
        }

        when (radioNumber) {
            0 -> {
                ch_radioButton.toggle()
                tx_editText.visibility = View.INVISIBLE
                rx_editText.visibility = View.INVISIBLE
            }
            1 -> {
                freq_radioButton.toggle()
                ch_editText.visibility = View.INVISIBLE
            }
        }


    }

    fun confirmButtonTapped(v: View) {
        var radioNumber: Int = -1
        if (ch_radioButton.isChecked) {
            radioNumber = 0
            var chStr = ch_editText.text.toString()

            if (chStr.isEmpty()) {
                return
            }

            if (HomepageFragment.isNum(chStr)) {
                ch = chStr.toInt()
                if (ch <= 0 || ch > 9999) {
                    ch_editText.text.clear()
                    Toast.makeText(this, "信道编号应在1到9999之间", Toast.LENGTH_SHORT).show()
                    return
                }
            }

        } else if (freq_radioButton.isChecked) {
            radioNumber = 1
            var txStr = tx_editText.text.toString()
            var rxStr = rx_editText.text.toString()

            if (txStr.isEmpty() || rxStr.isEmpty()) {
                return
            }

            if (NumberValidationUtils.isRealNumber(txStr) && NumberValidationUtils.isRealNumber(rxStr)) {
                var txD = txStr.toDouble()
                var rxD = rxStr.toDouble()


                if (txD <= 0 || txD > 99999.9) {
                    tx_editText.text.clear()
                    Toast.makeText(this, "发送频率应在0到99999.9之间", Toast.LENGTH_SHORT).show()
                    return
                }
                if (rxD <= 0 || rxD > 99999.9) {
                    rx_editText.text.clear()
                    Toast.makeText(this, "接收频率应在0到99999.9之间", Toast.LENGTH_SHORT).show()
                    return
                }

                val decimalFormat = DecimalFormat(".00")//构造方法的字符格式这里如果小数不足2位,会以0补足.
                tx = decimalFormat.format(java.lang.Double.valueOf(txStr))//format 返回的是字符串
                rx = decimalFormat.format(java.lang.Double.valueOf(rxStr))//format 返回的是字符串
            }
        }

        val mIntent = Intent()
        mIntent.putExtra("radioNumber", radioNumber)
        mIntent.putExtra("ch", ch)
        mIntent.putExtra("tx", tx)
        mIntent.putExtra("rx", rx)
        // 设置结果，并进行传送
        this.setResult(0, mIntent)
        onBackPressed()
    }

    fun cancelButtonTapped(v: View) {
//        this.setResult(0, null)
        onBackPressed()
    }

//    fun click(v: View) {
//        when (v.id) {
//            R.id.confirmButton -> confirmButtonTapped()
//            R.id.cancelButton -> cancelButtonTapped()
//        }
//    }
}
