package top.andnux.libbase.network.http;

import android.content.Context;

import java.io.InputStream;
import java.util.Map;

public interface EngineCallBack {

    void onPreExecute(Context context,
                      Map<String, Object> params,
                      Map<String, String> header);

    void onError(Exception e);

    void onSuccess(InputStream result);
}
