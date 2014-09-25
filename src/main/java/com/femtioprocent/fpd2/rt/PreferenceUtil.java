package com.femtioprocent.fpd2.rt;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class PreferenceUtil {
    private static final Logger logger = LoggerFactory.getLogger(PreferenceUtil.class);

    Class<?> owner;
    String ownername;

    static HashMap<String, Object> map = new HashMap<String, Object>();
    
    public PreferenceUtil(Class<?> owner) {
        this.owner = owner;
        String s = owner.getName();
        this.ownername = s.substring(s.lastIndexOf('.')+1);
    }

    /**
       Save an Object to the user preference area. Use the supplied id.
     */
    public void save(String subid, Object object) {
        map.put(subid, object);
        logger.info("SAVED map: " + map);
        Preferences prefs = Preferences.userNodeForPackage(owner);
        logger.info("save pref: " + subid + ' ' + prefs);
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(object);
            oo.close();
            byte[] ba = bo.toByteArray();
            byte[][] baa = split(ba, 1000);
            for(int i = 0; i < baa.length; i++) {
                prefs.putByteArray(ownername + ":" + subid + '-' + i, baa[i]);
            }
            prefs.putInt(ownername + ":" + subid + "-size", baa.length);
        } catch (Exception ex) {
            logger.severe("save pref: " + ex);
        } catch (Error ex) {
            logger.severe("save pref: " + ex);
        }
    }

    private byte[][] split(byte[] ba, int size) {
        int l = ba.length / size + (ba.length%size == 0 ? 0 : 1);
        byte baa[][] = new byte[l][];
        return split(baa, ba, size, 0);
    }

    private byte[][] split(byte[][] baa, byte[]ba, int size, int n) {
        int offs = n * size;
        int rest = ba.length - offs;
        int l = rest > size ? size : rest;
        byte[] nba = new byte[l];
        System.arraycopy(ba, offs, nba, 0, l);
        baa[n] = nba;
        if ( rest > size ) {
            split(baa, ba, size, n+1);
        }
        return baa;
    }

    /**
       Load a saved Object from the user preference area. Use the id to get it.
       Return the default value if nothing found.
     */
    public Object load(String subid, Object def) {
        if ( map.containsKey(subid) ) {
            return map.get(subid);
        }
        Preferences prefs = Preferences.userNodeForPackage(owner);
        try {
            int size = prefs.getInt(ownername + ":" + subid + "-size", 100);
            byte[] ba = load(prefs, ownername + ":" + subid, new ByteArrayOutputStream(), 0, size);
            if ( ba != null ) {
                ByteArrayInputStream is = new ByteArrayInputStream(ba);
                ObjectInputStream oi = new ObjectInputStream(is);
                Object object = oi.readObject();
                map.put(subid, object);
                logger.info("load pref: -> " + subid + ' ' + object);
                return object;
            } else {
                logger.info("load pref: -> " + subid + ' ' + "no byte[]");
            }
        } catch (Exception ex) {
            logger.severe("load pref: " + subid + ' ' + ex);
        } catch (Error ex) {
            logger.severe("load pref: " + subid + ' ' + ex);
        }
        map.put(subid, def);
        return def;
    }

    private byte[] load(Preferences prefs, String key, ByteArrayOutputStream bao, int n, int max) {
        byte[] ba = prefs.getByteArray(key + '-' + n, null);
        if ( ba == null )
            return bao.toByteArray();
        bao.write(ba, 0, ba.length);
        n++;
        if ( n == max )
            return bao.toByteArray();
        return load(prefs, key, bao, n, max);
    }

    private Object getObject(String sub_id, Object def) {
        Object o = map.get(sub_id);
        if ( o == null )
            return def;
        return o;
    }

//    static public void main(String[] args) {
//        PreferenceUtil pu = new PreferenceUtil(PreferenceUtil.class);
//        HashMap hm = (HashMap)pu.getObject("test_obj", new HashMap());
//        S.pe("get " + hm);
//        hm.put(args[0], args[1]);
//        pu.save("test_obj", hm);
//        S.pe("saved " + hm);
//        S.flush();
//    }
}
