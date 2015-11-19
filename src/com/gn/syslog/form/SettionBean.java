package com.gn.syslog.form;

import com.gn.syslog.util.StrUtil;

public class SettionBean {
    private String ip = "127.0.0.1";
    private int port = 514;
    private String ip_port = "127.0.0.1:514";
    private int total = 0;
    private int kbps = 1;
    private String sysLogTemp = "";

    public SettionBean() {
    }

    public String getIp_port() {
        return ip_port;
    }

    public void setIp_port(String ip_port) {
        this.ip_port = ip_port;
        if (!StrUtil.isEmpty(ip_port)) {
            String[] ipPort = ip_port.split(":");
            this.ip = ipPort[0];
            if (ipPort.length == 2) {
                this.port = Integer.parseInt(ipPort[1]);
            }
        }
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getKbps() {
        return kbps;
    }

    public void setKbps(int kbps) {
        this.kbps = kbps;
    }

    public String getSysLogTemp() {
        return sysLogTemp;
    }

    public void setSysLogTemp(String sysLogTemp) {
        this.sysLogTemp = sysLogTemp;
    }
}