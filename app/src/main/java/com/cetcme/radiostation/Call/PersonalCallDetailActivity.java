package com.cetcme.radiostation.Call;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.Dialog.QHDialog;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

public class PersonalCallDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_call_detail);
        getSupportActionBar().hide();

        initTitleView();
    }

    private void initTitleView() {
        QHTitleView qhTitleView = (QHTitleView) findViewById(R.id.qhTitleView);
        qhTitleView.setTitle(getResources().getStringArray(R.array.messageList)[0]);
        qhTitleView.setBackView(R.drawable.icon_back_button);
        qhTitleView.setRightView(R.drawable.icon_print);
        qhTitleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                onBackPressed();
            }

            @Override
            public void onRightClick() {
                Toast.makeText(getApplicationContext(), "打印", Toast.LENGTH_SHORT).show();
                //
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

