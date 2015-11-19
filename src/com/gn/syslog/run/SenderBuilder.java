package com.gn.syslog.run;

import java.io.IOException;

/**
 * Naive-GN
 * Created by guoning on 15/11/19.
 */
public class SenderBuilder {

    public static Sender build(String host,int port,String type ) throws Exception {
        Sender sender ;
        String _type = type.trim().toLowerCase();
        if("tcp".equals(_type)){
            sender = new TcpClientSocket(host,port);
        }else if("tcp".equals(_type)){
            sender = new UdpClientSocket(host,port);
        }else{
            sender = new SyslogClientSocket(host,port);
        }
        return sender;
    }

}
