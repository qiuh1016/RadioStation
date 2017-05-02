package com.cetcme.radiostation.rtp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cetcme.radiostation.R;

public class RtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtp);

        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    openSession();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("RTP发送数据异常:"+e);
                }
            }
        });
        sendThread.start();
    }

    //Jlibrtp开启会话发送数据
    public void openSession(){
        InitSession test = new InitSession();
        long teststart = System.currentTimeMillis();
        String str = "abce abcd abce abce abce abcd abcd abce " +
                "abcd abce abcd abce abcd abce abcd abce abcd abce " +
                "abcd abce abcd abce abcd abce abcd abce abcd abce abcd " +
                "abce abcd abce abcd abce abcd abce abcd abce abcd abce " +
                "abcd abce abcd abce abcd abce abcd abce abcd abce abcd " +
                "abce abcd abce abcd abce abcd abce abcd abce abcd abce " +
                "abcd abce abcd abce abcd abce abcd abce abcd abce abcd " +
                "abce abcd abce abcd abce abcd abce abcd abce abcd abce " +
                "abcd abce abcd abce abcd abce abcd abce abcd abce abcd " +
                "abce abcd abce abcd abce abcd abce abcd abce abcd abce " +
                "abcd abce abcd abce abcd abce abcd abce abcd abce abcd " +
                "abce abcd abce abcd abce abcd abce abcd ";
        byte[] data = str.getBytes();
        System.out.println(data.length);
        int i = 0;
        while (i < data.length) {
            System.out.println("已发送第" + i + "个字节数组：" + data);
            test.rtpSession.sendData(data);
            i++;
        }

        long testend = System.currentTimeMillis();

        System.out.println("发送用时：" + (testend - teststart));
        System.out.println("结束时间：" + testend);
        System.out.println("开始时间：" + teststart);
    }

//    public void receiveData(){
//        ReceiveData receive = new ReceiveData();
//    }
}
