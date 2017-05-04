package com.cetcme.radiostation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cetcme.radiostation.Fragment.CameraFragment;
import com.cetcme.radiostation.Fragment.FunctionFragment;
import com.cetcme.radiostation.Fragment.SettingFragment;
import com.cetcme.radiostation.Fragment.LocationFragment;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

public class MainActivity extends AppCompatActivity {

    private CameraFragment mCameraFragment;
    private LocationFragment mLocationFragment;
    private FunctionFragment mFunctionFragment;
    private SettingFragment mSettingFragment;

    private QHTitleView qhTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
//        initTitleView();
//        initBottomView();
    }

    /*
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


    private void initBottomView() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar.addItem(new BottomNavigationItem(android.R.drawable.ic_menu_camera, "首页"))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_compass, "消息"))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_search, "日志"))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_help, "功能"))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_help, "设置"))
                .initialise();//所有的设置需在调用该方法前完成

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {//这里也可以使用SimpleOnTabSelectedListener
            @Override
            public void onTabSelected(int position) {//未选中 -> 选中
                Log.d("Main", "onTabSelected() called with: " + "position = [" + position + "]");
                FragmentManager fm = getSupportFragmentManager();
                //开启事务
                FragmentTransaction transaction = fm.beginTransaction();
                switch (position) {
                    case 0:
                        if (mCameraFragment == null) {
                            mCameraFragment = CameraFragment.newInstance("首页");
                        }
                        qhTitleView.setTitle("首页");
                        transaction.replace(R.id.tabs, mCameraFragment);
                        break;
                    case 1:
                        if (mLocationFragment == null) {
                            mLocationFragment = LocationFragment.newInstance("消息");
                        }
                        qhTitleView.setTitle("消息");
                        transaction.replace(R.id.tabs, mLocationFragment);
                        break;
                    case 2:
                        if (mFunctionFragment == null) {
                            mFunctionFragment = FunctionFragment.newInstance("日志");
                        }
                        qhTitleView.setTitle("日志");
                        transaction.replace(R.id.tabs, mFunctionFragment);
                        break;
                    case 3:
                        if (mSettingFragment == null) {
                            mSettingFragment = SettingFragment.newInstance("功能");
                        }
                        qhTitleView.setTitle("功能");
                        transaction.replace(R.id.tabs, mSettingFragment);
                        break;
                    case 4:
                        if (mSettingFragment == null) {
                            mSettingFragment = SettingFragment.newInstance("设置");
                        }
                        qhTitleView.setTitle("设置");
                        transaction.replace(R.id.tabs, mSettingFragment);
                        break;
                    default:
                        break;
                }
                // 事务提交
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {//选中 -> 未选中
                Log.i("activity_audio_player", "onTabUnselected: " + position);
            }

            @Override
            public void onTabReselected(int position) {//选中 -> 选中
                Log.i("activity_audio_player", "onTabReselected: " + position);
            }
        });

        setDefaultFragment();
    }


    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mCameraFragment = CameraFragment.newInstance("首页");
        qhTitleView.setTitle("首页");
        transaction.replace(R.id.tabs, mCameraFragment);
        transaction.commit();
    }

    */
}
