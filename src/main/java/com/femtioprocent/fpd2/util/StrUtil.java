package com.femtioprocent.fpd2.util;

import com.femtioprocent.fpd2.b.util.text.HTML2plain;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

    /**
     * Method that checks a string if it is null or empty string
     * Trims strings before checking length.
     * 
     * @return true if it is null or empty
     */
   public static boolean isStringNullOrEmpty(String string) {
       return string == null || string.trim().length() == 0;
   }

   /**
    * Method that checks a string if it is null or empty string
    * Trims strings before checking length.
    * 
    * @return true if it is null or empty
    */
  public static boolean isStringNullOrEmptySkipTrim(String string) {
      return string == null || string.length() == 0;
  }

    /**
     * Convert simple plain text to html
     * 
     * @param plain
     * @return
     */
    public static String toHtml(String plain) {
        if ( plain == null )
            return null;
        
        StringBuilder sb = new StringBuilder();
        boolean atBeginningOfLine = true;

//        if ( plain.contains("<br/>") ) // bad choice
//            return plain;
        sb.append("<p>");
        for(char ch : plain.toCharArray()) {
            if ( ch == ' ' && atBeginningOfLine )
                sb.append("&nbsp;");
            else {
                if ( ch == '\n' ) {
                    sb.append("<br/>\n");
                    atBeginningOfLine = true;
                } else if ( ch == '&' ) {
                    sb.append("&amp;");
                } else if ( ch == '<' ) {
                    sb.append("&lt;");
                } else if ( ch == '\'' ) {
                    sb.append("&apos;");
                } else if ( ch == '"' ) {
                    sb.append("&quot;");
                } else if ( ch == '>' ) {
                    sb.append("&gt;");
                } else {
                    atBeginningOfLine = false;
                    sb.append(ch);
                }
            }
        }
        sb.append("</p>");
        return sb.toString();
    }

    public static String toPlain(String html) throws Throwable {
        if ( html == null )
            return null;
        HTML2plain h2p = new HTML2plain();
        String plainText = h2p.convert(html);
        return plainText;
    }
    
    private static String fixRTF_Font(String rtfText) {
        return rtfText.replace("fcharset0 Helvetica", "fcharset0 Arial").replace("\\Q\\fs0\\E", "\\fs12");
    }

    public static void trim(String[] sArr) {
        if ( sArr == null )
            return;
        for(int i = 0; i < sArr.length; i++)
            sArr[i] = sArr[i].trim();
    }

    public static Set<String> asTrimmedSet(String[] sArr) {
        Set<String> set = new HashSet<String>();
        for(String s : sArr)
            set.add(s.trim());
        return set;
    }

    public static String fixAmp(String nv) {
        Pattern pat = Pattern.compile("&amp;([a-zA-Z]+;)");
        Matcher mat = pat.matcher(nv);
        boolean p = mat.find();
        if ( !p )
            return nv;
        int st = mat.start();
        int en = mat.end();
        return nv.substring(0, st) + "&" + mat.group(1) + nv.substring(en);
    }

    public static String formatWithParantesis(String s1, String s2) {
        return s1 + '(' + s2 + ')';
    }

    public static class CntWithMax {
        int cnt;
        int max;
        
        public CntWithMax(int max) {
            this.max = max;
        }
        
        public void inc() {
            cnt++;
        }

        public void incMax(int v) {
            max += v;
        }
        
        public String toString() {
            return formatWithParantesis(cnt, max);
        }

        public int getMax() {
            return max;
        }

        public int getCnt() {
            return cnt;
        }
    }

    public static String formatWithParantesis(int s1, int s2) {
        return formatWithParantesis(String.valueOf(s1), String.valueOf(s2));
    }
        
    /**
     * Format as "name - description", normal formatting. Empty is ""
     * @param s1
     * @param s2
     * @return
     */
    public static String formatNameDescription(String s1, String s2) {
        return formatNameDescription(s1, s2, false);
    }

    /**
     * Format as "name - description", empty is " - "
     * @param s1
     * @param s2
     * @param withEmptyValueMarker
     * @return
     */
    public static String formatNameDescription(String s1, String s2, boolean withEmptyValueMarker) {
        return formatNameDescription(s1, s2, withEmptyValueMarker ? " - " : "");
    }

    /**
     * Format as "name - description", empty is emptyMarker
     * 
     * @param s1
     * @param s2
     * @param valueWhenEmpty
     * @param withEmptyMarker
     * @return
     */
    public static String formatNameDescription(String s1, String s2, String valueWhenEmpty) {
        if ( isStringNullOrEmpty(s1) && isStringNullOrEmpty(s2) )
            return valueWhenEmpty;
        if ( isStringNullOrEmpty(s1) )
            return "- " + s2;
        if ( isStringNullOrEmpty(s2) )
            return s1;
        if ( s1.equals(s2) )
            return s1;
        return s1 + " - " + s2;
    }

    public static String nbsp(String string) {
        return string.replace(" ", "&nbsp;");
    }

    public static int extractInt(String kpiDocId) {
        Pattern pat = Pattern.compile("([0-9]*).*");
        Matcher matcher = pat.matcher(kpiDocId);
        if ( matcher.matches() ) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

    public static int[] extractIntList(String kpiDocId) {
        String[] sa = kpiDocId.split(";");
        int[] ia = new int[sa.length];
        int ix = 0;
        for(String s : sa) {
            ia[ix++] = extractInt(s);
        }
        return ia;
    }
    
    /**
     * Return a string expanded with blank to width. Expand the width if s is larger.
     */
    public static class ExpandWidther {
        int width;
        String suffix = "";
        
        public ExpandWidther(int width) {
            this.width = width;
        }

        public ExpandWidther(String suffix) {
            this.suffix = suffix;
        }

        public ExpandWidther(int width, String suffix) {
            this.width = width;
            this.suffix = suffix;
        }
        
        public String f(String s) {
            if ( s.length() > width )
                width = s.length();
            return String.format("%" + width + "s", s) + suffix;
        }
    }
}
