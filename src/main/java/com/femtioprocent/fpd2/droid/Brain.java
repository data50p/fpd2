package com.femtioprocent.fpd2.droid;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import com.femtioprocent.fpd2.droid.DKey.Name;
import com.femtioprocent.fpd2.util.MilliTimer;
import com.femtioprocent.fpd2.util.ObjectUtil;
import com.femtioprocent.fpd2.util.ToString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * The Brain is a storage mechanism for a DroidWithBrain. The values are stored with access from a key (like HashMap).
 * The keys value is construkted from the Class owner of the Droid, the instanceId, and subkey. The whole Brain memory (if elephantMemory)
 * is stored in the database using the generic pdd_key_value table. The key in database (Name) is constructed from the elephantMemory class and
 * the name "cortex".
 * 
 * The database storage is a bit optimal since only changes are stored each in a own row (using pdd_key_value:operation Map.put).
 * When there is to much entries all of them is collected and stored in one new row (using pdd_key_value:operation Map.set).
 * 
 * 
 */
public abstract class Brain {
    private static final Logger logger = LoggerFactory.getLogger(Brain.class);

    /**
     * Return true if value was anoter than previous,
     * {@inheritDoc}
     */
    abstract boolean put(String key, Object value);

    abstract Object get(String key);

    private static Brain simpleBrain = null;
    private static Brain elephantBrain = null;

    /** Remembers only for one redeploy cycles. Store the data in memory only.
     * 
     * @return the Brain instance
     */
    public static synchronized Brain getSimpleBrain() {
        if (simpleBrain == null)
            simpleBrain = new Brain.Simple();
        return simpleBrain;
    }

    /** Remembers between redeploy cycles. Store the data in a key/value entry in the database
     * 
     * @return the Brain instance
     */
    public static synchronized Brain getElephantBrain() {
        if (elephantBrain == null)
            elephantBrain = new Brain.Elephant();
        return elephantBrain;
    }

    /**
     * A simple Brain that only remebered things while the same JVM instance is running.
     */
    private static class Simple extends Brain {
        protected HashMap<String, Object> cortex = new HashMap<String, Object>();

        /**
         * Return true if value was updated (different from previous)
         * {@inheritDoc}
         */
        synchronized boolean put(String key, Object value) {
            try {
                boolean itWasThereAlreadyAndSameValue = cortex.containsKey(key) && value != null && cortex.get(key).equals(value);
                boolean itWasThereAlreadyAndRemoved = cortex.containsKey(key) && value == null;
                return ! itWasThereAlreadyAndSameValue || itWasThereAlreadyAndRemoved;  // true; // do you know about the rollback problem?
            } finally {
                if ( value == null )
                    cortex.remove(key);
                else
                    cortex.put(key, value);
            }
        }

        synchronized public Object get(String key) {
            return cortex.get(key);
        }

        public List<String> dumpDetails() {
            ArrayList<String> l = new ArrayList<String>();
            for(String key : cortex.keySet()) {
                Object value = cortex.get(key);
                l.add("" + key + " -> " + value);
            }
            return l;
        }
        
        public String toString() {
            return ToString.toString(this);
        }
    }

    private static class Elephant extends Simple {
        static ElephantMemory elephantMemory;

        private Elephant() {
            if (elephantMemory == null) {
                elephantMemory = new ElephantMemory();
                elephantMemory.load("cortex", cortex);
            }
        }

        /**
         * Never forgets. Store content in the database.
         */
        static class ElephantMemory {

            static final String DB_OPERATION_SET = "Map:set";
            static final String DB_OPERATION_PUT = "Map:put";
            static final String DB_OPERATION_REM = "Map:rem";
            
            private DKey.Name dKeyName(String id) {
                return new DKey.Name(this.getClass(), id);
            }

            /**
             * Load the Brain with content stored in database. Each entry in the database is a XML serialized HashMap.
             * Add All those hashmaps into the Brain storage (cortex).
             * 
             * @param id
             * @param cortexMap
             */
            public void load(String id, HashMap<String, Object> cortexMap) {
                try {
                    MilliTimer mt = new MilliTimer();

                    DKeyService service = DKeyService.locate(DKeyService.class);
                    Name k = dKeyName(id);
                    List<DKey> values = service.getByName(k);

                    if (values != null) {
                        for(DKey value : values) {
                            
                            int size = cortexMap.size();
                            
                            // 1: load the map found in database
                            HashMap<String, Object> dbMap = (HashMap<String, Object>) ObjectUtil.recreateFromString(value.getValue());
                            
                            // 2: perform the operation on the loaded map
                            // This is for memory footprint reason, and time spent reason.
                            
                            // 2.1: put, add the loaded content into cortexMap
                            if ( DB_OPERATION_PUT.equals(value.getOperation()) ) {
                                logger.info(value.getOperation() + ":  " + dbMap.size());
                                if (dbMap != null)
                                    cortexMap.putAll(dbMap);
                            }

                            // 2.2: put, remove loaded content keys from cortexMap
                            if ( DB_OPERATION_REM.equals(value.getOperation()) ) {
                                logger.info(value.getOperation() + ":  " + dbMap.size());
                                if (dbMap != null) {
                                    for(String key : dbMap.keySet()) {
                                        cortexMap.remove(key);
                                    }
                                }
                            }

                            // 2.3: put, replace cortexMap with the loaded content
                            if ( DB_OPERATION_SET.equals(value.getOperation()) ) {
                                logger.info(value.getOperation() + ":  " + dbMap.size());
                                if (dbMap != null) {
                                    cortexMap.clear();
                                    cortexMap.putAll(dbMap);
                                }
                            }
                            logger.info("cortexMap:  " + size + " -> " + cortexMap.size());
                        }
                        logger.info("cortex loaded:  " + mt.getString() + ' ' + cortexMap.size() + ' ' + cortexMap.keySet());
                    } else {
                        logger.info("cortex loaded:  " + "<nothing>" + ' ' + mt.getString());
                    }
                } catch (Exception ex) {
                    logger.severe("can't load:  " + ex);
                }
            }

            /**
             * Saves the map
             * 
             * Use 'set' in operation field in DB.
             * 
             * @param map the map to save
             * @param key indicates what key was changed in the map
             */
            public void saveAll(String id, HashMap<String, Object> map) {
                try {
                    MilliTimer mt = new MilliTimer();

                    String string = ObjectUtil.convertToString(map);

                    DKeyService service = DKeyService.locate(DKeyService.class);
                    DKey.Name name = dKeyName(id);
                                        
                    DKey value = new DKey(name, DB_OPERATION_SET, string);
                    service.append(value);
                    int appendedId = value.getId();

                    service.deleteBeforeByName(name, appendedId);

                    logger.info("saved:  " + mt.getString() + ' ' + name + ' ' + map.size() + ' ' + string.length());
                } catch (Exception ex) {
                    logger.severe("can't save:  " + map.size() + ' ' + ex);
                }
            }

            /**
             * Save only the changed key/value. Other entries are kept.
             * Use 'put' in operation field in DB.
             * 
             * @param id
             * @param map
             * @param key
             */
            public void appendOnlyKey(String id, HashMap<String, Object> map, String key) {
                try {
                    MilliTimer mt = new MilliTimer();

                    // Save only the content by key in a separate HashMap.
                    // Use 'put' operation so entries can be added into existing map
                    HashMap<String, Object> nMap = new HashMap<String, Object>();
                    nMap.put(key, map.get(key));
                    String string = ObjectUtil.convertToString(nMap);
                    
                    DKeyService service = DKeyService.locate(DKeyService.class);
                    DKey.Name name = dKeyName(id);
                    
                    DKey value = new DKey(name, DB_OPERATION_PUT, string);
                    service.append(value);
                    
                    logger.info("saved:  " + mt.getString() + ' ' + name + ' ' + map.size() + ' ' + string.length());
                } catch (Exception ex) {
                    logger.severe("can't save:  " + map.size() + ' ' + key + ' ' + ex);
                }
            }

            /**
             * Save only the changed key/value. Other entries are kept.
             * Use 'put' in operation field in DB.
             * 
             * @param id
             * @param map
             * @param key
             */
            public void removeKey(String id, HashMap<String, Object> map, String key) {
                try {
                    MilliTimer mt = new MilliTimer();

                    // Save only the content by key in a separate HashMap.
                    // Use 'put' operation so entries can be added into existing map
                    HashMap<String, Object> nMap = new HashMap<String, Object>();
                    nMap.put(key, "");//map.get(key)); // get should give null, but then XMLEncoded Map is empty
                    String string = ObjectUtil.convertToString(nMap);
                    
                    DKeyService service = DKeyService.locate(DKeyService.class);
                    DKey.Name name = dKeyName(id);
                    
                    DKey value = new DKey(name, DB_OPERATION_REM, string);
                    service.append(value);
                    
                    logger.info("removed:  " + mt.getString() + ' ' + name + ' ' + map.size() + ' ' + string.length());
                } catch (Exception ex) {
                    logger.severe("can't remove:  " + map.size() + ' ' + key + ' ' + ex);
                }
            }

            /**
             * Is the memory full?
             * 
             * @param id
             * @return
             */
            public boolean memoryCongested(String id) {
                DKeyService service = DKeyService.locate(DKeyService.class);
                DKey.Name name = dKeyName(id);
                List<DKey> all = service.getByName(name);
                return all != null && all.size() > 10;
            }
            
            public String toString() {
                return ToString.toString(ElephantMemory.this);
            }
        }

        public synchronized boolean put(String key, Object value) {
            if ( super.put(key, value) ) {
                if ( elephantMemory.memoryCongested("cortex") )
                    // Optimize storage.
                    elephantMemory.saveAll("cortex", cortex);
                else {
                    if ( value == null )
                        elephantMemory.removeKey("cortex", cortex, key);
                    else
                        elephantMemory.appendOnlyKey("cortex", cortex, key);
                }
                return true;
            }
            return false;
        }

        synchronized public Object get(String key) {
            return super.get(key);
        }

        public String toString() {
            return ToString.toString(Elephant.this);
        }
    }

    public String toString() {
        return ToString.toString(this);
    }
    
    /**
     * Erase content and reload from database
     */
    synchronized public void eraseElephantBrainAndReload() {
        ((Elephant)elephantBrain).cortex.clear();
        Elephant.elephantMemory.load("cortex", ((Elephant)elephantBrain).cortex);
    }

    public List<String> dumpDetails() {
        return ((Elephant)elephantBrain).dumpDetails();
    }
}
