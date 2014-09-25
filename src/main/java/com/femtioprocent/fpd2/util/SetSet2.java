package com.femtioprocent.fpd2.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Essentially a set having two types <T, T2> as contained value. Note! not all methods implemented as expected
 * from a Collection class.
 * 
 * @see SetSet1
 * 
 */
public class SetSet2<T, T2> {
    private HashMap<T, Set<T2>> mapSet = new HashMap<T, Set<T2>>();
    
    public void add(T s1, T2 s2) {
        Set<T2> sT = mapSet.get(s1);
        if ( sT == null ) {
            sT = new HashSet<T2>();
            mapSet.put(s1, sT);
        }
        sT.add(s2);
    }

    public void remove(T s1, T2 s2) {
        Set<T2> sT = mapSet.get(s1);
        if ( sT == null )
            return;
        sT.remove(s2);
        if ( sT.size() == 0) {
            mapSet.remove(s1);
        }
    }
    
    public void clear() {
        mapSet = new HashMap<T, Set<T2>>();            
    }
    
    public boolean contains(T s1, T2 s2) {
        Set<T2> sT = mapSet.get(s1);
        return sT == null ? false : sT.contains(s2);
    }
    
    public String toString() {
        return "" + mapSet;
    }
}
