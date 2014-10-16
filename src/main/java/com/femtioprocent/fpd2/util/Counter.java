/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femtioprocent.fpd2.util;

/**
 *
 * @author lars
 */
public class Counter {
    int max;
    int c;
    
    public Counter() {
    }

    public Counter(int max) {
	this.max = max;
    }

    public int inc() {
	return ++c;
    }
    
    public int val() {
	return c;
    }

    public String toString() {
	if ( max > 0 )
	    return "" + c + "(" + max + ")";
	else
	    return "" + c;
    }

}
