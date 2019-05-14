package top.andnux.libbase.network.http;

import android.content.Context;

import java.util.Map;

public interface IHttpEngine {

    // get请求
    void get(boolean cache,
             boolean loading,
             Context context,
             String url,
             Map<String, Object> params,
             Map<String, String> headers,
             EngineCallBack callBack);

    // post请求
    void post(boolean cache,
              boolean loading,
              Context context,
              String url,
              Map<String, Object> params,
              Map<String, String> headers,
              EngineCallBack callBack);
}
