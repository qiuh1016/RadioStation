package com.cetcme.radiostation;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by qiuhong on 21/09/2017.
 */

public class ApplicationUtil extends Application {

    private Socket socket = null;
    private OutputStreamWriter out = null;
    private InputStreamReader in = null;

    public int BUFFER_SIZE = 1024 * 1024;

    String TAG = "ApplicationUtil";

    public static final int SOCKET_RECEIVE_DATA = 0x1000;
    public static final int SOCKET_DISCONNECTED = 0x1001;
    public static final int SOCKET_CONNECTED    = 0x1002;
    
    public void init() throws Exception{
        this.socket = new Socket();
        socket.connect(new InetSocketAddress("192.168.9.179", 7779), 5000);
        this.out = new OutputStreamWriter(socket.getOutputStream());
        this.in = new InputStreamReader(socket.getInputStream(), "UTF-8");
        startReader();
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

//                    socket.sendUrgentData(0xFF);
                } catch (IOException e) {
                    disconnectSocket();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public HashMap<String, Handler> handlerHashMap = new HashMap<>();

    public void startReader() {
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader br = new BufferedReader(in);
                    while (true) {
                        // 读取数据
                        char[] data = new char[BUFFER_SIZE];
                        int len = br.read(data);
                        if (len != -1) {
                            String rexml = String.valueOf(data, 0, len);
                            System.out.println("获取到服务器的信息：" + socket.getInetAddress() + " ");
                            System.out.println(rexml);

                            sendMsg(rexml, SOCKET_RECEIVE_DATA);
                        } else {
                            disconnectSocket();
                            return;
                        }
                    }
                } catch (IOException e) {
                    disconnectSocket();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void sendMsg(String data, int what) {
        Iterator iter = handlerHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Message msg = new Message();
            msg.what = what;
            msg.obj = data;

            Map.Entry entry = (Map.Entry) iter.next();
            Handler val = (Handler) entry.getValue();
            val.sendMessage(msg);
        }
    }

    private void sendMsg(String data, int what, String key) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = data;

        Handler handler = handlerHashMap.get(key);
        if (handler == null) {
            return;
        }
        handler.sendMessage(msg);
    }

    public void disconnectSocket() {
        try {
            socket.close();
            sendMsg("断开连接", SOCKET_DISCONNECTED, "MainActivity");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectSocket() {
        final boolean[] noErr = {true};
        new Thread() {
            @Override
            public void run() {
                try {
                    init();
                } catch (IOException e1) {
                    noErr[0] = false;
                    sendMsg("连接失败", SOCKET_DISCONNECTED, "SocketConnectDialog");
                    e1.printStackTrace();
                } catch (Exception e1) {
                    noErr[0] = false;
                    sendMsg("连接失败", SOCKET_DISCONNECTED, "SocketConnectDialog");
                    e1.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                while (noErr[0]) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (socket.isConnected()) {
                        Log.e(TAG, "建立连接：" + socket);
                        sendMsg("", SOCKET_CONNECTED);
                        setKeepaliveSocketOptions(socket, 4, 5, 6);
                        noErr[0] = false;
                    }
                }
                noErr[0] = true;
            }
        }.start();

    }

    private final static int SOL_TCP = 6;
    private final static int TCP_KEEPIDLE = 4;
    private final static int TCP_KEEPINTVL = 5;
    private final static int TCP_KEEPCNT = 6;

    protected void setKeepaliveSocketOptions(Socket socket, int idleTimeout, int interval, int count) {
        try {
            socket.setKeepAlive(true);
            try {
                Field socketImplField = Class.forName("java.net.Socket").getDeclaredField("impl");
                socketImplField.setAccessible(true);
                if(socketImplField != null) {
                    Object plainSocketImpl = socketImplField.get(socket);
                    Field fileDescriptorField = Class.forName("java.net.SocketImpl").getDeclaredField("fd");
                    if(fileDescriptorField != null) {
                        fileDescriptorField.setAccessible(true);
                        FileDescriptor fileDescriptor = (FileDescriptor)fileDescriptorField.get(plainSocketImpl);
                        Class libCoreClass = Class.forName("libcore.io.Libcore");
                        Field osField = libCoreClass.getDeclaredField("os");
                        osField.setAccessible(true);
                        Object libcoreOs = osField.get(libCoreClass);
                        Method setSocketOptsMethod = Class.forName("libcore.io.ForwardingOs").getDeclaredMethod("setsockoptInt", FileDescriptor.class, int.class, int.class, int.class);
                        if(setSocketOptsMethod != null) {
                            setSocketOptsMethod.invoke(libcoreOs, fileDescriptor, SOL_TCP, TCP_KEEPIDLE, idleTimeout);
                            setSocketOptsMethod.invoke(libcoreOs, fileDescriptor, SOL_TCP, TCP_KEEPINTVL, interval);
                            setSocketOptsMethod.invoke(libcoreOs, fileDescriptor, SOL_TCP, TCP_KEEPCNT, count);
                        }
                    }
                }
            }
            catch (Exception reflectionException) {
                disconnectSocket();
            }
        } catch (SocketException e) {
            disconnectSocket();
        }
    }

}
