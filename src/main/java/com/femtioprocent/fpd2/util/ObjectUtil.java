package com.femtioprocent.fpd2.util;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class ObjectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    public static <T> String convertToString(T t) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(baos));
            e.writeObject(t);
            e.close();
            return baos.toString("utf-8");
        } catch (Exception ex) {
            logger.severe("Can't XMLDecode " + ex);
        }
        return null;
    }

    /**
     */
    @SuppressWarnings("unchecked")
    public static <T> T recreateFromString(String s) {
        try {
            byte[] bytes = s.getBytes("utf-8");
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            XMLDecoder d = new XMLDecoder(new BufferedInputStream(bais));
            T t = (T) d.readObject();
            d.close();
            return t;
        } catch (IOException ex) {
            logger.severe("Can't XMLDecode " + ex);
        } catch (Exception ex) {
            logger.severe("Can't XMLDecode " + ex);
        }
        return null;
    }
}
