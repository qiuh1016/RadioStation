package com.cetcme.radiostation;

import android.app.Application;
import android.util.Log;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by qiuhong on 21/09/2017.
 */

public class ApplicationUtil extends Application {

    private Socket socket;
    private OutputStreamWriter out = null;
    private InputStreamReader in = null;
    
    public void init() throws Exception{
        Log.d("Application", "init: socket");
        this.socket = new Socket("192.168.10.3", 9999);
        this.out = new OutputStreamWriter(socket.getOutputStream());
        this.in = new InputStreamReader(socket.getInputStream());
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

}
