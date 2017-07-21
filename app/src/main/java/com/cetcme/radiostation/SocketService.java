package com.cetcme.radiostation;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketService extends Service {

    private String serverIP = "192.168.0.194";
    private int serverPort = 9000;

    private Socket socket;

    private static String testStr = "123";

    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    /**
     * 建立服务端连接
     */
    public void conn() {
        Log.e("JAVA", "to 建立连接：");
        testStr = "hasChanged 123";
        new Thread() {

            @Override
            public void run() {

                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(serverIP, serverPort), 2000);
                    Log.e("JAVA", "建立连接：" + socket);

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
     * 从参数的Socket里获取最新的消息
     */
    private void startReader() {

        new Thread() {
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
                        System.out.println("获取到服务器的信息：" + address + " :" + msg);


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 发送消息
     */
    public void send(final String message) {
        new Thread() {
            @Override
            public void run() {

                try {
                    // socket.getInputStream()
                    if (socket == null) {
                        return;
                    }
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type","1");
                        jsonObject.put("content", message);

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

    public static void test() {
        Log.e("JAVA", "test：" + testStr);
    }

}
