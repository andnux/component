package top.andnux.libbase.db;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import top.andnux.libbase.BasicApp;

public class DaoSupport<T> implements IDaoSupport<T> {

    private SQLiteDatabase mSqLiteDatabase;
    private Class<T> mClazz;
    private String TAG = "DaoSupport";
    private static final Object[] mPutMethodArgs = new Object[2];
    private static final Map<String, Method> mPutMethods
            = new ArrayMap<>();

    void init(Class<T> clazz) {
        this.mClazz = clazz;
        Application application = BasicApp.getApplication();
        File dbFile = application.getDatabasePath("core.db");
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ");

        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);// 设置权限
            String name = field.getName();
            String type = field.getType().getSimpleName();
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        String createTableSql = sb.toString();
        Log.e(TAG, "表语句--> " + createTableSql);
        mSqLiteDatabase.execSQL(createTableSql);
    }


    // 插入数据库 t 是任意对象
    @Override
    public long insert(T obj) {
        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.insert(DaoUtil.getTableName(mClazz),
                null, values);
    }

    @Override
    public void insert(List<T> datas) {
        // 批量插入采用 事物
        mSqLiteDatabase.beginTransaction();
        for (T data : datas) {
            // 调用单条插入
            insert(data);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    private QuerySupport<T> mQuerySupport;

    @Override
    public QuerySupport<T> querySupport() {
        if (mQuerySupport == null) {
            mQuerySupport = new QuerySupport<>(mSqLiteDatabase, mClazz);
        }
        return mQuerySupport;
    }

    // obj 转成 ContentValues
    private ContentValues contentValuesByObj(T obj) {
        ContentValues values = new ContentValues();
        Field[] fields = mClazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                // 设置权限，私有和共有都可以访问
                field.setAccessible(true);
                String key = field.getName();
                // 获取value
                Object value = field.get(obj);
                // put 第二个参数是类型  把它转换

                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;
                String filedTypeName = field.getType().getName();
                Method putMethod = mPutMethods.get(filedTypeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put",
                            String.class, value.getClass());
                    mPutMethods.put(filedTypeName, putMethod);
                }
                putMethod.invoke(values, mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }

    /**
     * 删除
     */
    public int delete(String whereClause, String[] whereArgs) {
        return mSqLiteDatabase.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs);
    }

    /**
     * 更新  这些你需要对  最原始的写法比较明了 extends
     */
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.update(DaoUtil.getTableName(mClazz),
                values, whereClause, whereArgs);
    }
}
