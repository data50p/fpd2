package com.femtioprocent.fpd2.droid;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import java.util.List;

import com.femtioprocent.fpd2.util.ToString;
import java.util.logging.Logger;

/**
 * A Droid can do some small task on behalf of a owner Class and id. Remebering items for instance.
 * 
 * A Droid instance with same instanceId is semantically the same droid-entity. It will remember the same content.
 * 
 * All Droid share the same Brain-memory. 
 * 
 * All things to remeber MUST be able to be serialized into XML format.
 * 
 * There is a SimpleDroid with no memory at all.
 * 
 * How to do it:
 * 
 * 
 * 
1) Create a Droid Subclass extending DroidWithBrain as so:
<code>
public class FoobarDroid extends DroidWithBrain {   
    public RagrugDroid(InstanceId instanceId) {
        super(instanceId);
    }
}
</code>

2) Create an instance using the DroidFactory and let it remember something
<code>
    Droid fbDroid = DroidFactory.create(FoobarDroid.class, new Droid.InstanceId([SomeClass].class, [some value]));
    [SomeType] value = [value];
    fbDroid.remember("[some subid]", value);
</code>

3) Create an instance using the DroidFactory and retreive the remebered thing
<code>
    Droid fbDroid = DroidFactory.create(FoobarDroid.class, new Droid.InstanceId([SomeClass].class, [some value]));
    [SomeType] value = ([SomeType]) fbDroid.retrieve("[some subid]");
</code>
 * 
 * The remebered item is identified by: "FoobarDroid:[SomeClass]:[some value]:[some subid]"
 * 
 * <p>
 * A Droid can be nested.
 * 
4) Create an instance using the DroidFactory and let it remember something
<code>
    Droid fbDroid2 = DroidFactory.create(fbDroid, new Droid.InstanceId([SomeClass2].class, [some value2]));
    [SomeType2] value = [value2];
    fbDroid2.remember("[some subid2]", value);
</code>

5) Create an instance using the DroidFactory and retreive the remebered thing
<code>
    Droid fbDroid = DroidFactory.create(fbDroid, new Droid.InstanceId([SomeClass2].class, [some value2]));
    [SomeType2] value = ([SomeType2]) fbDroid.retrieve("[some subid2]");
</code>
 * 
 * The remebered item is identified by: "FoobarDroid:[SomeClass].[SomeClass2]:[some value].[some value2]:[some subid2]"
 * 
 * There is also an InstanceId without any specified owner Class. 
 * 
 * An intended InstanceId is typically constructed from A JPA-Entity and the entity id in the database. I.e. "ProgramUnit:3881"
 */
 public class Droid {
    private static final Logger logger = LoggerFactory.getLogger(Droid.class);

    public Class<? extends Droid> clazz;
    public InstanceId instanceId;
    
    Brain brain = Brain.getElephantBrain(); // the brain is the same for all instances (for each kind of Brain)
    
    static public class InstanceId {
        InstanceId parent;
        Class<?> clazz;
        String id;
        String key = null;

        public InstanceId() {
            this(null);
        }

        public InstanceId(Object id) {
            this.clazz = id == null ? Object.class : id.getClass();
            this.id = id == null ? "" : id.toString();
        }

        public InstanceId(Class<?> clazz, Object id) {
            if ( clazz == null )
                throw new IllegalArgumentException("clazz can't be null");
            this.clazz = clazz;
            this.id = id == null ? "" : id.toString();
        }

        public InstanceId(Class<?> clazz, int nid) {
            if ( clazz == null )
                throw new IllegalArgumentException("clazz can't be null");
            this.clazz = clazz;
            this.id = "" + nid;
        }

        /**
         * Nested InstaceId. Normally not used by User. (Used in DroidFactory for nested Droids)
         * 
         * @param instanceIdparent
         * @param instanceId
         */
        InstanceId(InstanceId instanceIdparent, InstanceId instanceId) {
            this.parent = instanceIdparent;
            this.clazz = instanceId.clazz;
            this.id = instanceId.id;
        }

        
        public String toString() {
            return mkKey() + "(" + ToString.toString(this) + ")";
        }

        private boolean classIsSimple(Class clazz) {
            return clazz == String.class || clazz == Object.class;
        }

        private String classGetSimpleName(Class clazz, boolean alt) {
            return alt ? classIsSimple(clazz) ? "" : clazz.getSimpleName() : clazz.getSimpleName();
        }
        
        public String mkKey() {
            if (key == null) {
                StringBuilder sb = new StringBuilder();
                StringBuilder sbAlt = new StringBuilder();

                if (parent != null) {
                    parent.getKeyLeft(sb, false);
                    parent.getKeyLeft(sbAlt, true);
                    sb.append(".");
                    sbAlt.append(".");
                    sb.append(classGetSimpleName(clazz, false));
                    sbAlt.append(classGetSimpleName(clazz, true));
                } else {
                    sb.append(classGetSimpleName(clazz, false));
                }
                sb = compact(sb, sbAlt);
                
                sb.append(":");

                if (parent != null) {
                    parent.getKeyRight(sb);
                    sb.append(".");
                }
                sb.append(id);

                key = sb.toString();
            }
            return key;
        }

        private StringBuilder compact(StringBuilder sb, StringBuilder sbAlt) {
            StringBuilder r = null;
            if ( sbAlt.length() == 0 )
                if ( sb.toString().equals("String") )
                    r = new StringBuilder();
                else if ( sb.toString().equals("Object") )
                    r = new StringBuilder();
                else
                    r = sb;
            else if ( sbAlt.length() == 0 || onlyChar(sbAlt.toString(), '.') )
                r = new StringBuilder();
            else
                r = sb;
//            logger.debug("COMPACT: '" + sb + "' '" + sbAlt + "'  ->  '" + r + "'");
            return r;
        }

        private boolean onlyChar(String string, char c) {
            for(Character ch : string.toCharArray()) {
                if ( ch != c)
                    return false;
            }
            return true;
        }

        private void getKeyLeft(StringBuilder sb, boolean alt) {
            if ( parent != null ) {
                parent.getKeyLeft(sb, alt);
                sb.append(".");
            }
            sb.append(classGetSimpleName(clazz, alt));
        }

        private void getKeyRight(StringBuilder sb) {
            if ( parent != null ) {
                parent.getKeyRight(sb);
                sb.append(".");
            }
            sb.append(id);
        }

        public InstanceId getParent() {
            return parent;
        }

        public void setParent(InstanceId parent) {
            this.parent = parent;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    Droid() {
        this.instanceId = new InstanceId();
        clazz = this.getClass();
        logger.fine("Droid: <init> " + clazz + ' ' + instanceId);
    }

    protected Droid(InstanceId instanceId) {
        this.instanceId = instanceId;
        clazz = this.getClass();
        logger.fine("Droid: <init> " + clazz + ' ' + instanceId);
    }

    public void remember(String subKey, Object value) {
        boolean updated = brain.put(mkKey(subKey), value);
        logger.info("Droid remember: " + subKey + "(" + mkKey(subKey) + ") " + updated + ' ' + value);
    }

    public void forget(String subKey) {
        remember(subKey, null);
    }

    public Object retrieve(String subKey) {
        return brain.get(mkKey(subKey));
    }

    public String dump() {
        logger.info("Droid brain: " + brain);
        return "" + brain;
    }

    public List<String> dumpDetails() {
        return brain.dumpDetails();
    }

    public void relaod() {
        brain.eraseElephantBrainAndReload();
    }

    protected String mkKey(String subKey) {
        String name = clazz == DroidFactory.DefaultDroid.class ? "" : clazz.getSimpleName();
        return name + ':' + instanceId.mkKey() + ':' + subKey;
    }

    public Class<? extends Droid> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Droid> clazz) {
        this.clazz = clazz;
    }

    public InstanceId getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(InstanceId instanceId) {
        this.instanceId = instanceId;
    }
}
