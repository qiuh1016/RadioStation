package com.cetcme.radiostation.DialogView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cetcme.radiostation.ApplicationUtil;
import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

public class SocketConnectDialogActivity extends Activity {

    private String TAG = "SocketConnectDialog";
    private TextView hint_textView;
    private Button connect_button;

    private ApplicationUtil appUtil;

    private String BUTTON_CONNECTED = "连接";
    private String BUTTON_DISCONNECTED = "断开";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_connect_dialog);

        appUtil = (ApplicationUtil) getApplication();
        appUtil.handlerHashMap.put(TAG, handler);

        initView();

        boolean socket_disconnected = getIntent().getBooleanExtra("socket_disconnected", false);
        if (socket_disconnected) {
            hint_textView.setText("与服务器断开连接");
        }
    }

    private void initView() {
        QHTitleView titleView = (QHTitleView) findViewById(R.id.titleView);
        titleView.setTitle("服务器连接");
        titleView.setRightView(R.drawable.exit_icon);
        titleView.setBackgroundResource(R.drawable.top_select);
        titleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                //
            }

            @Override
            public void onRightClick() {
                onBackPressed();
            }
        });

        hint_textView = (TextView) findViewById(R.id.hint_textView);
        connect_button = (Button) findViewById(R.id.connect_button);

        if (appUtil.getSocket() != null && appUtil.getSocket().isConnected()) {
            hint_textView.setText("已连接");
            setConnectButtonEnable(true, BUTTON_DISCONNECTED);
        }
    }

    public void buttonClick(View v) {
        if (connect_button.getText().equals(BUTTON_CONNECTED)) {
            hint_textView.setText("正在连接...");
            setConnectButtonEnable(false, null);
            connectSocket();
        } else if (connect_button.getText().equals(BUTTON_DISCONNECTED)) {
            appUtil.disconnectSocket();
            hint_textView.setText("连接已断开");
            setConnectButtonEnable(true, BUTTON_CONNECTED);
        } else {
            Log.d(TAG, "buttonClick: " + connect_button.getText());
        }

    }

    private void connectSocket() {
        appUtil.connectSocket();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ApplicationUtil.SOCKET_CONNECTED:
                    hint_textView.setText("连接成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    }, 1000);
                    break;
                case ApplicationUtil.SOCKET_DISCONNECTED:
                    hint_textView.setText(msg.obj.toString());
                    setConnectButtonEnable(true, BUTTON_CONNECTED);
                    break;
                default:
                    break;
            }
        }
    };

    public void onBackPressed() {
        super.onBackPressed();
        appUtil.handlerHashMap.remove(TAG);
    }

    private void setConnectButtonEnable(boolean enable, String text) {
        if (text != null) {
            connect_button.setText(text);
        }
        connect_button.setEnabled(enable);
        connect_button.setBackgroundResource(enable ? R.drawable.single_select : R.drawable.single_select_disable);
    }

}
