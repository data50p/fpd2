package com.femtioprocent.fpd2.util;

import java.util.TreeMap;


public class AutoSortedMap<Tk, Tv> extends TreeMap<Tk, Tv> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    LambdaFunctions.Lambda2<Tk, Tv> getDef = null;

    public AutoSortedMap() {
    }

    public AutoSortedMap(LambdaFunctions.Lambda2<Tk, Tv> getDef) {
        this.getDef = getDef;
    }
    
    /**
     * A special get method that when the key is not present will call the Lambda2.eval to get a default value.
     * @param tk
     * @param getDef
     * @return
     */
    public Tv get(Tk tk, LambdaFunctions.Lambda2<Tk, Tv> getDef) {
        Tv tv = super.get(tk);
        if ( tv == null ) {
            tv = getDef.eval(tk);
            put(tk, tv);
        }
        return tv;
    }

    /**
     * @param tk
     * @return
     * @throws IllegalStateException
     */
    public Tv get(Object tk) throws IllegalStateException {
        Tv tv = super.get(tk);
        if ( tv == null ) {
            if ( getDef != null )
                tv = getDef.eval((Tk)tk);
            else
                throw new IllegalStateException("No creator");
            put((Tk)tk, tv);
        }
        return tv;
    }

    public Tv get(Tk tk, Tv tvDef) {
        Tv tv = super.get(tk);
        if ( tv == null ) {
            tv = tvDef;
        }
        put(tk, tv);
        return tv;
   }    
}