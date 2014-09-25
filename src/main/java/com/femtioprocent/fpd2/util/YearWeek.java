package com.femtioprocent.fpd2.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class YearWeek {
    public String stringValue;
    public Date dateValue;
    public boolean nullIsNow = true;
    
    public static YearWeek NULL = new YearWeek(true);
    public static YearWeek NULLNULL = new YearWeek(false);
    
    public YearWeek(boolean b) {
        dateValue = null;
        stringValue = null;
        nullIsNow = b;
    }

    public YearWeek() {
        dateValue = new Date();
        stringValue = asWeek(dateValue, true);
    }
    
    public YearWeek(String s) {
        stringValue = s.replaceAll("[Ww]", "");
        dateValue = getWeekTime(stringValue);
    }
    
    public YearWeek(Date d) {
        dateValue = d;
        stringValue = asWeek(dateValue, true);
    }

    public Date getDate() {
        if ( nullIsNow && dateValue == null )
            return new Date();
        return dateValue;
    }

    /**
     * Return year week format 12w34
     * @return
     */
    public String getWeek() {
        return stringValue == null ? "" : stringValue;
    }

    public String getNumericWeek() {
        return stringValue == null ? "" : stringValue.replace("w", "");
    }

    public int getWeekAsInt() {
        return stringValue == null ? 0 : Integer.parseInt(stringValue.replace("w", ""));
    }

    public boolean isNull() {
        return dateValue == null && stringValue == null;
    }
    
    public static String asWeek(Date date, boolean withW) {
        Locale locale = Locale.getDefault();
        Calendar cal = GregorianCalendar.getInstance(locale);
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        int yy = cal.get(Calendar.YEAR) % 100;
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int ww = cal.get(Calendar.WEEK_OF_YEAR);
        if ( ww == 1 && mm == 11 ) {
            yy++;
        } else if ( ww > 51 && mm == 0 ) {
            yy--;
        }
        String syy;
        if ( yy < 10 )
            syy = "0" + yy;
        else
            syy = "" + yy;
        String sww;
        if ( ww < 10 )
            sww = "0" + ww;
        else
            sww = "" + ww;
        return withW ? syy + 'w' + sww : syy + sww;
    }
    
    /**
     * special (the hour and minute)
     * @param weekValue
     * @return
     */
    public static Date getWeekTime(String weekValue) {
        if ( weekValue == null || weekValue.length() == 0 )
            return null;
        long adjust = 0;
        int yyww = Integer.parseInt(weekValue);
        int ww = yyww % 100;
        int yy = yyww / 100;
        if ( ww < 2 ) {
            ww++;
            adjust = -3600L * 1000L * 24 * 7;
        }
        if ( ww > 51 ) {
            ww -= 4;
            adjust = 4 * 3600L * 1000L * 24 * 7;
        }
        Locale locale = Locale.getDefault();
        Calendar cal = GregorianCalendar.getInstance(locale);
        cal.setMinimalDaysInFirstWeek(4);
        cal.set(Calendar.YEAR, 2000 + yy);
        cal.set(Calendar.WEEK_OF_YEAR, ww);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        Date dadj = new Date(d.getTime() + adjust);
        return dadj;
    }

    private Date getWeekTime(String weekValue, int dayOffset, boolean atEndOfDays) {
        if ( weekValue == null || weekValue.length() == 0 )
            return null;

        long adjust = 0;

        int yyww = Integer.parseInt(weekValue);
        int ww = yyww % 100;
        int yy = yyww / 100;
        if ( ww < 2 ) {
            ww++;
            adjust = -3600L * 1000L * 24 * 7;
        }
        if ( ww > 51 ) {
            ww -= 4;
            adjust = 4 * 3600L * 1000L * 24 * 7;
        }
        Locale locale = Locale.getDefault();
        Calendar cal = GregorianCalendar.getInstance(locale);
        cal.setMinimalDaysInFirstWeek(4);
        cal.set(Calendar.YEAR, 2000 + yy);
        cal.set(Calendar.WEEK_OF_YEAR, ww);
        cal.set(Calendar.DAY_OF_WEEK, 1 + dayOffset);
        if ( atEndOfDays ) {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
        Date d = cal.getTime();
        Date dadj = new Date(d.getTime() + adjust);
        return dadj;
    }
    
    public String asRangeString() {
        return "[" + format(getWeekTime(getNumericWeek(), 1, false)) +" - " + format(new Date(getWeekTime(getNumericWeek(), 6, true).getTime() + 3600 * 1000L * 24)) + "]";
    }
    
    private String format(Date d) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(d);
    }
    
    public String toString() {
        return ToString.toString(this, "-NULL", "-NULLNULL");
    }
}
