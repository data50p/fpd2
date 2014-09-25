package com.femtioprocent.fpd2.util;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class HashCounter<T> {
    public SortedMap<T, int[]> map = new TreeMap<T, int[]>();
    int startSize = 1;
    
    public HashCounter() {
        this(1);
    }

    public HashCounter(int startSize) {
        this.startSize = startSize;
    }
    
    public void add(T t, int ix, int val) {
        int[] I = map.get(t);
        if ( I == null ) {
            I = new int[ix+1 < startSize+1 ? startSize : ix+1];
            I[ix] = 1;
            map.put(t, I);
            return;
        }
        if ( ix >= I.length ) {
            int[] nIa = new int[ix+1];
            System.arraycopy(I, 0, nIa, 0, I.length);
            I = nIa;
            map.put(t, I);
        }
        I[ix] += val;
    }
    
    public void inc(T t, int ix) {
        add(t, ix, 1);
    }

    public void dec(T t, int ix) {
        add(t, ix, -1);
    }
    
    public int[] get(T t) {
        return map.get(t);
    }
    
    public String toString() {
        return map.toString();
    }
}
