package com.cetcme.radiostation.voiceSocket;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
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

import com.cetcme.radiostation.R;
import com.cetcme.radiostation.audio.AudioFileFunc;
import com.cetcme.radiostation.audio.ErrorCode;
import com.cetcme.radiostation.audio.MediaRecordFunc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class VoiceSocketActivity extends AppCompatActivity {

    private TextView mTextView;
    private EditText ipEditText;
    private EditText portEditText;

    private String serverIP = "192.168.9.179";
    private int serverPort = 7779;

    private Socket socket;


    //录制相关参数
    private final static int FLAG_WAV = 0;
    private final static int FLAG_AMR = 1;
    private int mState = -1;    //-1:没再录制，0：录制wav，1：录制amr
    private Button btn_record_wav;
    private Button btn_stop;
    private Button btn_play;
    private VSAUIHandler uiHandler;
    private VSAUIThread uiThread;

    private final static int CMD_RECORDING_TIME = 2000;
    private final static int CMD_RECORDFAIL = 2001;
    private final static int CMD_STOP = 2002;

    private MediaPlayer pressMedia;
    private MediaPlayer beginMedia;
    private MediaPlayer upMedia;

    //播放相关参数
    private AudioTrack player;
    private int audioBufSize;

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

        //录制相关设置
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

        //播放相关设置
        audioBufSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        player = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioBufSize,
                AudioTrack.MODE_STREAM);
        player.play();

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
                    if (socket == null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLogView("尚未连接服务器！");
                            }
                        });
                        return;
                    }
                    OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("InstructionType", 25);
                        jsonObject.put("Mode", 1);
                        JSONObject valueJson = new JSONObject();
                        for (int i = 1; i <= 30; i++) {
                            valueJson.put("Band_" + i, 1.1);
                        }
                        jsonObject.put("Value", valueJson);


                        writer.write(jsonObject.toString());
                        writer.flush();
                        Log.e("JAVA", "send: " + jsonObject.toString());

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
                try {
                    // 获取读取流
                    while (true) {

                        if (socket.isClosed()) {
                            return;
                        }
                        final InetAddress address = socket.getInetAddress();

                        System.out.println("*等待服务器数据*");

                        // 读取数据
                        int BUFFER_SIZE = 1024 * 1024;

                        char[] data = new char[BUFFER_SIZE];
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                        int len = br.read(data);
                        if (len != -1) {
                            String rexml = String.valueOf(data, 0, len);
                            System.out.println("获取到服务器的信息：" + address + " " + rexml);
                        } else {
                            socket.close();
                            return;
                        }


                        //解析
//                        try {
//                            JSONObject jsonObject = new JSONObject(rexml);
//                            Log.e("JAVA", jsonObject.toString());
//                            Log.e("JAVA", "Band_4: " + ((JSONObject)jsonObject.get("Value")).get("Band_4"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }


//                        final byte[] data = new byte[audioBufSize];
//                        int readSize = reader.read(data);
//                        player.write(data, 0, audioBufSize * 2);

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshLogView("获取到服务器的信息：" + address + " :" + data.toString());
//                            }
//                        });

//                        final String msg = reader.readUTF();
//                        System.out.println("获取到服务器的信息：" + address + " :" + msg);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshLogView("获取到服务器的信息：" + address + " :" + msg.toString());
//                            }
//                        });




                        /*
                        final String msg = reader.readUTF();
                        System.out.println("获取到服务器的信息：" + address + " :"+ msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLogView("获取到服务器的信息：" + address + " :"+ msg);
                            }
                        });
                        */



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
