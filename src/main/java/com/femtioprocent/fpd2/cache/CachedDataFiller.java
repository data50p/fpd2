package com.femtioprocent.fpd2.cache;

/**
 * Implement this interface for filling the data used by CachedDataManager.
 * @param <T>
 */
public interface CachedDataFiller<T> {
	public T fill();
}
