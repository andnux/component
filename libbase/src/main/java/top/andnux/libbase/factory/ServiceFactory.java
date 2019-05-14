package top.andnux.libbase.factory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.google.gson.Gson;

import java.util.Map;

import top.andnux.libbase.service.BaseService;

public class ServiceFactory<T extends BaseService> {

    private Context mContext;
    private Class<T> mClazz;
    private Map<String, Object> mParam = new ArrayMap<>();
    private String mAction;

    public ServiceFactory(Context mContext, Class<T> mClazz) {
        this.mContext = mContext;
        this.mClazz = mClazz;
    }

    public static <T extends BaseService> ServiceFactory<T> with(Context context, Class<T> clazz) {
        return new ServiceFactory<T>(context, clazz);
    }

    public static <T extends BaseService> ServiceFactory<T> with(Activity context, Class<T> clazz) {
        return new ServiceFactory<T>(context, clazz);
    }

    public ServiceFactory<T> addParam(String key, Object value) {
        if (mParam == null) {
            mParam = new ArrayMap<>();
        }
        mParam.put(key, value);
        return this;
    }

    public ServiceFactory<T> addParams(Map<String, Object> values) {
        if (mParam == null) {
            mParam = new ArrayMap<>();
        }
        mParam.putAll(values);
        return this;
    }

    public ServiceFactory<T> setAction(String action) {
        mAction = action;
        return this;
    }


    public void start() {
        if (mContext == null) {
            throw new IllegalArgumentException("mContext is null");
        }
        if (mClazz == null) {
            throw new IllegalArgumentException("mClazz is null");
        }
        try {
            Intent intent = new Intent(mContext, mClazz);
            if (!TextUtils.isEmpty(mAction)) {
                intent.setAction(mAction);
            }
            Gson gson = new Gson();
            String data = gson.toJson(mParam);
            intent.putExtra("data", data);
            mContext.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
