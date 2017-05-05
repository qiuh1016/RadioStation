package com.cetcme.radiostation.Setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

public class SelfCheckActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_self_check);

        getSupportActionBar().hide();
        initTitleView();

        initView();
    }

    private void initTitleView() {
        QHTitleView qhTitleView = (QHTitleView) findViewById(R.id.qhTitleView);
        qhTitleView.setTitle("自检");
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






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in_no_alpha,
                R.anim.push_right_out_no_alpha);
    }
}
