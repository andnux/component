package top.andnux.libbase.cache;

import android.util.LruCache;

import top.andnux.libbase.cache.BaseCache;

public class MemoryCache<K, V> extends BaseCache<K, V> {

    private LruCache<K, V> cache = null;
    private long mTimeOut;

    public MemoryCache() {
        long memory = Runtime.getRuntime().totalMemory();
        cache = new LruCache<>((int) (memory / 8));
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        if (!isAvailability()) {
            cache.remove(key);
            return null;
        }
        return cache.get(key);
    }

    @Override
    public void setTimeOut(long time) {

    }
}
