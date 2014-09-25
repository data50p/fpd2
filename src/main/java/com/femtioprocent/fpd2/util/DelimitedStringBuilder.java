package com.femtioprocent.fpd2.util;

/**
 * (Almost) Same as StringBuilder but insert a delimiter between appends.
 * 
 * only append(String) is implemented
 * 
 */
public class DelimitedStringBuilder {
    String delim1 = null;
    String delim = "";
    StringBuilder sb = new StringBuilder();
    
    public DelimitedStringBuilder() {
    }

    public DelimitedStringBuilder(String delim) {
        this.delim = delim;
    }

    public DelimitedStringBuilder(String delim1, String delim) {
        this.delim1 = delim1;
        this.delim = delim;
    }
    
    public void append(String s) {
        if ( sb.length() > 0 ) {
            if ( delim1 != null ) {
                sb.append(delim1);
                delim1 = null;
            } else {
                sb.append(delim);
            }
        }
        sb.append(s);
    }
    
    public String toString() {
        return sb.toString();
    }

    public String toString(String prefix, String suffix) {
        return prefix + sb.toString() + suffix;
    }

    public int length() {
        return sb.length();
    }
}
