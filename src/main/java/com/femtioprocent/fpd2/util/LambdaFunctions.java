package com.femtioprocent.fpd2.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Some Lambda function (surrogate for anonymous functions, coming in java 7 or java 8)
 */
public class LambdaFunctions {
    
    /**
     * A function taking one param
     * @param <T>
     */
    public static interface Lambda1<T> {
        boolean eval(T t);
    }

    /**
     * A function taking two param
     * @param <T>
     * @param <TT>
     */
    public static class Lambda2<T, TT> {
        final TT arg;
        
        /**
         * Arg is a parameter used for calling
         * @param arg
         */
        public Lambda2() {
            arg = null;
        }        

        public Lambda2(TT arg) {
            this.arg = arg;
        }        
        
        public boolean evalIs(T t) {
            return evalIs(t, arg);
        }
        
        public boolean evalIs(T t, TT arg) {
            return false;
        }
 
        public TT eval(T t) {
            return null;
        }
    }
    
    /**
     * Call l.eval(t) for each element in list and create a new List with the returned content
     * 
     * [t0, t1, t2, ..., tn] -> [l.eval(t0), l.eval(t1), l.eval(t2), l.eval(tn)] 
     * 
     * @param <T>
     * @param <TT> return type
     * @param list
     * @param l
     * @return
     */
    public static <T, TT> List<TT> map(List<T> list, Lambda2<T, TT> l) {
        List<TT> mList = new ArrayList<TT>();
        for(T t : list) {
            mList.add(l.eval(t));
        }
        return mList;
    }

    /**
     * Call l.call(t) for each element in list and create a new List with elements where the return value is true
     * 
     * @param <T>
     * @param list
     * @param l
     * @return
     */
    public static <T> List<T> filter(List<T> list, Lambda1<T> l) {
        List<T> mList = new ArrayList<T>();
        for(T t : list) {
            if ( l.eval(t) )
                mList.add(t);
        }
        return mList;
    }

    /**
     * Call l.eval(t) for each element in list and create a new List with elements where the return value is true
     * 
     * @param <T>
     * @param <TT>
     * @param list
     * @param func
     * @param arg
     * @return
     */
    public static <T, TT> List<T> filter(List<T> list, Lambda2<T, TT> func, TT arg) {
        List<T> mList = new ArrayList<T>();
        for(T t : list) {
            if ( func.evalIs(t, arg) )
                mList.add(t);
        }
        return mList;
    }
    
    /**
     * Call l.eval(t) for each element in list and create a new List with elements where the return value is true
     * 
     * Use the arg from constructor of Lambda2
     * 
     * @param <T>
     * @param <TT>
     * @param list
     * @param func
     * @return
     */
    public static <T, TT> List<T> filter(List<T> list, Lambda2<T, TT> func) {
        List<T> mList = new ArrayList<T>();
        for(T t : list) {
            if ( func.evalIs(t) )
                mList.add(t);
        }
        return mList;
    } 
}
