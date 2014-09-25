package com.femtioprocent.fpd2.cache;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import com.femtioprocent.fpd2.util.MilliTimer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cache an instance of a class <T>. There is at least one CacheDataManager for each class and each manager
 * can cache several instances identified by an id. There is a timeout value when the cached data is considered
 * old and need to be refilled. When retrieving a value from the cache via the get() method a CachedDataFiller
 * is supplied which will be called if the cached data is in need of refilling.
 * 
 * Typical usage:
<pre>
    static class MyData {
		String foo;
		String bar;
	}
    private static CachedDataManager<MyData> cacheProgramUnitDataList = new CachedDataManager<MyData>();
    private MyData getMyDataById(final String id) {
        MyData cacheddata = cacheProgramUnitDataList.get(id, new CachedDataFiller<Mydata>() {
        	// this function is called when there is no data in cache or it is expired (5 minutes)
        	public MyData fill() {
        		MyData data = new MyData(id);
        		return data;
        	}
        }, 5 * 60 * 1000);
        return cacheddata;
    }    
 </pre>
 *   
 * @author lnordbe2
 *
 * @param <T>
 */
public class CachedDataManager<T> {
    /**
     * Set to true to turn on the cache. If it is off, then the get() method will always call
     * the filler method to fill the data and nothing is put into the cache.
     */
	public static boolean CACHE_ON = true;
	
    /**
     * Indicates that item will forcibly be filled. Used as timeout value.
     */
	public static final long TIMEOUT_FILL_CACHE = 0L;
    /**
     * Indicates that item will not be put into cache. Used as timeout value.
     */
    public static final long TIMEOUT_SKIP_CACHE = -1;
	
	/**
	 * Standard logging
	 */
    private static final Logger logger = LoggerFactory.getLogger(CachedDataManager.class);
	
	/**
	 * All known Cached Data Managers. Used for disposing old content.
	 */
	private static List<CachedDataManager> globalList = Collections.synchronizedList(new ArrayList<CachedDataManager>());
	
	private static Object lock = new Object();
	
	/**
	 * An id for a CachedDataManager.
	 */
	private String cdataTagId;
	
	/**
	 * This is the map where all cached data is stored. Each data is identified by an String id.
	 */
	private Map<String, CachedDataItem<T>> cache = Collections.synchronizedMap(new HashMap<String, CachedDataItem<T>>());
	
	/**
	 * The cached data is decorated with timeout values so that we know when it is old and need to be refilled.
	 * @param <T>
	 */
	public static class CachedDataItem<T> {
	    /**
	     * The cached data.
	     */
    	public T t;
    	
    	/**
    	 * The fill time when data was filled.
    	 */
    	public MilliTimer mt;
    	
    	/**
    	 * The default or intended timeout in ms. This indicates who old the data can be untill refill is needed.
    	 * Note that there are dispose functions having a timeout value that is used instead of this intended. 
    	 */
    	public long intendedTimeout = 0;
    	
    	/**
    	 * The time in nanoseconds to fill the data
    	 */
    	public double fillTime = 0;
    	
    	public int counter = 0;
    	
    	/**
    	 * Create an instance of cached data and intended timeout.
    	 * @param t
    	 * @param intendedTimeout
    	 */
    	public CachedDataItem(T t, long intendedTimeout) {
    		this.t = t;
    		this.intendedTimeout = intendedTimeout;
    		mt = new MilliTimer();
		}
    	
    	/**
    	 * Is data expired according to intended timeout.
    	 * Status contains expire status in form:<br/>
    	 * "<isExpired> <current timout value (how old is cached data)> <expire value (when is cached data expired)>
    	 * @param status String array where status is stored. If status == null do not store anything
    	 * @return
    	 */
        public boolean isExpired(String[] status) {
            return isExpired(intendedTimeout, status);
        }

        /**
         * check timeout status. Check againts timeoutValueMilliSeconds or if this is null chech against intendedTimeout.
         * @param timeoutValueMilliSeconds
         * @return
         */
        public boolean isExpired(Long timeoutValueMilliSeconds, String[] status) {
            return CACHE_ON == false || mt.isExpired(timeoutValueMilliSeconds == null ? intendedTimeout : timeoutValueMilliSeconds, status);
        }
    	
        /**
         * Get the cached data
         * @return
         */
    	T get() {
    		return t;
    	}
    	
        /**
         * Print expired info and type of cached data
         * {@inheritDoc}
         */
        public String toString() {
            try {
                String[] status = new String[1];
                boolean expired = isExpired(status);
                if ( t instanceof ArrayList ) {
                    if ( ((ArrayList)t).size() > 0 ) {
                        Object o = ((ArrayList)t).get(0);
                        return "" + counter + " " + fillTime + ' ' + status[0] + " ArrayList " + o.getClass().getSimpleName();
                    } else
                        return "" + counter + " " + fillTime + ' ' + status[0] + ' ' + t.getClass().getSimpleName();
                }
                else
                    return "" + counter + " " + fillTime + ' ' + status[0] + ' ' + t.getClass().getSimpleName();
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "_ _ _";
            }
        }

        /**
         * Print expired info and type of cached data
         * {@inheritDoc}
         */
        public String toStringHTML() {
            try {
                String[] status = new String[1];
                boolean expired = isExpired(status);
                String ft = String.format("%8.3f", fillTime);
                if ( t instanceof ArrayList ) {
                    if ( ((ArrayList)t).size() > 0 ) {
                        Object o = ((ArrayList)t).get(0);
                        return "<td>" + counter + "</td><td>" + ft + "</td><td>" + status[0].replace(" ", "</td><td>") + "</td><td>" + " ArrayList " + o.getClass().getSimpleName() + "</td>";
                    } else
                        return "<td>" + counter + "</td><td>" + ft + "</td><td>" + status[0].replace(" ", "</td><td>") + "</td><td>" + t.getClass().getSimpleName() + "</td>";
                }
                else
                    return "<td>" + counter + "</td><td>" + ft + "</td><td>" + status[0].replace(" ", "</td><td>") + "</td><td>" + t.getClass().getSimpleName() + "</td>";
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "_ _ _";
            }
        }
    }

	/**
	 * Create a new CachedDataManager identified by cdataTagId
	 * @param cdataTagId for identity
	 */
	private CachedDataManager(String cdataTagId) {
		this.cdataTagId = cdataTagId;
	}

	public static <T> CachedDataManager<T> create(String cdataTagId) {
	    CachedDataManager<T> cdm = new CachedDataManager<T>(cdataTagId);
        //logger.info("create: (sync... gL) " + cdataTagId);
        globalList.add(cdm);
        //logger.info("create: (...sync gL) " + cdataTagId);
	    return cdm;
    }
	
	/**
	 * How many cached objects is stored in cache
	 * @return
	 */
	public int size() {
		return cache.size();
	}

	/**
	 * Check if tagExp is matched whith the cdataTagId for this CacheddataManager
	 * @param tagExp
	 * @param defaultValue
	 * @return
	 */
	private boolean matchTag(String tagExp, boolean defaultValue) {
		if ( tagExp == null )
			return defaultValue;
		Pattern pat = Pattern.compile(tagExp);
		Matcher mat = pat.matcher(cdataTagId);
		//logger.info("matchTag: " + tagExp + ' ' + cdataTagId + ' ' + mat.matches());
		return mat.matches();
	}

	/**
	 * Get an cached instance if it exist or not expired. Call CachedDataFiller.fill() if no instance exist and
	 * save it in cache.
	 *  
	 * @param key
	 * @param f
	 * @param timeout in ms. TIMEOUT_SKIP_CACHE is handled
	 * @return
	 */
	public T get(String key, CachedDataFiller<T> f, long timeoutValue) {
	    if ( timeoutValue != TIMEOUT_SKIP_CACHE ) 
	        disposeAllExpired();

	    try {
	        CachedDataItem<T> cdataItem = timeoutValue == TIMEOUT_SKIP_CACHE ? null : cache.get(key);
	        // get cached data or if this is old or absent, fill it from CachedDataFiller.fill()
	        String[] status = new String[1];
	        if ( CACHE_ON == false || cdataItem == null || cdataItem.isExpired(timeoutValue, status) ) {
                logger.info(" :-: >--> fill...: " + cdataTagId + " [" + key + "] " + status[0]);
                MilliTimer mt = new MilliTimer();
	            T t = f.fill();
	            double fillTime = mt.getValue();
	            if ( timeoutValue != TIMEOUT_SKIP_CACHE ) {
	                cdataItem = new CachedDataItem<T>(t, timeoutValue);
	                cdataItem.fillTime = fillTime;
	                cache.put(key, cdataItem);
                    logger.info(" :-: <--< filled: " + cdataTagId + "[" + key + "] " + fillTime + " ms");
                    return t;
	            } else {
	                cache.remove(key);
	                logger.info(" :-: <--< filled: " + cdataTagId + "[" + key + "] " + fillTime + " ms");
	                return t;
	            }
	        }
	        cdataItem.counter++;
            logger.info(" :-: <--< get: " + cdataTagId + "[" + key + "] " + cdataItem.counter);
	        return cdataItem.get();        
        } finally {
        }
	}
	
    /**
     * Remove items that has expired from cache. Use the timeoutValue set to 0 to remove all.
     * @param timeoutValue
     */
    public void disposeExpired(Long timeoutValue) {
        disposeExpiredTag(null, timeoutValue);
    }

    /**
     * Remove items that has expired from cache. Use the intended timeout value.
     * @param timeoutValue
     */
    public void disposeExpired() {
        disposeExpiredTag(null, null);
    }

	/**
	 * Remove items that has expired from cache. Use the timeoutValue set to 0 to remove all.
	 * @param cdataTagIdRegExp select which cached data to check
	 * @param timeoutValue
	 */
	public void disposeExpiredTag(String cdataTagIdRegExp, Long timeoutValue) {
		List<Object> expList = new ArrayList<Object>(); // key of items to remove
		
		Pattern pat = cdataTagIdRegExp == null ? null : Pattern.compile(cdataTagIdRegExp);

        String[] status = new String[1];

        //logger.info(">>>> disposeET: (sync...) this " + hashCode());
		for(Entry<String, CachedDataItem<T>> ent : cache.entrySet()) {
			if ( ent.getValue().isExpired(timeoutValue, status) && (pat == null || pat.matcher(ent.getKey()).matches()) ) {
				expList.add(ent.getKey());
			} else {
                //logger.info("             : not disposed " + cdataTagId + " (" + cdataTagIdRegExp + ") " + ent.getKey() + ' ' + status[0]);
			}
		}
		for(Object key : expList) {
			cache.remove(key);
            logger.info(" :-: disposed " + cdataTagId + " (" + cdataTagIdRegExp + ") " + key + ' ' + status[0]);
		}        
        //logger.info("<<<< disposeET: (...sync) this " + hashCode());
	}
	
	/**
	 * Preventing to many call to dispose function
	 */
    private static AtomicLong disposeAllExpiredDoneTime = new AtomicLong(0);
    
    /**
     * Count how many time dispose function skipped
     */
    private static int disposeAllExpiredDoneTimeSkipped = 0;    
    
    /**
     * Remove all items that has expired from all caches.
     */
    public static void disposeAllExpired() {
        long sinceLastTime = System.nanoTime() - disposeAllExpiredDoneTime.get();
        if ( sinceLastTime < 400000000L ) { // max one call per 0.4 second
            disposeAllExpiredDoneTimeSkipped++;
            return;
        }
        // dispose all cache, all items and by intended timeout
        disposeAllExpiredDoneTime.set(System.nanoTime());
        disposeAllExpired(null, null, null);
        logger.info("status: " + disposeAllExpiredDoneTimeSkipped);
    }

    /**
     * Remove all items that has expired from cache and match regular expression tagExp.
     * Use the intended timeoutValue.
     * @param cacheRegExp select which cache to check
     */
    public static void disposeAllExpired(String cacheRegExp) {
        disposeAllExpired(cacheRegExp, null, null);
    }

    /**
     * Remove all items that has expired from cache and match regular expression tagExp.
     * Use the timeoutValue set to 0 to remove all.
     * @param cacheRegExp select which cache to check
     * @param timeoutValue
     */
    public static void disposeAllExpired(String cacheRegExp, Long timeoutValue) {
        disposeAllExpired(cacheRegExp, null, timeoutValue);
    }
	
	/**
	 * Remove all items that has expired from cache and match regular expression tagExp.
     * Use the timeoutValue set to 0 to remove all.
     * 
	 * @param cacheRegExp select which cache item to handle (null = select all) 
	 * @param cachedDataManagerTagExp select which cached data to check for expiration (null = select all)
	 * @param timeoutValue (null = use intended timeout, 0 = expire now)
	 */
	public static void disposeAllExpired(String cacheRegExp, String cachedDataManagerTagExp, Long timeoutValue) {
	    if ( CACHE_ON == false )
	        return;
	    List<CachedDataManager> globalListCopy = new ArrayList<CachedDataManager>();

	    //logger.info(">>>> disposeAll: (sync...) gL");
	    globalListCopy.addAll(globalList);               
        //logger.info("<<<< disposeAll: (....sync) gL");

	    for(CachedDataManager cdm : globalListCopy) {
	        if ( cdm.matchTag(cacheRegExp, true) ) {
	            int beforeSize = cdm.cache.size();
	            //logger.info("dispose check: matched " + cacheRegExp + ' ' + cdm.cdataTagId + ' ' + beforeSize);
	            cdm.disposeExpiredTag(cachedDataManagerTagExp, timeoutValue);
	            int afterSize = cdm.cache.size();
//                logger.info("dispose stat: " + cacheRegExp + ' ' + cdm.cdataTagId + ' ' + beforeSize + " >size> " + afterSize);
	        } else {
	            //logger.info("dispose check: no match " + cacheRegExp + ' ' + cdm.cdataTagId + ' ' + cdm.cache.size());
	        }
	    }
	    //logger.info("dispose check: globalList.size " + globalList.size());
	}

	/**
	 * Create one single map of all CachedDataItems. Collect all CachedDataManagers and retrieve all CachedDataItems for each.
	 * Replace the key for each Item to have the name <CachedDataManager.cdataTagId>[<CachedDataManager.cache.<key>>].
	 * If this key is used multiple times then append the uniq string ~<number>. 
	 * 
	 * @return
	 */
    public static Map<String, CachedDataItem> getAll() {
        SortedMap<String, CachedDataItem> map = new TreeMap<String, CachedDataItem>();
        int unique = 0;
        logger.info(">>>> getAll: (sync...) gL");
            for(CachedDataManager cdm : globalList) {
                Map<String, CachedDataItem> cm = cdm.cache;
                for(String key : cm.keySet()) {
                    String modifiedKey = cdm.cdataTagId + '[' + key + ']';
                    if ( map.containsKey(modifiedKey))
                        modifiedKey += "~" + unique++;
                    map.put(modifiedKey, cm.get(key));
                }
            }
        logger.info("<<<< getAll: (...sync) gL");
        disposeAllExpired();
        return map;
    }
}
