package top.andnux.libbase.factory;

import android.os.Bundle;
import android.util.ArrayMap;

import com.google.gson.Gson;

import java.util.Map;

import top.andnux.libbase.fragment.BaseFragment;

public class FragmentFactory<T extends BaseFragment> {

    private Class<T> mClazz;
    private Map<String, Object> mParam = new ArrayMap<>();

    public FragmentFactory(Class<T> mClazz) {
        this.mClazz = mClazz;
    }

    public static <T extends BaseFragment> FragmentFactory<T> with(Class<T > clazz) {
        return new FragmentFactory<T>(clazz);
    }

    public FragmentFactory<T> addParam(String key, Object value) {
        if (mParam == null){
            mParam = new ArrayMap<>();
        }
        mParam.put(key, value);
        return this;
    }

    public FragmentFactory<T> addParams(Map<String,Object> values) {
        if (mParam == null){
            mParam = new ArrayMap<>();
        }
        mParam.putAll(values);
        return this;
    }

    public T build() {
        T t = null;
        try {
            t = mClazz.newInstance();
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String data = gson.toJson(mParam);
            bundle.putString("data", data);
            t.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
