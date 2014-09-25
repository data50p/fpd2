package com.femtioprocent.fpd2.util;


/**
 * Hold an object of type T.
 * 
 * Useful for changing the instance after a em.merge()
 * 
 * @param <T>
 */
public class Container<T> {
    private T t;
    int setCounter = 0;
    
    public Container(T t) {
        this.t = t;
        setCounter++;
    }
    
    public T get() {
        return t;
    }

    public synchronized int getSetCounter() {
        return setCounter;
    }
    
    public synchronized T set(T t) {
        try {
            setCounter++;
            return this.t;
        } finally {
            this.t = t;
        }
    }
    
    public String toString() {
        return ToString.toString(this);
    }
}
