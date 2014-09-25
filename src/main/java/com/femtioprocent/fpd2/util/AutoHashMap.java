package com.femtioprocent.fpd2.util;

import java.util.ArrayList;
import java.util.HashMap;


public class AutoHashMap<Tk, Tv> extends HashMap<Tk, Tv> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Call this getDef.eval to get the value when HashMap dosn't contain a value for some key.
     */
    LambdaFunctions.Lambda2<Tk, Tv> getDef = null;

    public AutoHashMap() {
    }

    public AutoHashMap(LambdaFunctions.Lambda2<Tk, Tv> getDef) {
        this.getDef = getDef;
    }
    
    /**
     * A special get method that when the key is not present will call the Lambda2.eval to get a default value.
     * @param tk
     * @param getDef
     * @return
     */
    public synchronized Tv get(Tk tk, LambdaFunctions.Lambda2<Tk, Tv> getDef) {
        Tv tv = super.get(tk);
        if ( tv == null ) {
            tv = getDef.eval(tk);
            put(tk, tv);
        }
        return tv;
    }

    /**
     * Get from HashMap using key. If it is not there (value is null) then create a new content instance and put it there.
     * @param tk
     * @return
     * @throws IllegalStateException
     */
    public synchronized Tv get(Object tk) throws IllegalStateException {
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

    public synchronized Tv get(Tk tk, Tv tvDef) {
        Tv tv = super.get(tk);
        if ( tv == null ) {
            tv = tvDef;
        }
        put(tk, tv);
        return tv;
   }    
}