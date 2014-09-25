package com.femtioprocent.fpd2.droid;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import com.femtioprocent.fpd2.droid.Droid.InstanceId;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public class DroidFactory {
    private static final Logger logger = LoggerFactory.getLogger(DroidFactory.class);

    static class DefaultDroid extends Droid {
        public DefaultDroid(InstanceId instanceId) {
            super(instanceId);
        }
    }
    
    public static synchronized <T extends Droid> Droid create(Class<T> clazz, InstanceId instanceId) {
        if ( clazz == null ) {
            throw new IllegalArgumentException("class must not be null");
        }
        if ( clazz == Droid.class ) {
            throw new IllegalArgumentException("class must not be the abstract class Droid");
        }
        try {
            Constructor<? extends Droid> constructor = clazz.getConstructor(InstanceId.class);
            logger.fine("create droid from: " + clazz + ' ' + instanceId + ' ' + constructor);
            Droid droid = (Droid) constructor.newInstance(instanceId);
            return droid;
        } catch (Exception e) {
            logger.fine("Can't create droid: " + clazz + ' ' + instanceId + ' ' + e);
            return null;
        }
    }
    
    public static synchronized <T extends Droid> Droid create() {
        return create(DefaultDroid.class, new InstanceId());
    }

    public static synchronized <T extends Droid> Droid create(Class<T> clazz) {
        return create(clazz, new InstanceId());
    }

    public static synchronized <T extends Droid> Droid create(Class<T> clazz, String id) {
        return create(clazz, new InstanceId(id));
    }

    /**
     * Create a nested Droid
     * 
     * @param droid
     * @param instanceId
     * @return
     */
    public static synchronized Droid create(Droid droid, InstanceId instanceId) {
        return create(droid.getClass(), new InstanceId(droid.instanceId, instanceId));
    }

    /**
     * Create a nested Droid. Convenient method.
     * 
     * @param droid
     * @param id
     * @return
     */
    public static synchronized Droid create(Droid droid, String id) {
        return create(droid.getClass(), new InstanceId(droid.instanceId, new InstanceId(id)));
    }
}
