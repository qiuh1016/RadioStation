package com.cetcme.radiostation;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.cetcme.radiostation.Fragment.FunctionFragment;
import com.cetcme.radiostation.Fragment.SettingFragment;
import com.cetcme.radiostation.Fragment.HomepageFragment;
import com.cetcme.radiostation.Fragment.LogFragment;
import com.cetcme.radiostation.Fragment.MessageFragment;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.qiuhong.qhlibrary.Utils.DensityUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "MainActivity";

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

    private RelativeLayout rl_1;
    private RelativeLayout rl_2;
    private RelativeLayout rl_3;
    private RelativeLayout rl_4;
    private RelativeLayout rl_5;

    private HomepageFragment mHomepageFragment;
    private MessageFragment mMessageFragment;
    private LogFragment mLogFragment;
    private FunctionFragment mFunctionFragment;
    private SettingFragment mSettingFragment;

    //按2次返回退出
    private boolean hasPressedBackOnce = false;
    //back toast
    private Toast backToast;

    private KProgressHUD kProgressHUD;
    private KProgressHUD okHUD;

    private Socket socket;
    private OutputStreamWriter out;
    private InputStreamReader in;
    private ApplicationUtil appUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initHud();
        setupTabBar();
        setDefaultFragment();

        messageTips(-1, tv_1);
        messageTips(-1, tv_2);
        messageTips(-2, tv_3);
        messageTips(-1, tv_4);
        messageTips(-1, tv_5);

        initSocket();
    }

    private void initSocket() {
        new Thread() {
            @Override
            public void run() {
                appUtil =  (ApplicationUtil) MainActivity.this.getApplication();
                try {
                    appUtil.init();
                    socket = appUtil.getSocket();
                    appUtil.handlerHashMap.put(TAG, handler);
                    Log.e(TAG, "建立连接：" + socket);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.e(TAG, "err：" );
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Log.e(TAG, "err：" );
                }

            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ApplicationUtil.SOCKET_RECEIVE_DATA:
                    Log.e(TAG, "handleMessage: " + msg.obj);
                    break;
                default:
                    break;

            }
        }
    };



    public void connectServer() {
        kProgressHUD.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                kProgressHUD.dismiss();
                okHUD.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        okHUD.dismiss();
                    }
                }, 1000);
            }
        },1500);
    }

    private void setupTabBar() {
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

        rl_1 = (RelativeLayout) findViewById(R.id.rl_1);
        rl_2 = (RelativeLayout) findViewById(R.id.rl_2);
        rl_3 = (RelativeLayout) findViewById(R.id.rl_3);
        rl_4 = (RelativeLayout) findViewById(R.id.rl_4);
        rl_5 = (RelativeLayout) findViewById(R.id.rl_5);

        rb_1.setOnClickListener(this);
        rb_2.setOnClickListener(this);
        rb_3.setOnClickListener(this);
        rb_4.setOnClickListener(this);
        rb_5.setOnClickListener(this);

        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        rl_4.setOnClickListener(this);
        rl_5.setOnClickListener(this);
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

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.rl_1:
            case R.id.rb_1:
                Log.i(TAG, "onClick: 1");
                if (mHomepageFragment == null) {
                    mHomepageFragment = HomepageFragment.newInstance(getString(R.string.main_tab_name_1));
                }
                transaction.replace(R.id.tabs, mHomepageFragment);
                setTabButtonAction(0);
                break;
            case R.id.rl_2:
            case R.id.rb_2:
                Log.i(TAG, "onClick: 2");
                if (mMessageFragment == null) {
                    mMessageFragment = MessageFragment.newInstance(getString(R.string.main_tab_name_2));
                }
                transaction.replace(R.id.tabs, mMessageFragment);
                setTabButtonAction(1);
                break;
            case R.id.rl_3:
            case R.id.rb_3:
                Log.i(TAG, "onClick: 3");
                if (mLogFragment == null) {
                    mLogFragment = LogFragment.newInstance(getString(R.string.main_tab_name_3));
                }
                transaction.replace(R.id.tabs, mLogFragment);
                setTabButtonAction(2);
                break;
            case R.id.rl_4:
            case R.id.rb_4:
                Log.i(TAG, "onClick: 4");
                if (mFunctionFragment == null) {
                    mFunctionFragment = FunctionFragment.newInstance(getString(R.string.main_tab_name_4));
                }
                transaction.replace(R.id.tabs, mFunctionFragment);
                setTabButtonAction(3);
                break;
            case R.id.rl_5:
            case R.id.rb_5:
                Log.i(TAG, "onClick: 5");
                if (mSettingFragment == null) {
                    mSettingFragment = SettingFragment.newInstance(getString(R.string.main_tab_name_5));
                }
                transaction.replace(R.id.tabs, mSettingFragment);
                setTabButtonAction(4);
                break;
        }
        transaction.commit();
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mHomepageFragment = HomepageFragment.newInstance(getString(R.string.main_tab_name_1));
        transaction.replace(R.id.tabs, mHomepageFragment);
        transaction.commit();
        setTabButtonAction(0);
    }

    private void initHud() {
        //hudView
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.connect_server_progress))
                .setAnimationSpeed(1)
                .setDimAmount(0.3f)
                .setSize(110, 110)
                .setCancellable(false);
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.drawable.checkmark);
        okHUD  =  KProgressHUD.create(this)
                .setCustomView(imageView)
                .setLabel(getString(R.string.connect_server_ok))
                .setCancellable(false)
                .setSize(110,110)
                .setDimAmount(0.3f);
    }

    public void onBackPressed() {

        if (!hasPressedBackOnce) {
            backToast = Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT);
            backToast.show();
            hasPressedBackOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hasPressedBackOnce = false;
                }
            },2500);
        } else {
            backToast.cancel();
            appUtil.handlerHashMap.remove(TAG);
            super.onBackPressed();
        }
    }

    private void setTabButtonAction(int index) {
        RadioButton[] radioButtons = new RadioButton[] {rb_1, rb_2, rb_3, rb_4, rb_5};

        for (int i = 0; i < 5; i++) {
            if (i == index) {
                radioButtons[i].setTextColor(getResources().getColor(R.color.tab_text_selected));
                radioButtons[i].getPaint().setFakeBoldText(true);
                radioButtons[i].setChecked(true);
            } else {
                radioButtons[i].setTextColor(getResources().getColor(R.color.homepage_text_color));
                radioButtons[i].getPaint().setFakeBoldText(false);
                radioButtons[i].setChecked(false);
            }
        }

    }
}
