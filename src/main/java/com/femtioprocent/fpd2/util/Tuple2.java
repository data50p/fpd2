package com.femtioprocent.fpd2.util;

public class Tuple2<T1, T2> {   
    public String id1, id2;
    public final T1 t1;
    public final T2 t2;
    
    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
        id1 = id2 = "";
    }

    public Tuple2(T1 t1, T2 t2, String id1, String id2) {
        this.t1 = t1;
        this.t2 = t2;
        this.id1 = id1;
        this.id2 = id2;
    }
    
    /**
     * @return the t1
     */
    public T1 getT1() {
        return t1;
    }

    /**
     * @return the t2
     */
    public T2 getT2() {
        return t2;
    }
    
    private String f(String s) {
        return s.length() == 0 ? "" : s + "=";
    }
    
    public String toString() {
        return "[" + f(id1) + t1 + ',' + f(id2) + t2 + ']';
    }
}
