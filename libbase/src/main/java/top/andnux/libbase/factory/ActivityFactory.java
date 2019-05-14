package top.andnux.libbase.factory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;

import com.google.gson.Gson;

import java.util.Map;

import top.andnux.libbase.base.BaseActivity;

public class ActivityFactory<T extends BaseActivity> {

    private Context mContext;
    private Class<T> mClazz;
    private Map<String, Object> mParam = new ArrayMap<>();
    private int mReqCode;

    public ActivityFactory(Context mContext, Class<T> mClazz) {
        this.mContext = mContext;
        this.mClazz = mClazz;
    }

    public static <T extends BaseActivity> ActivityFactory<T> with(Context context, Class<T> clazz) {
        return new ActivityFactory<T>(context, clazz);
    }

    public static <T extends BaseActivity> ActivityFactory<T> with(Activity context, Class<T> clazz) {
        return new ActivityFactory<>(context, clazz);
    }

    public ActivityFactory<T> addParam(String key, Object value) {
        if (mParam == null){
            mParam = new ArrayMap<>();
        }
        mParam.put(key, value);
        return this;
    }

    public ActivityFactory<T> addParams(Map<String, Object> values) {
        if (mParam == null){
            mParam = new ArrayMap<>();
        }
        mParam.putAll(values);
        return this;
    }

    public ActivityFactory<T> reqCode(int reqCode) {
        mReqCode = reqCode;
        return this;
    }

    public void navigation() {
        if (mContext == null) {
            throw new IllegalArgumentException("mContext is null");
        }
        if (mClazz == null) {
            throw new IllegalArgumentException("mClazz is null");
        }

        Intent intent = new Intent(mContext, mClazz);
        Gson gson = new Gson();
        String data = gson.toJson(mParam);
        intent.putExtra("data", data);
        if (mReqCode > 0) {
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                activity.startActivityForResult(intent, mReqCode);
            } else {
                throw new IllegalArgumentException("mContext is not Activity");
            }
        } else {
            mContext.startActivity(intent);
        }
    }
}
