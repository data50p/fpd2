package com.femtioprocent.fpd2.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Essentially a set having two same types <T> as contained value. Note! not all methods implemented as expected
 * from a Collection class.
 * 
 * @see SetSet2
 * 
 */
public class SetSet1<T> {
    private HashMap<T, Set<T>> mapSet = new HashMap<T, Set<T>>();
    
    public void add(T s1, T s2) {
        Set<T> sT = mapSet.get(s1);
        if ( sT == null ) {
            sT = new HashSet<T>();
            mapSet.put(s1, sT);
        }
        sT.add(s2);
    }

    public void remove(T s1, T s2) {
        Set<T> sT = mapSet.get(s1);
        if ( sT == null )
            return;
        sT.remove(s2);
        if ( sT.size() == 0) {
            mapSet.remove(s1);
        }
    }
    
    public void clear() {
        mapSet = new HashMap<T, Set<T>>();            
    }

    public Set<T> get(T key) {
        return mapSet.get(key);
    }
    
    public boolean contains(T s1, T s2) {
        Set<T> sT = mapSet.get(s1);
        return sT == null ? false : sT.contains(s2);
    }
    
    public String toString() {
        return "" + mapSet;
    }
}
