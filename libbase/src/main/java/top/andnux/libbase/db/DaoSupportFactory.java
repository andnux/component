package top.andnux.libbase.db;

import android.database.sqlite.SQLiteDatabase;

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;

    // 持有外部数据库的引用
    private SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory() {

    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        DaoSupport<T> daoSupport = new DaoSupport<T>();
        daoSupport.init(clazz);
        return daoSupport;
    }
}
