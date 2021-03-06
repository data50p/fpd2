/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femtioprocent.fpd2.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.lang.reflect.Field;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class makes it easy to create a String representation of an instance with all known members value.
 * Convenient to have this in the toString method.
 * 
 * Format:</br/>
 *   -> <ClassName>{<variable_1>=<value_1>, <variable_2>=<value_2>, <variable_N>=<value_N>}<br/>
 *
 * Use regular pattern such as '-<RE>' to romeve variables and '+<RE>' to add variables in the output.
 * Use regular pattern "" to supress class name
 *
 * toString(obj, "-.*Foo.*")<br/>
 *   -> ToString{nominalInt=123, nominalString=nominal value}<br/>

 * toString(obj, "-.*Foo.*", "+.*Str.*)<br/>
 *   -> ToString{nominalInt=123, nominalString=nominal value, nominalFooString=nominal foo value}<br/>
 * <br/>
 * toString(obj, "")<br/>
 *    -> nominalInt=123, nominalString=nominal value, nominalFooInt=12345, nominalFooString=nominal foo value<br/>
 * <br/>
 * <p/>
 * public String toString() {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;return ToString.toString(this);</br>
 * }</br>
 * 
 * @author lars
 */
public class ToString {

    /**
     * Used as a demo for ToString
     */
    int nominalInt = 123;
    /**
     * Used as a demo for ToString
     */
    String nominalString = "nominal value";

    /**
     * Used as a demo for ToString
     */
    int nominalFooInt = 12345;
    /**
     * Used as a demo for ToString
     */
    String nominalFooString = "nominal foo value";

    /**
     * Create a string consisting of the class base name and a list of all members and its value.
     *
     * @param obj
     * @param specs
     * @return
     */
    public static String toString(Object obj) {
        return toString_(false, obj, "", "=", "", ", ", new String[0]);
    }

    public static String toString(Object obj, String... specs) {
        return toString_(false, obj, "", "=", "", ", ", specs);
    }

    public static String toString_(boolean escape, Object obj, String prefix, String eq, String suffix, String delimiter, String... specs) {
        boolean noClassName = false;

        if (obj == null) {
            return "=null=";
        }

        if (obj.getClass() == String.class) {
            return obj.toString();
        } else if (obj.getClass() == int.class) {
            return obj.toString();
        } else if (obj.getClass() == Integer.class) {
            return obj.toString();
        } else if (obj.getClass() == long.class) {
            return obj.toString();
        } else if (obj.getClass() == Long.class) {
            return obj.toString();
        } else if (obj.getClass() == double.class) {
            return obj.toString();
        } else if (obj.getClass() == Double.class) {
            return obj.toString();
        } else if (obj.getClass() == GregorianCalendar.class) {
            GregorianCalendar gc = (GregorianCalendar) obj;
            return "" + gc.getTime();
        }

        String[] str = new String[specs.length];
        Pattern[] pat = new Pattern[specs.length];
        char[] type = new char[specs.length];
        for (int i = 0; i < pat.length; i++) {
            if (specs[i].length() == 0) {
                noClassName = true;
//                pat[i] = null;
            } else {
                char typ = specs[i].charAt(0);
                int ix = 0;
                if ( typ == '=' ) {
                    type[i] = '=';
                    pat[i] = null;
                    str[i] = specs[i].substring(1);
                    continue;
                }
                if (typ == '-' || typ == '+') {
                    ix = 1;
                }
                pat[i] = Pattern.compile(specs[i].substring(ix));
                if (ix == 1) {
                    type[i] = specs[i].charAt(0);
                }
            }
        }

        Class tc = obj.getClass();

        StringBuilder sb = new StringBuilder();

        if (!noClassName) {
            sb.append(last(tc.getName()) + '{');
        }

        Field[] fA = tc.getDeclaredFields();

        boolean first = true;
        FIELD:
        for (Field f : fA) {
            try {
                String name = f.getName();
                Boolean Bp = null;
                boolean bp = false;
                int i = 0;
                for (Pattern p : pat) {
                    if (p != null) {
                        Matcher m = p.matcher(name);
                        if (m.matches()) {
                            if (type[i] == '+') {
                                Bp = true;
                            } else if (type[i] == '-') {
                                Bp = false;
                            } else if (type[i] == '=') {
                                continue FIELD;
                            } else {
                                bp = true;
                            }
                        }
                    } else {
                    }
                    i++;
                }
                if (Bp == null && !bp || Bp != null && Bp) {
                    if (!first) {
                        sb.append(delimiter);
                    }
                    boolean b = f.isAccessible();
                    f.setAccessible(true);
                    String v = "";// obj.getClass().getName();
                    try {
                        Object oo = f.get(obj);
                        if (oo == null) {
                            v = "<null>";
                        } else {
                            // toString for a GregorianCalendar is huge
                            if (oo instanceof GregorianCalendar) {
                                v = "" + ((GregorianCalendar) oo).getTime();
                            } else {
                                v = oo.toString();
                            }
                        }
                    } catch (Exception _) {
                        System.err.println("" + _);
                        v = _.getMessage();
                    }
                    String vv = escape ? parseHTMLSafeAndEscape(v) : v;
                    sb.append(prefix + f.getName() + eq + vv + suffix);
                    if (!b) {
                        f.setAccessible(b);
                    }
                    first = false;
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ToString.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            }
        }
        int i = 0;
        for (Pattern p : pat) {
            if ( p == null && type[i] == '=' )
                sb.append(delimiter + str[i]);
            i++;
        }

        if (!noClassName) {
            sb.append("}");
        }

        return sb.toString();
    }

    private static String last(String s) {
        int ix = s.lastIndexOf('.');
        if (ix == -1) {
            return s;
        }
        return s.substring(ix + 1);
    }

    /**
     * return "ToString{nominalInt=123, nominalString=nominal value}"
     * @return
     */
    @Override
    public String toString() {
        return ToString.toString(this);
    }

    public static String parseHTMLSafe(String str) {
        if (str == null) {
            return "";
        }
        str = str.trim();
        if (str.length() == 0) {
            return "";
        }
        return str;
    }

    public static String parseHTMLSafeAndEscape(String value) {
        String str = parseHTMLSafe(value);
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("\\Q\"\\E", "&quot;");
        str = str.replaceAll("\\Q'\\E", "&apos;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        return str;
    }

    /**
     * Print out "ToString{nominalInt=123, nominalString=nominal value}"
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("" + toString(new ToString()));
        System.out.println("" + toString(new ToString(), args));
    }
}
