package com.femtioprocent.fpd2.util;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

public class SundryUtil {
    private static final Logger logger = LoggerFactory.getLogger(SundryUtil.class);

    private static int count = 0;

    public static void gcStat() {
        gcStat("");
    }

    public static synchronized void gcStat(String msg) {
        long ct = System.currentTimeMillis();
        count++;
        long free1 = Runtime.getRuntime().freeMemory();
        System.gc();
        // System.gc();
        long free2 = Runtime.getRuntime().freeMemory();
        long ctg = System.currentTimeMillis();
        logger.info("count (max total free) used -> used [freed] gc: " + count + " (" + Runtime.getRuntime().maxMemory() * 0.000001 + ' '
                + Runtime.getRuntime().totalMemory() * 0.000001 + ' ' + free2 * 0.000001 + ") "
                + (Runtime.getRuntime().totalMemory() - free1) * 0.000001 + " -> " + (Runtime.getRuntime().totalMemory() - free2)
                * 0.000001 + " [" + (free1 - free2) * 0.000001 + "] " + (ctg - ct) + " ms " + msg);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(T v) {
        List<T> l = new ArrayList();
        l.add(v);
        return l;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(T... va) {
        List<T> l = new ArrayList();
        for (T t : va)
            l.add(t);
        return l;
    }

    public static String formatDateDiff(Date d1, Date d2) {
        return formatMilliTime(d1.getTime(), d2.getTime(), false, true);
    }

    public static String formatMilliTime(long l1, long l2, boolean suppressZero, boolean withMS) {
        long diff = l1 - l2;
        String prefix = "";
        if (diff < 0) {
            diff = -diff;
            prefix = "-";
        }
        int ms = (int) (diff % 1000);
        diff /= 1000;
        int sec = (int) (diff % 60);
        diff /= 60;
        int min = (int) (diff % 60);
        diff /= 60;
        int hour = (int) (diff % 24);
        diff /= 24;
        int day = (int) diff;
        String mss = "00" + ms;
        int l = mss.length();
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        if (!suppressZero || day != 0) {
            sb.append(day);
            sb.append("d");
        }
        if (!suppressZero || hour != 0) {
            sb.append(hour);
            sb.append("h");
        }
        if (!suppressZero || min != 0) {
            sb.append(min);
            sb.append("m");
        }
        sb.append(sec);
        sb.append("s");
        if (withMS) {
            sb.append(mss.substring(l - 3));
        }
        return sb.toString();
    }

    public static void trace(int jsp_line) {
        try {
            throw new Exception("for tracing");
        } catch (Exception ex) {
            StackTraceElement[] stack = ex.getStackTrace();
            int i = 1;
            logger.info("STACK: " + i + "java:" + (stack[i].getLineNumber()) + " = jsp+" + (stack[i].getLineNumber() - jsp_line) + " jsp:"
                    + jsp_line + " file: " + stack[i].getFileName());
        }
    }

    /**
     * split a list into smaller list of lists, each sublist having max size
     * 
     * @param <T>
     * @param list
     * @param size
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int size) {
        List<List<T>> split = new ArrayList<List<T>>();
        for (int i = 0; i < list.size(); i += size) {
            int from = i;
            int to = i + size;
            if (to > list.size())
                to = list.size();
            split.add(list.subList(from, to));
        }
        return split;
    }

    /**
     * Extract all items in value list into one big. The order is arbitrary.
     * 
     * @param <T>
     * @param <T2>
     * @param map
     * @return
     */
    public static <T, T2> List<T> extractAll(HashMap<T2, List<T>> map) {
        List<T> list = new ArrayList<T>();
        for (List<T> l : map.values())
            list.addAll(l);
        return list;
    }

    /**
     * Convert a list of pairs into a pair of list, join list content.
     * 
     * [Pair<List<La1>, List<La2>>, Pair<List<Lb1>, List<Lb2>>, ..., Pair<List<Ln1>, List<Ln2>>] -> Pair<List<La1, Lb1, Ln1>, List<La2, Lb2, Ln2>>
     * 
     * @param <T>
     * @param <T2>
     * @param col
     * @return
     */
    public static <T> Pair<List<T>> extractAllPair(Collection<Pair<List<T>>> col) {
        List<T> fstList = new ArrayList<T>();
        List<T> sndList = new ArrayList<T>();
        for (Pair<List<T>> pl : col) {
            fstList.addAll(pl.fst);
            sndList.addAll(pl.snd);
        }
        return new Pair(fstList, sndList);
    }

    public static Set<Integer> extractIntegers(String s, String re) {
        Set<Integer> set = new HashSet<Integer>();
        String[] sa = s.split(re);
        for (String s1 : sa) {
            try {
                int a = Integer.parseInt(s1.trim());
                set.add(a);
            } catch (Exception _) {
                logger.severe("Not an integer string value: " + s1);
            }
        }
        return set;
    }

    /**
     * Convert 3 column table into hashmap of hasmaps
     * 
    <code>
      {hm-1-key, hm-2-key1, hm2-value1}
      {hm-1-key, hm-2-key2, hm2-value2}
      {hm-1-key, hm-2-key3, hm2-value3}
      {hm-1-key2, hm-2-key1, hm2-value4}
      {hm-1-key2, hm-2-key2, hm2-value5}
      {hm-1-key2, hm-2-key3, hm2-value6}
      
      ->
      
      {hm-1-key -> {hm-2-keyn -> hm-value}}
      
    </code>
     * @param tab
     * @return
     */
    public static HashMap<String, HashMap<String, String>> populateMapMap(String[][] tab) {
        HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
        for (String[] sa3 : tab) {
            String key1 = sa3[0];
            HashMap<String, String> m = map.get(key1);
            if (m == null) {
                m = new HashMap<String, String>();
                map.put(key1, m);
            }
            m.put(sa3[1], sa3[2]);
        }
        return map;
    }

    public static <T1,T2> SortedMap<T1,T2> convertToSortedMap(Map<T1,T2> map) {
        if ( map == null )
            return null;
        if ( map instanceof SortedMap<?,?> )
            return (SortedMap<T1,T2>) map;
        SortedMap<T1,T2> sm = new TreeMap<T1,T2>();
        sm.putAll(map);
        return sm;
    }

    public static String whichMethod() {
        return whichMethod(1, false);
    }

    public static String whichMethodAndClass() {
        return whichMethod(1, true);
    }

    public static String whichMethod(int offs) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String method = stackTrace[3 + offs].getMethodName();
        return method;
    }

    public static String whichMethod(int offs, boolean withClass) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String method = stackTrace[3 + offs].getClassName() + ':' + stackTrace[3 + offs].getMethodName();
        return method;
    }

    public static String getWeekList(YearWeek upToWeek, int howMany) {
        if (upToWeek.isNull())
            upToWeek = new YearWeek(new Date());
        DelimitedStringBuilder dsb = new DelimitedStringBuilder(",");
        for (int i = howMany; i > 0; i--) {
            Date wdate = new Date(upToWeek.getDate().getTime() - i * (1000L * 3600 * 24 * 7));
            YearWeek wweek = new YearWeek(wdate);
            String vw = wweek.getWeek();
            dsb.append(vw);
        }
        return dsb.toString();
    }

    public static String currentWeek() {
        YearWeek vw = new YearWeek(new Date());
        String week = vw.getWeek();
        return week;
    }

    public static Date timeSetEven(Date date) {
        if (date == null)
            return timeSetEven(new Date());
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) & ~1);
        return now.getTime();
    }

    public static Date timeSetOdd(Date date) {
        if (date == null)
            return timeSetOdd(new Date());
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) | 1);
        return now.getTime();
    }

    public static boolean timeIsEven(long ms) {
        long seconds = ms / 1000;
        return (seconds & 1) == 0;
    }

    public static boolean timeIsEven(Date date) {
        if (date == null)
            return false;
        return timeIsEven(date.getTime());
    }

    public static boolean timeIsOdd(long ms) {
        return !timeIsEven(ms);
    }

    public static boolean timeIsOdd(Date date) {
        return !timeIsEven(date.getTime());
    }

    public static Date currentTimeSetEven() {
        return timeSetEven(new Date());
    }

    public static Date currentTimeSetOdd() {
        return timeSetOdd(new Date());
    }

    public static Date getEarlyDateConstant() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse("2010-01-01 00:00:00");
        } catch (ParseException e) {
            return null;
        }
    }

    public static void trim(String[] sourceArr) {
        for (int i = 0; i < sourceArr.length; i++)
            sourceArr[i] = trim(sourceArr[i]);
    }

    private static String trim(String s) {
        if (s == null)
            return null;
        return s.trim();
    }

    // simple crypt/decrypt
    static final String oef62xc = "cvbnmQWERTY";
    static final String nmdb = "RTYASDFGH";
    static final String nmsovf = "VBNM01278";
    static final String djlfb53 = "LZXfqwghjk";
    static final String svo = "lz3nmUI";
    static final String tetge5 = "ZXCVBNM01";
    static final String xr35e = "qwertyuiopa";
    static final String jbvg4 = "sdfghjklzx";
    static final String opiwehfg = "iopaQWE";
    static final String sklvnj34 = "tysOPu";
    static final String a = "456xcvC";
    static final String mv34 = "23456789";
    static final String rete4 = "UIOPASDFGHJKL";
    static final String q1 = "9derbJK";

    private static String mix(String s, String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            int ix = s1.indexOf(ch);
            if (ix > -1)
                sb.append(s2.charAt(ix));
            else
                sb.append(ch);
        }
        s = sb.toString();
        return s;
    }

    public static String crypt(String s) {
        return mix(s, xr35e + jbvg4 + oef62xc + rete4 + tetge5 + mv34, sklvnj34 + opiwehfg + nmdb + a + nmsovf + q1 + djlfb53 + svo);
    }

    public static String decrypt(String s) {
        return mix(s, sklvnj34 + opiwehfg + nmdb + a + nmsovf + q1 + djlfb53 + svo, xr35e + jbvg4 + oef62xc + rete4 + tetge5 + mv34);
    }

    public static int[] convertToIntArr(String csv) {
        if (csv == null || csv.length() == 0)
            return null;
        String[] sa = csv.split(",");
        int[] ia = new int[sa.length];
        int ix = 0;
        for (String s : sa) {
            int val = Integer.parseInt(s.trim());
            ia[ix++] = val;
        }
        return ia;
    }

    /**
     * Return if the argument s is like a numeric sql id.
     * 
     * @param s
     * @return
     */
    public static boolean isValueLikeNumericId(String s) {
        return s != null && s.length() > 0 && !s.equals("0");
    }
}
