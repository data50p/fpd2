package com.femtioprocent.fpd2.config;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import com.femtioprocent.fpd2.rt.PreferenceUtil;
import java.util.logging.Logger;

/**
 * Save values and remember them between deployes
 */
public class RuntimeConfig {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeConfig.class);
    public static final boolean TRUE_TMP = true;

    /**
     * Append this string on a named key to add an integer arguemn connected to it.
     */
    public static enum RuntimeSubItem {
        INTARG_1("intArg-1");
        
        private String name;
        
        private RuntimeSubItem(String name) {
            this.name = name;
        }

        /**
         * @return the name
         */
        String getName() {
            return name;
        }
    }
    
    public static enum RuntimeItem {
        Null("", "Empty Item", false, false),
        EmailException("EmailException", "Send Email Exception", false, false),
        LiveStatus("LiveStatus", "Live Status", true, false),
        ConAttr("ConAttr", "Connection Attribute", false, false),
        McmFeature("McmFeature", "MCM Notification Feature", false, false),
        MilestoneLabel("MilestoneLabel", "Milestone Label", false, false),
        LogAjaxRequest("LogAjaxRequest", "Log Ajax Request", false, true),
        ViewReportVitalFew("ViewReportVitalFew", "Vital Few report in rag-rug", false, false),
        SuppressKPI("SuppressKPI", "Suppress KPI", false, false);

        private String name;
        private String description;
        private boolean defaultValue;
        private boolean numeric;
        
        private RuntimeItem(String name, String description, boolean defaultValue, boolean numeric) {
            this.name = name;
            this.description = description;
            this.defaultValue = defaultValue;
            this.numeric = numeric;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return the defaultValue
         */
        public boolean isDefaultValue() {
            return defaultValue;
        }

        /**
         * @return the numeric
         */
        public boolean isNumeric() {
            return numeric;
        }
    }
    
    static {
        for(RuntimeItem ruit : RuntimeItem.values()) {
            try {
                boolean currentValue = is(ruit.getName(), ruit.isDefaultValue());
                logger.info("RuntimeConfig: loaded " + ruit.getName() + ' ' + ruit.isDefaultValue() + " -> " + currentValue);
            } catch (Exception ex) {
                logger.info("RuntimeConfig: load fail " + ruit.getName() + ' ' + ex);
            }
        }
    }
    
    private static boolean is(String name, boolean def) {
        PreferenceUtil pu = new PreferenceUtil(RuntimeConfig.class);
        Boolean b = (Boolean) pu.load(name.toLowerCase(), new Boolean(def));
        return b == null ? false : b.booleanValue();
    }

    public static boolean is(RuntimeItem ruit) {
        return is(ruit, null); 
    }

    public static void set(RuntimeItem ruit, boolean value) {
        set(ruit, null, value);
    }

    public static boolean is(RuntimeItem ruit, RuntimeSubItem sub) {
        String name = mkName(ruit, sub);
        PreferenceUtil pu = new PreferenceUtil(RuntimeConfig.class);
        Boolean b = (Boolean) pu.load(name.toLowerCase(), null);
        return b == null ? false : b.booleanValue();
    }

    public static int getInt(RuntimeItem ruit, RuntimeSubItem sub, int def) {
        String name = mkName(ruit, sub);
        PreferenceUtil pu = new PreferenceUtil(RuntimeConfig.class);
        Integer value = (Integer) pu.load(name.toLowerCase(), new Integer(def));
        return value == null ? def : value.intValue();
    }

    public static String get(RuntimeItem ruit, RuntimeSubItem sub, String def) {
        String name = mkName(ruit, sub);
        PreferenceUtil pu = new PreferenceUtil(RuntimeConfig.class);
        String value = (String) pu.load(name.toLowerCase(), def);
        return value == null ? def : value.toString();
    }

    public static void set(RuntimeItem ruit, RuntimeSubItem sub, boolean value) {
        String name = mkName(ruit, sub);
        PreferenceUtil pu = new PreferenceUtil(RuntimeConfig.class);
        pu.save(name.toLowerCase(), new Boolean(value));
        logger.info("set " + name + ' ' + value);
    }

    public static void setInt(RuntimeItem ruit, RuntimeSubItem sub, int value) {
        String name = mkName(ruit, sub);
        PreferenceUtil pu = new PreferenceUtil(RuntimeConfig.class);
        pu.save(name.toLowerCase(), new Integer(value));
        logger.info("set " + name + ' ' + value);
    }

    public static void set(RuntimeItem ruit, RuntimeSubItem sub, String value) {
        String name = mkName(ruit, sub);
        PreferenceUtil pu = new PreferenceUtil(RuntimeConfig.class);
        pu.save(name.toLowerCase(), value);
        logger.info("set " + name + ' ' + value);
    }

    private static String mkName(RuntimeItem ruit, RuntimeSubItem sub) {
        return ruit.getName() + (sub == null ? "" : "-" + sub.getName());
    }
}
