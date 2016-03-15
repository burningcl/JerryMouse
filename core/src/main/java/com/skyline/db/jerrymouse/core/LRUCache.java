package com.skyline.db.jerrymouse.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 固定大小的LRU缓存。线程安全
 *
 * @author guocz
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> {
	public static interface CacheResizeListener<K, V> {
		void onEntryToDel(Map.Entry<K, V> eldest);
	}

	private static final float factor = 0.75f;

	private Map<K, V> map;

	private int cacheSize;

	public LRUCache(int cacheSize) {
		this.cacheSize = cacheSize;
		int capacity = (int) Math.ceil(cacheSize / factor) + 1;
		map = new LinkedHashMap<K, V>(capacity, factor, true) {
			private static final long serialVersionUID = -6265505777659103537L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				boolean todel = size() > LRUCache.this.cacheSize;
				return todel;
			}
		};
	}

	public synchronized V get(K key) {
		return map.get(key);
	}

	public synchronized void put(K key, V value) {
		map.put(key, value);
	}

	public synchronized void clear() {
		map.clear();
	}

	public synchronized int usedEntries() {
		return map.size();
	}

	public synchronized Collection<Map.Entry<K, V>> getAll() {
		return new ArrayList<Map.Entry<K, V>>(map.entrySet());
	}

}