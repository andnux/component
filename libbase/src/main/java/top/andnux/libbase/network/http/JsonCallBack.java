package top.andnux.libbase.network.http;

import com.google.gson.Gson;

public abstract class JsonCallBack<T> extends StringCallBack {

    public abstract void onSuccess(T result);

    @Override
    public void onSuccess(String result) {
        try {
            Class<?> clazzInfo = SupportUtil.analysisClazzInfo(this);
            Gson gson = new Gson();
            T o = (T) gson.fromJson(result, clazzInfo);
            onSuccess(o);
        }catch (Exception e){
            onError(e);
        }
    }
}
