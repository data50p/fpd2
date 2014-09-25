package com.femtioprocent.fpd2.util;

import java.util.Iterator;
import java.util.List;

/**
 * The list to iterate and the iterator itself. The iterator is recycled after reset.
 */
public class RecycleIterator<T> implements Iterator<T> {
    List<T> puList;
    Iterator<T> iter;

    public RecycleIterator(List<T> puList) {
        this.puList = puList;
        iter = puList.iterator();
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public T next() {
        T pu = iter.next();
        return pu;
    }

    public void remove() {
        iter.remove();
    }

    public void reset() {
        iter = puList.iterator();
    }
    
    public int size() {
        return puList.size();
    }

    public class ThisIterable implements Iterable<Integer> {
        public Iterator iterator() {
            return RecycleIterator.this;
        }
    }
}
