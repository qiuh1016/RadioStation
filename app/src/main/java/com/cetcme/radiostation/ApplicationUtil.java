package com.cetcme.radiostation;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by qiuhong on 21/09/2017.
 */

public class ApplicationUtil extends Application {

    private Socket socket;
    private OutputStreamWriter out = null;
    private InputStreamReader in = null;

    public int BUFFER_SIZE = 1024 * 1024;

    String TAG = "ApplicationUtil";

    public static final int SOCKET_RECEIVE_DATA = 0x1000;
    public static final int SOCKET_DISCONNECTED = 0x1001;
    public static final int SOCKET_CONNECTED    = 0x1002;
    
    public void init() throws Exception{
        this.socket = new Socket();
        socket.connect(new InetSocketAddress("192.168.9.179", 7779), 2000);
        this.out = new OutputStreamWriter(socket.getOutputStream());
        this.in = new InputStreamReader(socket.getInputStream(), "UTF-8");
        startReader(handlerHashMap);
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    public OutputStreamWriter getOut() {
        return out;
    }
    
    public void setOut(OutputStreamWriter out) {
        this.out = out;
    }
    
    public InputStreamReader getIn() {
        return in;
    }
    
    public void setIn(InputStreamReader in) {
        this.in = in;
    }

    public void send(final JSONObject json) {
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("*to send*");
                    if (socket == null) {
                        return;
                    }
                    out.write(json.toString());
                    out.flush();
                    Log.e(TAG, "run: send");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public HashMap<String, Handler> handlerHashMap = new HashMap<>();

    public void startReader(final HashMap<String, Handler> handlerHashMap) {
        new Thread() {
            @Override
            public void run() {
                try {
//                    final InetAddress address = socket.getInetAddress();
                    BufferedReader br = new BufferedReader(in);
                    while (true) {
                        // 读取数据
                        char[] data = new char[BUFFER_SIZE];
                        int len = br.read(data);
                        if (len != -1) {
                            String rexml = String.valueOf(data, 0, len);
                            System.out.println("获取到服务器的信息：" + socket.getInetAddress() + " ");
                            System.out.println(rexml);

                            sendMsg(rexml);
                        } else {
                            socket.close();
                            return;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void sendMsg(String data) {
        Iterator iter = handlerHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Message msg = new Message();
            msg.what = SOCKET_RECEIVE_DATA;
            msg.obj = data;

            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Handler val = (Handler) entry.getValue();
            Log.e(TAG, "run: " + key);
            val.sendMessage(msg);
        }
    }

}
