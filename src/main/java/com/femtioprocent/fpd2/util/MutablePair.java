package com.femtioprocent.fpd2.util;

public class MutablePair<T> {

    public  T fst;
    public  T snd;
    
    public MutablePair() {
    }
    
    public MutablePair(T fst, T snd) {
        this.fst = fst;
        this.snd = snd;
    }
        
    public T getFst() {
        return fst;
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public T getSnd() {
        return snd;
    }

    public void setSnd(T snd) {
        this.snd = snd;
    }

    public String toString() {
        return "[~" + fst + ',' + snd + "~]";
    }
}
