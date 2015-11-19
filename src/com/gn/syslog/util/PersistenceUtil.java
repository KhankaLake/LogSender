package com.gn.syslog.util;

import java.io.File;
import java.net.URL;

/**
 * Naive-GN
 * Created by guoning on 15/11/1.
 */
public class PersistenceUtil {


    public static void save(Object obj) {
        String xml = XStreamHelper.beanToXml(obj);

        try {
            FileHelper.writeString(getClassFile(PersistenceUtil.class) + File.separator, obj.getClass().getName() + ".xml", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T build(Class clazz) {
        File file = new File(getClassFile(PersistenceUtil.class) + File.separator + clazz.getName() + ".xml");
        if (!file.exists()) {
            try {
                return (T) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        return XStreamHelper.xmlToBxean(file);
    }


    public static File getClassFile(Class clazz) {
        URL path = clazz.getResource(clazz.getName().substring(
                clazz.getName().lastIndexOf(".") + 1)
                + ".class");
        if (path == null) {
            String name = clazz.getName().replaceAll("[.]", "/");
            path = clazz.getResource("/" + name + ".class");
        }
        return new File(path.getFile());
    }

}
