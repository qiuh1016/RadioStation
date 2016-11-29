package com.cetcme.radiostation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        new SocketServer();
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
                    socket = new Socket("192.168.0.138", 9999);
                    Log.e("JAVA", "建立连接：" + socket);
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
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                    writer.writeUTF("我是客户端.."); // 写一个UTF-8的信息

                    System.out.println("发送消息");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 从参数的Socket里获取最新的消息
     */
    private static void startReader(final Socket socket) {

        new Thread(){
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    InetAddress address = socket.getInetAddress();

                    while (true) {
                        System.out.println("*等待服务器数据*");
                        // 读取数据
                        String msg = reader.readUTF();
                        System.out.println("获取到服务器的信息：" + address + " :"+ msg);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
