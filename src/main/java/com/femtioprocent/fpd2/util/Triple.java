package com.femtioprocent.fpd2.util;

public class Triple<T> {   

    public final T fst;
    public final T snd;
    public final T trd;
       
    public Triple(T fst, T snd, T trd) {
        this.fst = fst;
        this.snd = snd;
        this.trd = trd;
    }
        
    public String toString() {
        return "3[" + fst + ',' + snd + ',' + trd + ']';
    }
}
