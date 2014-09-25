package com.femtioprocent.fpd2.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;


/**
 * <p>Helper class for Number to String conversion (and vice versa). Numbers include
 * both integer and decimal numbers.<br>
 * This conversion is Locale and TimeZone aware. There are two kinds of methods:
 * <ul>
 * <li>parse methods - for transforming data from String representation into Java representation,</li>
 * <li>format method - for transforming data from Java representation into String representation.</li>
 * </ul><br/></p>
 * 
 * <p>Conversion in both directions is done by the following rules:
 * <ul>
 * <li>If null and result is an object then null is returned else return formatted data</li>
 * <li>If null and result is not an object then 0 is returned else return formatted data</li>
 * </ul>
 * </p>
 * <p>Parsing (from String to Number) has the following extra rule:
 * <ul>
 * <li>If we have an empty String ("") and result is an object then null is returned</li>
 * <li>If we have an empty String ("") and result is not an object then 0 is returned</li>
 * </ul></p>
 * 
 */
public final class NumberFormatUtil {

    private static final String LOCALE_IS_NULL = "Locale is null, it must exist for conversion.";
    private static final String NUMBER_FORMATTER_IS_NULL = "Number formatter is null, it must exist for conversion.";
    private static final String UNPARSABLE_VALUE = "Unparsable value: ";

    /**
     * Cannot instantiate this class, must use static methods
     */
    private NumberFormatUtil() {
    }

    /**
     * Returns a formatter that formats decimal values for the given locale.
     * 
     * @param locale that will be used to determine formatter
     * @return formatted for the give locale
     * @throws NullPointerException if locale is null
     */
    public static NumberFormat getIntegerFormatter(Locale locale) {
        return getIntegerFormatter(locale, false);
    }

    /**
     * Returns a formatter that formats integer values (i.e. return values are not decimal values) for the given locale.
     * 
     * @param locale that will be used to determine formatter
     * @return formatted for the give locale
     * @throws NullPointerException if locale is null
     */
    public static NumberFormat getIntegerFormatter(Locale locale, boolean groupingUsed) {
        if (locale == null) {
            throw new NullPointerException(LOCALE_IS_NULL);
        }
        final NumberFormat nf = NumberFormat.getNumberInstance(locale);
        // parsing stops at the decimal separator
        nf.setParseIntegerOnly(true);
        nf.setGroupingUsed(groupingUsed);
        return nf;
    }

    /**
     * Returns a formatter that formats decimal values for the given locale.
     * 
     * @param locale that will be used to determine formatter
     * @return formatted for the give locale
     * @throws NullPointerException if locale is null
     */
    public static NumberFormat getDecimalFormatter(Locale locale) {
        return getDecimalFormatter(locale, false);
    }

    /**
     * Returns a formatter that formats decimal values for the given locale.
     * 
     * @param locale that will be used to determine formatter
     * @param groupingUsed determines if grouping delimiters should be visible or not
     * @throws NullPointerException if locale is null
     * @return formatted for the give locale
     */
    public static NumberFormat getDecimalFormatter(Locale locale, boolean groupingUsed) {
        if (locale == null) {
            throw new NullPointerException(LOCALE_IS_NULL);
        }
        final NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setGroupingUsed(groupingUsed);
        return nf;
    }

    /**
     * Method that parses a value with the specified formatter.
     * 
     * @param nf the formatter to use
     * @param value to be parsed
     * @return formatted Number value
     * @throws ParseException when number could not be parsed
     * @throws NullPointerException if number formatter is null
     */
    private static Number parseNumber(NumberFormat nf, String value) throws ParseException {
        if (nf == null) {
            throw new NullPointerException(NUMBER_FORMATTER_IS_NULL);
        }
        value = value.trim();
        ParsePosition position = new ParsePosition(0);
        Number number = nf.parse(value, position);
        if (position.getErrorIndex() != -1 || position.getIndex() < value.length()) {
            throw new ParseException(UNPARSABLE_VALUE + value, position.getErrorIndex());
        }
        return number;
    }

    /**
     * Parses int using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed int
     * @throws ParseException when value cannot be parsed
     */
    public static int parseIntValue(String value, Locale locale) throws ParseException {
        return parseIntValue(getIntegerFormatter(locale), value);
    }
    
    /**
     * Parses int using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed int
     * @throws ParseException when value cannot be parsed
     */
    public static int parseIntValue(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return 0;
        }
        value = value.trim();
        if (value.length() == 0) {
            return 0;
        }
        Number number = parseNumber(nf, value);
        return number.intValue();
    }
    
    /**
     * Parses Integer using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed Integer
     * @throws ParseException when value cannot be parsed
     */
    public static Integer parseInteger(String value, Locale locale) throws ParseException {
        return parseInteger(getIntegerFormatter(locale), value);
    }
    
    /**
     * Parses Integer using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed Integer
     * @throws ParseException when value cannot be parsed
     */
    public static Integer parseInteger(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() == 0) {
            return null;
        }
        Number number = parseNumber(nf, value);
        return new Integer(number.intValue());
    }

    /**
     * Formats int using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI int string
     */
    public static String formatInt(int value, Locale locale) {
        return formatInt(getIntegerFormatter(locale), value);
    }

    /**
     * Formats int using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI int string
     */
    public static String formatInt(final NumberFormat nf, int value) {
        return nf.format(value);
    }

    /**
     * Formats Integer using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI Integer string
     */
    public static String formatInteger(Integer value, Locale locale) {
        return formatInteger(getIntegerFormatter(locale), value);
    }

    /**
     * Formats Integer using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI Integer string
     */
    public static String formatInteger(final NumberFormat nf, Integer value) {
        if (value == null) {
            return null;
        }
        return formatInt(nf, value.intValue());
    }

    /**
     * Parses long using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed long
     * @throws ParseException when value cannot be parsed
     */
    public static long parseLongValue(String value, Locale locale) throws ParseException {
        return parseLongValue(getIntegerFormatter(locale), value);
    }

    /**
     * Parses long using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed long
     * @throws ParseException when value cannot be parsed
     */
    public static long parseLongValue(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return 0;
        }
        value = value.trim();
        if (value.length() == 0) {
            return 0;
        }
        Number number = parseNumber(nf, value);
        return number.longValue();
    }

    /**
     * Parses Long using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed Long
     * @throws ParseException when value cannot be parsed
     */
    public static Long parseLong(String value, Locale locale) throws ParseException {
        return parseLong(getIntegerFormatter(locale), value);
    }

    /**
     * Parses Long using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed Long
     * @throws ParseException when value cannot be parsed
     */
    public static Long parseLong(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() == 0) {
            return null;
        }
        Number number = parseNumber(nf, value);
        return new Long(number.longValue());
    }

    /**
     * Formats long using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI long string
     */
    public static String formatLong(long value, Locale locale) {
        return formatLong(getIntegerFormatter(locale), value);
    }

    /**
     * Formats long using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI long string
     */
    public static String formatLong(final NumberFormat nf, long value) {
        return nf.format(value);
    }

    /**
     * Formats Long using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI Long string
     */
    public static String formatLong(Long value, Locale locale) {
        return formatLong(getIntegerFormatter(locale), value);
    }

    /**
     * Formats Long using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI Long string
     */
    public static String formatLong(final NumberFormat nf, Long value) {
        if (value == null) {
            return null;
        }
        return formatLong(nf, value.longValue());
    }

    /**
     * Parses double using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed double
     * @throws ParseException when value cannot be parsed
     */
    public static double parseDoubleValue(String value, Locale locale) throws ParseException {
        return parseDoubleValue(getDecimalFormatter(locale), value);
    }

    public static double parseDoubleValueNoExceptions(String value, Locale locale) {
        try {
            return parseDoubleValue(getDecimalFormatter(locale), value);
        } catch (ParseException _) {
            return 0;
        }
    }

    /**
     * Parses double using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed double
     * @throws ParseException when value cannot be parsed
     */
    public static double parseDoubleValue(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return 0;
        }
        value = value.trim();
        if (value.length() == 0) {
            return 0;
        }
        Number number = parseNumber(nf, value);
        return number.doubleValue();
    }

    /**
     * Parses Double using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed Double
     * @throws ParseException when value cannot be parsed
     */
    public static Double parseDouble(String value, Locale locale) throws ParseException {
        return parseDouble(getDecimalFormatter(locale), value);
    }

    /**
     * Parses Double using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed Double
     * @throws ParseException when value cannot be parsed
     */
    public static Double parseDouble(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() == 0) {
            return null;
        }
        Number number = parseNumber(nf, value);
        return new Double(number.doubleValue());
    }

    /**
     * Formats double using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI double string
     */
    public static String formatDouble(double value, Locale locale) {
        return formatDouble(getDecimalFormatter(locale), value);
    }
    
    /**
     * Formats double using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI double string
     */
    public static String formatDouble(final NumberFormat nf, double value) {
        return nf.format(value);
    }

    /**
     * Formats Double using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI Double string
     */
    public static String formatDouble(Double value, Locale locale) {
        return formatDouble(getDecimalFormatter(locale), value);
    }
    
    /**
     * Formats Double using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI Double string
     */
    public static String formatDouble(final NumberFormat nf, Double value) {
        if (value == null) {
            return null;
        }
        return formatDouble(nf, value.doubleValue());
    }

    /**
     * Parses float using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed float
     * @throws ParseException when value cannot be parsed
     */
    public static float parseFloatValue(String value, Locale locale) throws ParseException {
        return parseFloatValue(getDecimalFormatter(locale), value);
    }

    /**
     * Parses float using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed float
     * @throws ParseException when value cannot be parsed
     */
    public static float parseFloatValue(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return 0;
        }
        value = value.trim();
        if (value.length() == 0) {
            return 0;
        }
        Number number = parseNumber(nf, value);
        return number.floatValue();
    }

    /**
     * Parses Float using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed Float
     * @throws ParseException when value cannot be parsed
     */
    public static Float parseFloat(String value, Locale locale) throws ParseException {
        return parseFloat(getDecimalFormatter(locale), value);
    }

    /**
     * Parses Float using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed Float
     * @throws ParseException when value cannot be parsed
     */
    public static Float parseFloat(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() == 0) {
            return null;
        }
        Number number = parseNumber(nf, value);
        return new Float(number.floatValue());
    }

    /**
     * Formats float using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI float string
     */
    public static String formatFloat(float value, Locale locale) {
        return formatFloat(getDecimalFormatter(locale), value);
    }
    
    /**
     * Formats float using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI float string
     */
    public static String formatFloat(final NumberFormat nf, float value) {
        return nf.format(value);
    }

    /**
     * Formats Float using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI Float string
     */
    public static String formatFloat(Float value, Locale locale) {
        return formatFloat(getDecimalFormatter(locale), value);
    }

    /**
     * Formats Float using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI Float string
     */
    public static String formatFloat(final NumberFormat nf, Float value) {
        if (value == null) {
            return null;
        }
        return formatFloat(nf, value.floatValue());
    }

    /**
     * Parses BigDecimal using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed BigDecimal
     * @throws ParseException when value cannot be parsed
     */
    public static BigDecimal parseBigDecimal(String value, Locale locale) throws ParseException {
        return parseBigDecimal(getDecimalFormatter(locale), value);
    }

    /**
     * Parses BigDecimal using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed BigDecimal
     * @throws ParseException when value cannot be parsed
     */
    public static BigDecimal parseBigDecimal(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() == 0) {
            return null;
        }
        Number number = parseNumber(nf, value);
        return new BigDecimal(String.valueOf(number.doubleValue()));
    }

    /**
     * Formats BigDecimal using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI BigDecimal string
     */
    public static String formatBigDecimal(BigDecimal value, Locale locale) {
        return formatBigDecimal(getDecimalFormatter(locale), value);
    }

    /**
     * Formats BigDecimal using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI BigDecimal string
     */
    public static String formatBigDecimal(final NumberFormat nf, BigDecimal value) {
        if (value == null) {
            return null;
        }
        return nf.format(value);
    }

    /**
     * Parses BigInteger using locale settings.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param value to be parsed
     * @param locale to be used
     * @return parsed BigInteger
     * @throws ParseException when value cannot be parsed
     */
    public static BigInteger parseBigInteger(String value, Locale locale) throws ParseException {
        return parseBigInteger(getDecimalFormatter(locale), value);
    }

    /**
     * Parses BigInteger using specified formatter.
     * This method should be used for parsing data coming from user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return parsed BigInteger
     * @throws ParseException when value cannot be parsed
     */
    public static BigInteger parseBigInteger(final NumberFormat nf, String value) throws ParseException {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() == 0) {
            return null;
        }
        Number number = parseNumber(nf, value);
        return new BigInteger(Long.toString(number.longValue()));
    }

    /**
     * Formats BigInteger using locale settings. 
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param value to be formatted
     * @param locale that will be used during formatting
     * @return formatted GUI BigInteger string
     */
    public static String formatBigInteger(BigInteger value, Locale locale) {
        return formatBigInteger(getIntegerFormatter(locale), value);
    }

    /**
     * Formats BigInteger using specified formatter.
     * This method should be used for formatting data that will be displayed on user interface (like web GUI).
     * 
     * @param nf number formatter to be used
     * @param value to be parsed
     * @return formatted GUI BigInteger string
     */
    public static String formatBigInteger(final NumberFormat nf, BigInteger value) {
        if (value == null) {
            return null;
        }
        return nf.format(value);
    }
}
