package com.cetcme.radiostation;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cetcme.radiostation.Fragment.CameraFragment;
import com.cetcme.radiostation.Fragment.HelpFragment;
import com.cetcme.radiostation.Fragment.LocationFragment;
import com.cetcme.radiostation.Fragment.SearchFragment;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

public class MainActivity extends AppCompatActivity {

    private CameraFragment mCameraFragment;
    private LocationFragment mLocationFragment;
    private SearchFragment mSearchFragment;
    private HelpFragment mHelpFragment;

    private QHTitleView qhTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initTitleView();
        initBottomView();
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

    private void initBottomView() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar.addItem(new BottomNavigationItem(android.R.drawable.ic_menu_camera, "相机"))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_compass, "位置"))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_search, "搜索"))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_help, "帮助"))
                .initialise();//所有的设置需在调用该方法前完成

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {//这里也可以使用SimpleOnTabSelectedListener
            @Override
            public void onTabSelected(int position) {//未选中 -> 选中
                Log.d("Main", "onTabSelected() called with: " + "position = [" + position + "]");
                FragmentManager fm = getFragmentManager();
                //开启事务
                FragmentTransaction transaction = fm.beginTransaction();
                switch (position) {
                    case 0:
                        if (mCameraFragment == null) {
                            mCameraFragment = CameraFragment.newInstance("相机");
                        }
                        qhTitleView.setTitle("相机");
                        transaction.replace(R.id.tabs, mCameraFragment);
                        break;
                    case 1:
                        if (mLocationFragment == null) {
                            mLocationFragment = LocationFragment.newInstance("位置");
                        }
                        qhTitleView.setTitle("位置");
                        transaction.replace(R.id.tabs, mLocationFragment);
                        break;
                    case 2:
                        if (mSearchFragment == null) {
                            mSearchFragment = SearchFragment.newInstance("搜索");
                        }
                        qhTitleView.setTitle("搜索");
                        transaction.replace(R.id.tabs, mSearchFragment);
                        break;
                    case 3:
                        if (mHelpFragment == null) {
                            mHelpFragment = HelpFragment.newInstance("帮助");
                        }
                        qhTitleView.setTitle("帮助");
                        transaction.replace(R.id.tabs, mHelpFragment);
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

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mCameraFragment = CameraFragment.newInstance("相机");
        qhTitleView.setTitle("相机");
        transaction.replace(R.id.tabs, mCameraFragment);
        transaction.commit();
    }
}
