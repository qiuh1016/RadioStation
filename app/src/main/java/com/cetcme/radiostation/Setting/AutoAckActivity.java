package com.cetcme.radiostation.Setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

public class AutoAckActivity extends AppCompatActivity {


    private Spinner spinner_1;
    private Spinner spinner_2;
    private Spinner spinner_3;
    private Spinner spinner_4;
    private Spinner spinner_5;
    private Spinner spinner_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_auto_ack);

        getSupportActionBar().hide();
        initTitleView();

        initView();
    }

    private void initTitleView() {
        QHTitleView qhTitleView = (QHTitleView) findViewById(R.id.qhTitleView);
        qhTitleView.setTitle("AUTO ACK设置");
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

        spinner_1 = (Spinner) findViewById(R.id.spinner1);
        spinner_2 = (Spinner) findViewById(R.id.spinner2);
        spinner_3 = (Spinner) findViewById(R.id.spinner3);
        spinner_4 = (Spinner) findViewById(R.id.spinner4);
        spinner_5 = (Spinner) findViewById(R.id.spinner5);
        spinner_6 = (Spinner) findViewById(R.id.spinner6);

        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in_no_alpha,
                R.anim.push_right_out_no_alpha);
    }
}
