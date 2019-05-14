package top.andnux.libbase.network.http;

import android.util.Log;
import top.andnux.libbase.db.DaoSupportFactory;
import top.andnux.libbase.db.IDaoSupport;
import top.andnux.libbase.utils.MD5Util;

import java.util.List;

public class CacheDataUtil {

    /**
     * 获取数据
     */
    public static String getCacheResultJson(String finalUrl) {
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        List<CacheData> cacheDatas = dataDaoSupport.querySupport()
                .selection("key = ?").selectionArgs(MD5Util.string2MD5(finalUrl)).query();
        if (cacheDatas.size() != 0) {
            // 代表有数据
            CacheData cacheData = cacheDatas.get(0);
            String resultJson = cacheData.getValue();

            return resultJson;
        }
        return null;
    }

    /**
     * 缓存数据
     */
    public static long cacheData(String finalUrl, String resultJson) {
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().
                getDao(CacheData.class);
        dataDaoSupport.delete("key=?", MD5Util.string2MD5(finalUrl));
        long number = dataDaoSupport.insert(new CacheData(MD5Util.string2MD5(finalUrl), resultJson));
        Log.e("TAG", "number --> " + number);
        return number;
    }
}
