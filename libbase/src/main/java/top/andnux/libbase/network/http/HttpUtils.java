package top.andnux.libbase.network.http;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils{

    private static final int POST_TYPE = 0x0011;
    private static final int GET_TYPE = 0x0022;

    private String mUrl;
    private int mType = GET_TYPE;
    private Map<String,Object> mParams;
    private Map<String,String> mHeaders;
    private IHttpEngine mHttpEngine = null;
    private Context mContext;
    private boolean mCache = false;
    private boolean mLoading = false;

    private HttpUtils(Context context){
        mContext = context;
        mParams = new HashMap<>();
        mHeaders = new HashMap<>();
    }
    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }

    public HttpUtils url(String url){
        this.mUrl = url;
        return this;
    }

    // 请求的方式
    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }
    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }

    // 是否配置缓存
    public HttpUtils cache(boolean isCache){
        mCache = isCache;
        return this;
    }

    // 是否配置缓存
    public HttpUtils loading(boolean isLoading){
        mLoading = isLoading;
        return this;
    }

    // 添加文件
    public HttpUtils addFile(String key, File value){
        ArrayList<File> files = null;
        for (String s : mParams.keySet()) {
            Object o = mParams.get(s);
            if (s.equals(key) && o instanceof List){
                files = (ArrayList<File>) o;
            }
        }
        if (files == null){
            files = new ArrayList<>();
        }
        files.add(value);
        mParams.put(key, files);
        return this;
    }
    public HttpUtils addFiles(String key,List<File> files){
        List<File> tmp = files;
        for (String s : mParams.keySet()) {
            Object o = mParams.get(s);
            if (s.equals(key) && o instanceof List){
                tmp = (ArrayList<File>) o;
            }
        }
        if (tmp == null){
            tmp = new ArrayList<>();
        }
        tmp.addAll(files);
        mParams.put(key, tmp);
        return this;
    }

    // 添加参数
    public HttpUtils addParam(String key,Object value){
        mParams.put(key, value);
        return this;
    }
    public HttpUtils addParams(Map<String,Object> params){
        mParams.putAll(params);
        return this;
    }

    // 添加请求头
    public HttpUtils addHeader(String key,String value){
        mHeaders.put(key, value);
        return this;
    }
    public HttpUtils addHeaders(Map<String,String> headers){
        mHeaders.putAll(headers);
        return this;
    }

    // 请求头：

    // 添加回掉 执行
    public void execute(EngineCallBack callBack){
        if (mHttpEngine == null){
            mHttpEngine = new OkHttpEngine();
        }
        callBack.onPreExecute(mContext,mParams,mHeaders);
        // 判断执行方法
        if(mType == POST_TYPE){
            post(mCache,mLoading,mContext,mUrl,mParams,mHeaders,callBack);
        }
        if(mType == GET_TYPE){
            get(mCache,mLoading,mContext,mUrl,mParams,mHeaders,callBack);
        }
    }

    public void execute(){
       execute(null);
    }

    /**
     * 每次可以自带引擎
     */
    public HttpUtils exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
        return this;
    }


    private void get(boolean cache,
                     boolean loading,
                     Context context,
                     String url,
                     Map<String, Object> params,
                     Map<String, String> headers,
                     EngineCallBack callBack) {
        mHttpEngine.get(cache, loading, context, url, params, headers, callBack);
    }


    private void post(boolean cache,
                      boolean loading,
                      Context context,
                      String url,
                      Map<String, Object> params,
                      Map<String, String> headers,
                      EngineCallBack callBack) {
        mHttpEngine.get(cache, loading, context, url, params, headers, callBack);
    }
}
