package com.femtioprocent.fpd2.rt;

import java.util.HashMap;
import java.util.Map;

import com.femtioprocent.fpd2.util.DelimitedStringBuilder;
import com.femtioprocent.fpd2.util.SundryUtil;

public class AssertAtRuntime {
    public static Map<String, Object> map = new HashMap<String, Object>();

    public static void register(String key, boolean status) {
        map.put(key, status);
    }

    public static void register(Object obj) {
        String key = "" + SundryUtil.whichMethod(1, true);
        map.put(key, obj);
    }

    public static String dump(String delimiter) {
        DelimitedStringBuilder sb = new DelimitedStringBuilder(delimiter);
        for (Map.Entry<String, Object> ent : map.entrySet()) {
            sb.append("" + ent);
        }
        return sb.toString();
    }
}
