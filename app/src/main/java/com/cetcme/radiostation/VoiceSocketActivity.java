package com.cetcme.radiostation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class VoiceSocketActivity extends AppCompatActivity {

    private String TAG = "VoiceSocketActivity";

    protected Socket socket;
    protected DataOutputStream dout;


    private BufferedReader in = null;
    private PrintWriter out = null;

    private String content = "";

    private TextView tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_socket);
        getSupportActionBar().hide();
        initTitleView();


        tv_msg = (TextView) findViewById(R.id.contentTV);

        new Thread(voiceSocketClient).start();

    }

    private void initTitleView() {
        QHTitleView qhTitleView = (QHTitleView) findViewById(R.id.qhTitleView);
        qhTitleView.setTitle("语音");
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

    Runnable voiceSocketClient = new Runnable() {

        @Override
        public void run() {
            try {
                socket = new Socket("192.168.0.56", 9000);
//                dout = new DataOutputStream(socket.getOutputStream());

                in = new BufferedReader(new InputStreamReader(socket
                        .getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream())), true);

                new Thread(receiveSocket).start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    Runnable receiveSocket = new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    if (!socket.isClosed()) {
                        if (socket.isConnected()) {
                            if (!socket.isInputShutdown()) {

                                if ((content = in.readLine()) != null) {
                                    content += "\n";
                                    Log.i(TAG, "run: " + content);
                                    mHandler.sendMessage(mHandler.obtainMessage());
                                } else {

                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 发送消息
     */
    public void send(View v) {
        new Thread() {
            @Override
            public void run() {

                try {
                    // socket.getInputStream()
                    if (socket == null) {
                        return;
                    }
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                    writer.writeUTF("我是客户端.."); // 写一个UTF-8的信息
//                    writer.write(getPCMData());

                    System.out.println("发送消息");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 如果连接出现异常，弹出AlertDialog！
     */
    public void ShowDialog(String msg) {
        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    //接收线程发送过来信息，并用TextView显示
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_msg.setText(content);
        }
    };




}
