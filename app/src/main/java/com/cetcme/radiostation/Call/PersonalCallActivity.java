package com.cetcme.radiostation.Call;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cetcme.radiostation.DialogView.CTypeActivity;
import com.cetcme.radiostation.DialogView.SqlSelectActivity;
import com.cetcme.radiostation.MyClass.NumberValidationUtils;
import com.cetcme.radiostation.R;
import com.cetcme.radiostation.Setting.AddressActivity;
import com.qiuhong.qhlibrary.Dialog.QHDialog;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

public class PersonalCallActivity extends AppCompatActivity {

    String TAG = "PersonalCallActivity";

    private Button sendButton;
    private boolean stopCountDown = false;
    private int countDownNumber = 3;

    private Spinner prioritySpinner;
    private Spinner typeSpinner;

    private Spinner dscSpinner;

    private TextView communicateTextView;
    private EditText addressEditText;

    private ArrayAdapter<String> dscAdapter;
    private String[] dscItems = new String[]{};

    private int type = 0;
    private int ch = 0;
    private String tx = "";
    private String rx = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_call);
        getSupportActionBar().hide();


        initTitleView();
        initView();
    }

    private void initTitleView() {
        QHTitleView qhTitleView = (QHTitleView) findViewById(R.id.qhTitleView);
        qhTitleView.setTitle(getResources().getStringArray(R.array.messageList)[0]);
        qhTitleView.setBackView(R.drawable.icon_back_button);
        qhTitleView.setRightView(0);
        qhTitleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                onBackPressed();
            }

            @Override
            public void onRightClick() {
                //
            }
        });
    }

    private void initView() {
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        sendButton = (Button) findViewById(R.id.send_button);

        prioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);

        dscSpinner = (Spinner) findViewById(R.id.dsc_spinner);

        // 建立Adapter并且绑定数据源
        dscAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getDscItems());
        //绑定 Adapter到控件
        dscSpinner.setAdapter(dscAdapter);

        communicateTextView = (TextView) findViewById(R.id.communicateTextView);
        communicateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CTypeActivity.class);
                intent.putExtra("radioNumber", type);
                intent.putExtra("ch", ch);
                intent.putExtra("tx", tx);
                intent.putExtra("rx", rx);
                startActivityForResult(intent, 0);
            }
        });

        findViewById(R.id.addressTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                intent.putExtra("forAddress", true);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_left_in_no_alpha, R.anim.push_left_out_no_alpha);
            }
        });


    }

    private String[] getDscItems() {
        //TODO

        dscItems = new String[]{
                "DISTRESS1:2187.5",
                "DISTRESS2:4207.5",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0",
                "DISTRESS3:6312.0"
        };

        return dscItems;
    }

    public void send(View v) {

        String address = addressEditText.getText().toString();
        String priority = prioritySpinner.getSelectedItem().toString();
        String type = typeSpinner.getSelectedItem().toString();
        String dscFreq = dscSpinner.getSelectedItem().toString();

        Log.i(TAG, "send: " + address);
        Log.i(TAG, "send: " + priority);
        Log.i(TAG, "send: " + type);
        Log.i(TAG, "send: " + tx);
        Log.i(TAG, "send: " + rx);
        Log.i(TAG, "send: " + dscFreq);


        sendButton.setText(countDownNumber + "");
        sendButton.setEnabled(false);
        new Thread(new CountDown()).start();
    }

    // 线程类
    class CountDown implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < countDownNumber; i++) {

                if (stopCountDown) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendButton.setText(String.valueOf(countDownNumber - 1 - finalI));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sendButton.setEnabled(true);
                    sendButton.setText("发送");

                    // 测试用
                    newMessageDialog();
                }
            });

            stopCountDown = false;
        }
    }

    private void newMessageDialog() {
        QHDialog qhDialog = new QHDialog(this,"新消息", "收到来自123456789的个人呼叫消息！");
        qhDialog.setPositiveButton("查看", 0, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
                //TODO: 打开查看界面
                Intent intent = new Intent(getApplicationContext(), PersonalCallDetailActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in_no_alpha, R.anim.push_left_out_no_alpha);
            }
        });
        qhDialog.setNegativeButton("取消", 0, null);
        qhDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in_no_alpha,
                R.anim.push_right_out_no_alpha);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }


        // 根据上面发送过去的请求码来区别
        switch (requestCode) {
            //通信频率
            case 0:
                type = data.getIntExtra("radioNumber", 0);
                ch = data.getIntExtra("ch", 0);
                tx = data.getStringExtra("tx");
                rx = data.getStringExtra("rx");

                if (type == 0) {
                    //TODO: 查询数据库 用发送和接收频率代替显示
                    communicateTextView.setText("通信频道：" + ch);
                } else if (type == 1) {
                    communicateTextView.setText("TX: " + tx + ", RX: " + rx);
                }
                break;
            //发送地址
            case 1:
                String address = data.getStringExtra("address");
                addressEditText.setText(address);
            default:
                break;
        }
    }
}

