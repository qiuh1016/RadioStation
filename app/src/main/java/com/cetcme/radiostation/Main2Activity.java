package com.cetcme.radiostation;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cetcme.radiostation.Fragment.CameraFragment;
import com.cetcme.radiostation.Fragment.HelpFragment;
import com.cetcme.radiostation.Fragment.LocationFragment;
import com.cetcme.radiostation.Fragment.SearchFragment;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;
import com.qiuhong.qhlibrary.Utils.DensityUtil;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "Main2Activity";

    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;

    private RadioButton rb_1;
    private RadioButton rb_2;
    private RadioButton rb_3;
    private RadioButton rb_4;
    private RadioButton rb_5;

    private QHTitleView qhTitleView;

    private CameraFragment mCameraFragment;
    private LocationFragment mLocationFragment;
    private SearchFragment mSearchFragment;
    private HelpFragment mHelpFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();

        initTitleView();
        setupView();
        setDefaultFragment();

        messageTips(-1, tv_1);
        messageTips(-2, tv_2);
        messageTips(1, tv_3);
        messageTips(3, tv_4);
        messageTips(100, tv_5);
    }

    private void initTitleView() {
        qhTitleView = (QHTitleView) findViewById(R.id.main_QHTitleView);
        qhTitleView.setTitle("activity_audio_player");
        qhTitleView.setBackView(0);
        qhTitleView.setRightView(0);
        qhTitleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                //
            }

            @Override
            public void onRightClick() {
                //
            }
        });
    }

    private void setupView() {
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);

        rb_1 = (RadioButton) findViewById(R.id.rb_1);
        rb_2 = (RadioButton) findViewById(R.id.rb_2);
        rb_3 = (RadioButton) findViewById(R.id.rb_3);
        rb_4 = (RadioButton) findViewById(R.id.rb_4);
        rb_5 = (RadioButton) findViewById(R.id.rb_5);

        rb_1.setOnClickListener(this);
        rb_2.setOnClickListener(this);
        rb_3.setOnClickListener(this);
        rb_4.setOnClickListener(this);
        rb_5.setOnClickListener(this);
    }

    /**
     * -1:表示没有新消息
     * -2:表示新消息用红点的方式显示
     * 0-99：直接显示数字
     * >=100:用99+显示
     */
    private void messageTips (int num, TextView tv) {
        if(num == -1) {
            tv.setVisibility(View.GONE);
        } else if(num == -2){
            tv.setVisibility(View.VISIBLE);
            tv.setText("");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            layoutParams.height= DensityUtil.dip2px(this,10);
            layoutParams.width= DensityUtil.dip2px(this,10);
            tv.setLayoutParams(layoutParams);
        } else if(num >= 0 && num <= 99) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(num+"");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            layoutParams.height= DensityUtil.dip2px(this,16);
            layoutParams.width= DensityUtil.dip2px(this,16);
            tv.setLayoutParams(layoutParams);
        } else if(num>=100) {
            tv.setVisibility(View.VISIBLE);
            tv.setText("99+");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            layoutParams.height= DensityUtil.dip2px(this,16);
            layoutParams.width= DensityUtil.dip2px(this,16);
            tv.setTextSize(DensityUtil.dip2px(this,3));
            tv.setLayoutParams(layoutParams);
        } else {
            tv.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.rb_1:
                Log.i(TAG, "onClick: 1");
                if (mCameraFragment == null) {
                    mCameraFragment = CameraFragment.newInstance(getString(R.string.main_tab_name_1));
                }
                qhTitleView.setTitle(getString(R.string.main_tab_name_1));
                transaction.replace(R.id.tabs, mCameraFragment);
                break;
            case R.id.rb_2:
                Log.i(TAG, "onClick: 2");
                if (mLocationFragment == null) {
                    mLocationFragment = LocationFragment.newInstance(getString(R.string.main_tab_name_2));
                }
                qhTitleView.setTitle(getString(R.string.main_tab_name_2));
                transaction.replace(R.id.tabs, mLocationFragment);
                break;
            case R.id.rb_3:
                Log.i(TAG, "onClick: 3");
                if (mSearchFragment == null) {
                    mSearchFragment = SearchFragment.newInstance(getString(R.string.main_tab_name_3));
                }
                qhTitleView.setTitle(getString(R.string.main_tab_name_3));
                transaction.replace(R.id.tabs, mSearchFragment);
                break;
            case R.id.rb_4:
                Log.i(TAG, "onClick: 4");
                if (mHelpFragment == null) {
                    mHelpFragment = HelpFragment.newInstance(getString(R.string.main_tab_name_4));
                }
                qhTitleView.setTitle(getString(R.string.main_tab_name_4));
                transaction.replace(R.id.tabs, mHelpFragment);
                break;
            case R.id.rb_5:
                Log.i(TAG, "onClick: 5");
                if (mHelpFragment == null) {
                    mHelpFragment = HelpFragment.newInstance(getString(R.string.main_tab_name_5));
                }
                qhTitleView.setTitle(getString(R.string.main_tab_name_5));
                transaction.replace(R.id.tabs, mHelpFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mCameraFragment = CameraFragment.newInstance(getString(R.string.main_tab_name_1));
        qhTitleView.setTitle(getString(R.string.main_tab_name_1));
        transaction.replace(R.id.tabs, mCameraFragment);
        transaction.commit();
    }
}
