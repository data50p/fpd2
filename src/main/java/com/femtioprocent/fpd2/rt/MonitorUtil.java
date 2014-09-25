package com.femtioprocent.fpd2.rt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.femtioprocent.fpd2.util.MilliTimer;
import com.femtioprocent.fpd2.util.TimerProfile;

/**
 * Keep track of times, string data. Used for monitoring performances.
 * 
 * Typical usage, in this case measuring the time
 <pre>
 public FooResult foo() {
    MonitorItem monitorItem = MonitorUtil.registerMilliTimerMonitorItem("foo", "Time to do some stuff in function foo()");
    try {
        // do whatever is intended in foo();
        return new FooResult();
    } finally {
        monitorItem.register();
    }
 }
 
 public void printAboutFoo() {
     MonitorItem monitorItem = MonitorUtil.get("foo");
     System.out.println("Report for calling foo(): " + monitorItem.getReport().toString());
 }
 </pre>
 * 
 * MonitorUtil is a wrapper class. Everything needed are here.
 */
public class MonitorUtil {
    /**
     * singleton
     */
    private static MonitorUtil defaultMonitorUtil = new MonitorUtil();
    
    /**
     * All known monitored items
     */
    private HashMap<String, MonitorItem> monitorItems = new HashMap<String, MonitorItem>();
    
    /**
     * Global flag to turn on or off the monitor feature.
     */
    private static boolean OFF = false;
    
    /**
     * A special MonitorItem used when monitoring is off.
     */
    private static MonitorItem OFF_ITEM = new MonitorItem("MONITOR_OFF", "This feature is off", "");
    
    /**
     * A report of current status of a MonitorItem. No reference back to MonitorItem, only String values.
     */
    public static class Report {
    	private String name;
		private String value;
    	private String minMax;
    	private String latest;
    	private String count;
    	private String unit;
    	private String time = "";
    	
		Report() {		
    	}
    	
    	public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

    	public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

    	public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getMinMax() {
			return minMax;
		}

		public void setMinMax(String minMax) {
			this.minMax = minMax;
		}

		public String getLatest() {
			return latest;
		}

		public void setLatest(String latest) {
			this.latest = latest;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

        public String toString() {
        	return "~" + value + " [" + minMax + "] =" + latest + " " + unit + " #" + count;
        }

    }
    
    /**
     * Smple MonitorItem for a single double value. Latest value, min, max, or average value is stored.
     */
    public static class MonitorItem implements Comparable<MonitorItem> {
        protected String name;
        protected String description;
        protected String unit = "";
        
        private double accumulated;
        protected double latest;
        protected double min = Double.MAX_VALUE;
        protected double max = Double.MIN_VALUE;
        protected int count;
        protected long time; // when latest value was registered
        
        public MonitorItem(String name) {
            this.name = name;
            this.description = name;
        }

        public MonitorItem(String name, String description, String unit) {
            this.name = name;
            this.description = description;
            this.unit = unit;
        }

        public void clear() {
            accumulated = 0;
            latest = 0;
            count = 0;
            min = Double.MAX_VALUE;
            max = Double.MIN_VALUE;
        }

        public double getAverage() {
            return count == 0 ? 0 : accumulated / count;
        }

        public double getLatest() {
            return latest;
        }

        public double getMin() {
            return min;
        }
        
        public double getMax() {
            return max;
        }
        
        public synchronized Report getReport() {
        	Report report = new Report();
        	report.setName(name);
        	report.setValue("" + getReportValue());
        	report.setMinMax(getReportValueMinMax());
        	report.setUnit(getReportUnit());
        	report.setLatest(getReportLatest());
        	report.setCount(getReportCount());
        	report.setTime(new Date(time).toString());
        	return report;
        }

        public String getReportValueMinMax() {
            return "" + getMin() + ", " + getMax() + "";
        }

        public String getReportValue() {
            return "" + getAverage();
        }

        public String getReportLatest() {
            return "" + getLatest();
        }

        public String getReportCount() {
            return "" + count;
        }

        public String getReportUnit() {
            return unit;
        }
        
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description the description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return the accumulated
         */
        public double getAccumulated() {
            return accumulated;
        }

        /**
         * @param accumulated the accumulated to set
         */
        public void setAccumulated(double accumulated) {
            this.accumulated = accumulated;
        }

        /**
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * @param count the count to set
         */
        public void setCount(int count) {
            this.count = count;
        }

        /**
         * @return the unit
         */
        public String getUnit() {
            return unit;
        }

        /**
         * @param unit the unit to set
         */
        public void setUnit(String unit) {
            this.unit = unit;
        }

        public synchronized void register(double value) {
            latest = value;
            min = Math.min(value, min);
            max = Math.max(value, max);
            accumulated += value;
            count++;
            time = System.currentTimeMillis();
        }

        public synchronized void register(int value) {
            latest = value;
            min = Math.min(value, min);
            max = Math.max(value, max);
            accumulated += value;
            count++;
            time = System.currentTimeMillis();
        }
        
        public void register(String value) {
            double dvalue = 0;
            try {
                dvalue = Double.parseDouble(value);
            } catch (Exception _) {
                // ignored
            }
            register(dvalue);
        }

        public void register() {
        }
        
        public void reActivate() {
        }
        
        public String toString() {
            return "" + name + ' ' + getReport().toString();
        }

		public int compareTo(MonitorItem o) {
			return this.getName().compareTo(o.getName());
		}
    }
    
    public static class MilliTimerMonitorItem extends MonitorItem {
        private ThreadLocal<MilliTimer> milliTimer = new ThreadLocal<MilliTimer>();
     
        public TimerProfile timerProfile = null;
        
        public MilliTimerMonitorItem(String name, String description) {
        	super(name, description, "ms");
            milliTimer.set(new MilliTimer());
        }
        
        public void clear() {
        	super.clear();
        	reActivate();
        }

        public void register() {
            double value = milliTimer.get().getValue();
            super.register(value);
            if ( timerProfile != null )
                timerProfile.add(value);
        }

        public void reActivate() {
            super.reActivate();
            milliTimer.set(new MilliTimer());
        }
        
        public Report getReport() {
        	Report r = super.getReport();
        	if ( timerProfile != null ) {     	    
        	    r.setMinMax(getReportValueMinMax() + ' ' + timerProfile.toString());
            }
            return r;
        }
    }

    /**
     * ProfileMonitorItem has a arrays of counters, each covering a numeric range. Increase
     * counter when the registred value fall into corresponding range. There is one special
     * counter for values lesser than the first specified range (profile).
     */
    public static class ProfileMonitorItem extends MonitorItem {
        private int[] profileValues;
        private int[] profileCounter;
        private int minCounter;
        
        public ProfileMonitorItem(String name, String description, String unit, int[] profile) {
            super(name, description, unit);
            this.profileValues = profile;
            
            init();
        }
        
        private void init() {
            profileCounter = new int[profileValues.length];
            minCounter = 0;
            max = 0;
        }
        
        public void clear() {
        	super.clear();
        	init();
        }
        
        public synchronized void register(int value) {
            try {
                latest = value;
                if ( value < profileValues[0] )
                    minCounter++;
                else if ( value >= profileValues[profileValues.length - 1] )
                    profileCounter[profileValues.length - 1]++;
                else {
                    for(int i = 0; i < profileValues.length; i++) {
                        if ( value >= profileValues[i] ) {
                            continue;
                        }
                        profileCounter[i-1]++;
                        break;
                    }
                }
            } finally {
                    count++;
                    time = System.currentTimeMillis();
            }
        }

        public void reActivate() {
            super.reActivate();
        }

        public synchronized String format() {
            StringBuilder sb = new StringBuilder();
            sb.append("[min:");
            sb.append("" + minCounter);
            for(int i = 0; i < profileValues.length; i++) {
                sb.append(", ");
                sb.append(profileValues[i]);
                sb.append(':');
                sb.append(profileCounter[i]);
            }
            sb.append("]");
            return sb.toString();
        }

        public String getReport(String prefix) {
            return "" + ": " + (prefix == null ? "" : prefix + ' ') + format() + " (" + getLatest() + ") " + unit + " [" + count + "]";
        }
        
        public String getReportValue() {
            return format();
        }

        public String getReportValueMinMax() {
            return "";
        }        
    }

    public static class StringProfileMonitorItem extends MonitorItem {
        HashMap<String, int[]> map;
        protected String latest = "";
        
        public StringProfileMonitorItem(String name, String description, String unit) {
            super(name, description, unit);
            
            init();
        }
        
        private void init() {
            map = new HashMap<String, int[]>();
        }

        public void clear() {
        	super.clear();
        	init();
        }

        public synchronized void register(double value) {
            register("" + value);
        }

        public synchronized void register(int value) {
            register("" + value);
        }

        public void register() {
            register("_");
        }

        public synchronized void register(String value) {
            try {
                latest = value;
                int [] ia = map.get(value);
                if ( ia == null ) {
                    map.put(value, new int[] {1});
                } else {
                    ia[0]++;
                }
            } finally {
                    count++;
                    time = System.currentTimeMillis();
            }
        }

        public void reActivate() {
            super.reActivate();
        }

        public synchronized String format() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(String key : map.keySet()) {
                if ( sb.length() > 1 )
                    sb.append(", ");
                sb.append(key);
                sb.append(':');
                sb.append(map.get(key)[0]);
            }
            sb.append("]");
            return sb.toString();
        }
        
        public String getValue() {
        	return format();
        }
        
        public String getReportValue() {
        	return getValue();
        }

        public String getReportValueMinMax() {
            return "";
        }
    }

    /**
     * The ring of a fixed size can have that many strings attached. When a new String is registred then the
     * oldest in the ring get disposed.
     */
    public static class StringRingMonitorItem extends MonitorItem {
        LinkedList<String> list;
        protected String latest = "";
        int maxSize = 10;
        
        public StringRingMonitorItem(String name, String description, int maxSize) {
            super(name, description, "_");
            this.maxSize = maxSize;
            init();
        }
        
        private void init() {
            list = new LinkedList<String>();
        }

        public void clear() {
            super.clear();
            init();
        }
        
        public synchronized void register(double value) {
            register("" + value);
        }

        public synchronized void register(int value) {
            register("" + value);
        }

        public void register() {
            register("" + new Date());
        }

        public synchronized void register(String value) {
            try {
                latest = value;
                if ( list.size() >= maxSize )
                    list.removeLast();
                list.add(0, value);
            } finally {
                count++;
                time = System.currentTimeMillis();
            }
        }

        public void reActivate() {
            super.reActivate();
        }

        public synchronized String format() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(String s : list) {
                if ( sb.length() > 1 )
                    sb.append(", ");
                sb.append(s);
            }
            sb.append("]");
            return sb.toString();
        }
        
        public String getValue() {
            return format();
        }
        
        public String getReportValue() {
            return getValue();
        }

        public String getReportValueMinMax() {
            return "";
        }
    }
    
    public static MonitorUtil getDefaultMonitorUtil() {
        return defaultMonitorUtil;
    }
    
    public static synchronized MonitorItem registerMilliTimerMonitorItem(String name, String description) {
    	if ( OFF ) {
    		return OFF_ITEM;
    	}
        MonitorItem nmi = getDefaultMonitorUtil().monitorItems.get(name);
        if ( nmi != null ) {
            if ( ! nmi.getName().equals(nmi.getDescription()) )
                nmi.setDescription(description);
            // reset the start time.
            nmi.reActivate();
            return nmi;
        }
    	MonitorItem mi = registerMonitorItem(new MilliTimerMonitorItem(name, description));
        return mi;
    }

    public static synchronized MonitorItem registerMonitorItem(MonitorItem mi) {
    	if ( OFF ) {
    		return OFF_ITEM;
    	}
        MonitorItem nmi = getDefaultMonitorUtil().monitorItems.get(mi.name);
        if ( nmi != null ) {
            if ( ! nmi.getDescription().equals(mi.getDescription()) )
                nmi.setDescription(mi.getDescription());
            nmi.reActivate();
            return nmi;
        }
        getDefaultMonitorUtil().monitorItems.put(mi.getName(), mi);
        mi.reActivate();
        return mi;
    }
        
    public static synchronized MonitorItem getMonitorItem(String name) {
        MonitorItem mi = getDefaultMonitorUtil().monitorItems.get(name);
        if ( mi == null ) {
            mi = new MonitorItem(name);
            getDefaultMonitorUtil().monitorItems.put(name, mi);
        }
        return mi;
    }

    public static synchronized List<MonitorItem> getMonitorItemsList(String pattern) {
    	List<MonitorItem> list = new ArrayList<MonitorItem>();
    	
    	for(Map.Entry<String, MonitorItem> entry : getDefaultMonitorUtil().monitorItems.entrySet()) {
    		String key = entry.getKey();
    		MonitorItem mi = entry.getValue();
    		list.add(mi);
        }
        Collections.sort(list);
        return list;
    }

    public static synchronized void clearAll(String pattern) {
    	for(Map.Entry<String, MonitorItem> entry : getDefaultMonitorUtil().monitorItems.entrySet()) {
    		String key = entry.getKey();
    		MonitorItem mi = entry.getValue();
    		mi.clear();
        }
    }

    public static synchronized MonitorItem deleteMonitorItem(String name) {
        MonitorItem mi = getDefaultMonitorUtil().monitorItems.remove(name);
        return mi;
    }
}
