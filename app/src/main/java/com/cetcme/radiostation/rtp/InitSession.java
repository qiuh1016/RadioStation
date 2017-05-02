package com.cetcme.radiostation.rtp;

import com.cetcme.radiostation.jlibrtp.DataFrame;
import com.cetcme.radiostation.jlibrtp.Participant;
import com.cetcme.radiostation.jlibrtp.RTPAppIntf;
import com.cetcme.radiostation.jlibrtp.RTPSession;

import java.net.DatagramSocket;

/**
 * Created by qiuhong on 31/03/2017.
 */

public class InitSession implements RTPAppIntf {

    public RTPSession rtpSession = null;

    public InitSession() {
        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;

        try {
            rtpSocket = new DatagramSocket(8002);
            rtcpSocket = new DatagramSocket(8003);
        } catch (Exception e) {
            System.out.println("发送创建会话异常抛出:"+e);
        }

        //建立会话
        rtpSession = new RTPSession(rtpSocket, rtcpSocket);
        rtpSession.RTPSessionRegister(this,null,null);
        //设置参与者（目标IP地址，RTP端口，RTCP端口）
        Participant p = new Participant("192.168.226.116", 8004, 8005);
        rtpSession.addParticipant(p);
    }

    public void receiveData(DataFrame frame, Participant p){
        String s = new String(frame.getConcatenatedData());
        System.out.println("接收到数据: " + s + " , 参与者CNAME： "
                + p.getCNAME() + "同步源标识符("+p.getSSRC()+")");
    }


    public void userEvent(int type, Participant[] participant) {
        // TODO Auto-generated method stub
    }


    public int frameSize(int payloadType) {
        return 1;
    }

}
