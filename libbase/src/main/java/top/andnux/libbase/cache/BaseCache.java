package top.andnux.libbase.cache;


public abstract class BaseCache<K, V> implements ICache<K, V> {

    private long mTimeOut;

    protected boolean isAvailability() {
        return (System.currentTimeMillis() - mTimeOut) < 0;
    }

    @Override
    public void setTimeOut(long time) {
        this.mTimeOut = System.currentTimeMillis() + time;
    }
}