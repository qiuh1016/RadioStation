package com.cetcme.radiostation;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cetcme.radiostation.audio.AudioFileFunc;
import com.cetcme.radiostation.voiceSocket.RecordSocketFunc;
import com.cetcme.radiostation.audio.ErrorCode;
import com.cetcme.radiostation.audio.MediaRecordFunc;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class VoiceSocketActivity extends AppCompatActivity {

    private TextView mTextView;
    private EditText ipEditText;
    private EditText portEditText;

    private String serverIP = "192.168.0.194";
    private int serverPort = 9000;

    private Socket socket;


    private final static int FLAG_WAV = 0;
    private final static int FLAG_AMR = 1;
    private int mState = -1;    //-1:没再录制，0：录制wav，1：录制amr
    private Button btn_record_wav;
    private Button btn_stop;
    private Button btn_play;
    private VSAUIHandler uiHandler;
    private VSAUIThread uiThread;

    private MediaPlayer pressMedia;
    private MediaPlayer beginMedia;
    private MediaPlayer upMedia;

    private final static int CMD_RECORDING_TIME = 2000;
    private final static int CMD_RECORDFAIL = 2001;
    private final static int CMD_STOP = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_socket);


        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTextView.setText("LOG:");

        ipEditText = (EditText) findViewById(R.id.ip_editText);
        portEditText = (EditText) findViewById(R.id.port_editText);
        ipEditText.setText(serverIP);
        portEditText.setText(serverPort + "");

        btn_record_wav = (Button) findViewById(R.id.record_button);
        btn_stop = (Button) findViewById(R.id.stop_record_button);
        btn_record_wav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord(FLAG_WAV);
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });

        uiHandler = new VSAUIHandler();

    }



    /**
     * 建立服务端连接
     */
    public void conn(View v) {
        new Thread() {

            @Override
            public void run() {

                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ipEditText.getText().toString(), Integer.parseInt(portEditText.getText().toString())),2000);
                    Log.e("JAVA", "建立连接：" + socket);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLogView("建立连接：" + socket);
                        }
                    });

                    startReader();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLogView("尚未连接服务器！");
                            }
                        });
                        return;
                    }
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type","1");
                        jsonObject.put("content", "指令内容");

                        writer.writeUTF(jsonObject.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 从参数的Socket里获取最新的消息
     */
    private void startReader() {

        new Thread(){
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    final InetAddress address = socket.getInetAddress();

                    while (true) {

                        System.out.println("*等待服务器数据*");

                        // 读取数据
                        final String msg = reader.readUTF();
                        System.out.println("获取到服务器的信息：" + address + " :"+ msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLogView("获取到服务器的信息：" + address + " :"+ msg);
                            }
                        });


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    /**
     * 更新TextView
     */
    void refreshLogView(String msg){
        mTextView.append("\n" + msg);
        int offset = mTextView.getLineCount() * mTextView.getLineHeight();
        if (offset > mTextView.getHeight()) {
            mTextView.scrollTo(0, offset - mTextView.getHeight());
        }
    }

    /**
     * 开始录音
     * @param mFlag，0：录制wav格式，1：录音amr格式
     */
    private void startRecord(int mFlag){
        if(mState != -1){
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_RECORDFAIL);
            b.putInt("msg", ErrorCode.E_STATE_RECODING);
            msg.setData(b);

            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            return;
        }
        int mResult = -1;
        switch(mFlag){
            case FLAG_WAV:
                RecordSocketFunc mRecord_1 = RecordSocketFunc.getInstance(socket);
                mResult = mRecord_1.startRecordAndFile();
                break;
            case FLAG_AMR:
                MediaRecordFunc mRecord_2 = MediaRecordFunc.getInstance();
                mResult = mRecord_2.startRecordAndFile();
                break;
        }
        if(mResult == ErrorCode.SUCCESS){
            uiThread = new VoiceSocketActivity.VSAUIThread();
            new Thread(uiThread).start();
            mState = mFlag;
        }else{
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd",CMD_RECORDFAIL);
            b.putInt("msg", mResult);
            msg.setData(b);

            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }
    }

    /**
     * 停止录音
     */
    private void stopRecord(){
        if(mState != -1){
            switch(mState){
                case FLAG_WAV:
                    RecordSocketFunc mRecord_1 = RecordSocketFunc.getInstance(socket);
                    mRecord_1.stopRecordAndFile();
                    break;
                case FLAG_AMR:
                    MediaRecordFunc mRecord_2 = MediaRecordFunc.getInstance();
                    mRecord_2.stopRecordAndFile();
                    break;
            }
            if(uiThread != null){
                uiThread.stopThread();
            }
            if(uiHandler != null)
                uiHandler.removeCallbacks(uiThread);
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd",CMD_STOP);
            b.putInt("msg", mState);
            msg.setData(b);
            uiHandler.sendMessageDelayed(msg,1000); // 向Handler发送消息,更新UI
            mState = -1;
        }
    }

    class VSAUIHandler extends Handler{
        public VSAUIHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......");
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int vCmd = b.getInt("cmd");
            switch(vCmd)
            {
                case CMD_RECORDING_TIME:
                    int vTime = b.getInt("msg");
                    refreshLogView("正在录音中，已录制："+vTime+" s");
                    break;
                case CMD_RECORDFAIL:
                    int vErrorCode = b.getInt("msg");
                    String vMsg = ErrorCode.getErrorInfo(VoiceSocketActivity.this, vErrorCode);
                    refreshLogView("录音失败："+vMsg);
                    break;
                case CMD_STOP:
                    int vFileType = b.getInt("msg");
                    switch(vFileType){
                        case FLAG_WAV:
                            RecordSocketFunc mRecord_1 = RecordSocketFunc.getInstance(socket);
                            long mSize = mRecord_1.getRecordFileSize();
                            refreshLogView("录音已停止.录音文件:"+ AudioFileFunc.getWavFilePath()+"\n文件大小："+mSize);
                            break;
                        case FLAG_AMR:
                            MediaRecordFunc mRecord_2 = MediaRecordFunc.getInstance();
                            mSize = mRecord_2.getRecordFileSize();
                            refreshLogView("录音已停止.录音文件:"+AudioFileFunc.getAMRFilePath()+"\n文件大小："+mSize);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    class VSAUIThread implements Runnable {
        int mTimeMill = 0;
        boolean vRun = true;
        public void stopThread(){
            vRun = false;
        }
        public void run() {
            while(vRun){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mTimeMill ++;
                Log.d("thread", "mThread........"+mTimeMill);
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("cmd",CMD_RECORDING_TIME);
                b.putInt("msg", mTimeMill);
                msg.setData(b);

                VoiceSocketActivity.this.uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            }

        }
    }



}
