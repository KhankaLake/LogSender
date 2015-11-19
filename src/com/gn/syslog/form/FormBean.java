package com.gn.syslog.form;

public class FormBean {
    private String host;
    private String port;
    private String total;
    private String kbps;
    private String template;
    private String msg;

    public FormBean() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(final String port) {
        this.port = port;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(final String total) {
        this.total = total;
    }

    public String getKbps() {
        return kbps;
    }

    public void setKbps(final String kbps) {
        this.kbps = kbps;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }
}