package com.cetcme.radiostation;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketActivity extends AppCompatActivity {

    private TextView mTextView;
    private EditText ipEditText;
    private EditText portEditText;

//    private String serverIP = "192.168.1.179";
//    private int serverPort = 1025;

    private String serverIP = "192.168.0.219";
    private int serverPort = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);


        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTextView.setText("LOG:");

        ipEditText = (EditText) findViewById(R.id.ip_editText);
        portEditText = (EditText) findViewById(R.id.port_editText);
        ipEditText.setText(serverIP);
        portEditText.setText(serverPort + "");

//        new SocketServer();
    }

    private Socket socket;

    /**
     * 建立服务端连接
     */
    public void conn(View v) {
        new Thread() {

            @Override
            public void run() {

                try {
//                    socket = new Socket(serverIP, serverPort);
//                    socket = new Socket(ipEditText.getText().toString(), Integer.parseInt(portEditText.getText().toString()));
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ipEditText.getText().toString(), Integer.parseInt(portEditText.getText().toString())),2000);
                    Log.e("JAVA", "建立连接：" + socket);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLogView("建立连接：" + socket);
                        }
                    });

                    startReader(socket);
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
//                    writer.writeUTF("我是客户端.."); // 写一个UTF-8的信息
                    writer.write(getPCMData());

                    System.out.println("发送消息");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLogView("发送消息：我是客户端");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 从参数的Socket里获取最新的消息
     */
    private void startReader(final Socket socket) {

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

    void refreshLogView(String msg){
        mTextView.append("\n" + msg);
        int offset = mTextView.getLineCount() * mTextView.getLineHeight();
        if (offset > mTextView.getHeight()) {
            mTextView.scrollTo(0, offset - mTextView.getHeight());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in_no_alpha,
                R.anim.push_right_out_no_alpha);
    }


    String  filePath  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/testmusic-0.pcm";
    //String  filePath  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/0RadioStation/FinalAudio.wav";

    /*
     * 获得PCM音频数据
     */
    public byte[] getPCMData()
    {

        File file = new File(filePath);
        if (file == null){
            return null;
        }

        FileInputStream inStream;
        try {
            inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        byte[] data_pack = null;
        if (inStream != null){
            long size = file.length();

            data_pack = new byte[(int) size];
            try {
                inStream.read(data_pack);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

        }

        return data_pack;
    }

}
