package com.cetcme.radiostation.Setting;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.Dialog.QHDialog;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

public class AddressEditActivity extends AppCompatActivity {

    private Button addButton;
    private String usage;

    private EditText nameEditText;
    private EditText numberEditText;
    private Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
        getSupportActionBar().hide();

        usage = getIntent().getStringExtra("for");
        initTitleView();
        initView();
    }

    private void initTitleView() {
        QHTitleView qhTitleView = (QHTitleView) findViewById(R.id.qhTitleView);
        if (usage.equals("add")) {
            qhTitleView.setTitle("地址簿添加");
        } else if (usage.equals("edit")) {
            qhTitleView.setTitle("地址簿修改");
        }
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
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        numberEditText = (EditText) findViewById(R.id.numberEditText);
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        if (usage.equals("edit")) {
            nameEditText.setText(getIntent().getStringExtra("name"));
            numberEditText.setText(getIntent().getStringExtra("number"));
            typeSpinner.setSelection(getIntent().getIntExtra("type", 0));
        }
    }

    public void save(View v) {

        if (nameEditText.getText().toString().equals("") || numberEditText.getText().toString().equals("")) {
            return;
        }

        QHDialog qhDialog = new QHDialog(this, "提示", "已保存");
        qhDialog.setPositiveButton("好的", 0, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
                onBackPressed();
            }
        });
        qhDialog.setCancelable(false);
        qhDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in_no_alpha,
                R.anim.push_right_out_no_alpha);
    }
}
