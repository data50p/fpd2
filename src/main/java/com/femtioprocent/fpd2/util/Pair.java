package com.femtioprocent.fpd2.util;

public class Pair<T> {   

    public final T fst;
    public final T snd;
    
    public Pair(T fst, T snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T getFst() {
        return fst;
    }

    public T getSnd() {
        return snd;
    }

    public String toString() {
        return "[" + fst + ',' + snd + ']';
    }
}
