package com.gn.syslog.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Naive-GN
 * Created by guoning on 15/11/1.
 */
public class IPUtil {

    private static Random r = new Random();

    public static String getRandomIp4() {
        return r.nextInt(255) + "." + r.nextInt(255) + "." + r.nextInt(255) + "." + r.nextInt(255);
    }

    public static int getRandonPort(){
        return r.nextInt(10000);
    }


    public static int getInt(){
        return r.nextInt(255);
    }


    public static String getTime(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(new Date(System.currentTimeMillis()));
    }


    public static String getTime(long addTime){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(new Date(System.currentTimeMillis() + addTime));
    }


    public static void main(String[] args) {
        System.out.printf("${PORT}".replaceAll("\\$\\{PORT\\}", IPUtil.getRandonPort() + ""));
    }
}
