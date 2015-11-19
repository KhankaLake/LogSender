package com.gn.syslog.util;

import com.thoughtworks.xstream.XStream;

import java.io.File;

/**
 * Copyright: 版权所有 ( c ) 北京启明星辰信息安全技术有限公司 2013。保留所有权利
 * 作者: 郭宁
 * 创建时间: 14-8-31 下午9:14
 * 文件描述:
 * 修改描述:
 */
public class XStreamHelper {

    private static XStream XS = new XStream();

    /**
     * bean转换成xml
     *
     * @param obj
     * @return
     */
    public static String beanToXml(Object obj) {
        return XS.toXML(obj);
    }

    public static <T> T xmlToBean(String xml) {
        return (T) XS.fromXML(xml);
    }

    public static <T> T xmlToBxean(File file) {
        return (T) XS.fromXML(file);
    }

}
