package com.gn.syslog.run;

import java.io.UnsupportedEncodingException;

/**
 * Naive-GN
 * Created by guoning on 15/11/19.
 */
public interface Sender {
    void send(String msg) throws Exception;

    void close();

}
