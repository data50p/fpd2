package com.femtioprocent.fpd2.util;

import java.util.ArrayList;

/**
 * A ArrayList where no more than maxSize entries can be added. Remove old antries before adding the latest.
 * @param <T>
 */
public class MaxSizedArrayList<T> extends ArrayList<T> {
    private final int maxSize;

    public MaxSizedArrayList(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    public boolean add(T t) {
        if (size() >= maxSize) {
            remove(0);
        }
        return super.add(t);
    }
}
