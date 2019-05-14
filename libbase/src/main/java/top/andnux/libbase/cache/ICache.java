package top.andnux.libbase.cache;

public interface ICache<K, V> {

    void put(K key, V value);

    V get(K key);

    void setTimeOut(long time);
}
