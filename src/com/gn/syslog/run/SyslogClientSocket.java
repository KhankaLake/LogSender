package com.gn.syslog.run;


import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogIF;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Naive-GN
 * Created by guoning on 15/11/1.
 */
public class SyslogClientSocket implements Sender {

    private String host;
    private int port;

    //获取syslog的操作类，使用udp协议。syslog支持"udp", "tcp", "unix_syslog", "unix_socket"协议
    SyslogIF syslog;



    public SyslogClientSocket(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    private void init(){
        syslog = Syslog.getInstance("udp");
        //设置syslog服务器端地址
        syslog.getConfig().setHost(host);
        //设置syslog接收端口，默认514
        syslog.getConfig().setPort(port);
    }

    public void send(String msg) throws UnsupportedEncodingException {
        syslog.log(0, URLDecoder.decode(msg, "utf-8"));
    }

    @Override
    public void close() {
        syslog = null;
    }
}
