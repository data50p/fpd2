package com.femtioprocent.fpd2.util;

public class Tuple3<T1, T2, T3> {
    public String id1, id2, id3;
    public final T1 t1;
    public final T2 t2;
    public final T3 t3;
    
    public Tuple3(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        id1 = id2 = id3 = "";
    }

    public Tuple3(T1 t1, T2 t2, T3 t3, String id1, String id2, String id3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
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

    /**
     * @return the t3
     */
    public T3 getT3() {
        return t3;
    }

    private String f(String s) {
        return s.length() == 0 ? "" : s + "=";
    }

    public String toString() {
        return "[" + f(id1) + t1 + ',' + f(id2) + t2+ ',' + f(id3) + t3 + ']';
    }
}
