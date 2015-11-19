package com.gn.syslog.run;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Naive-GN
 * Created by guoning on 15/11/19.
 */

public class TcpClientSocket implements Sender {

    private String host="";
    private int port;

    private Socket socket = null;

    // 向服务器端发送数据
    private OutputStream os;
    private DataOutputStream bos;

    public TcpClientSocket(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        init();
    }
    private void  init() throws IOException {
        socket = new Socket(host,port);
        os = socket.getOutputStream();
        bos = new DataOutputStream(os);
    }
    
    public void send(String msg){
        try {
            bos.writeUTF(msg);
            bos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            bos.flush();
            bos.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}