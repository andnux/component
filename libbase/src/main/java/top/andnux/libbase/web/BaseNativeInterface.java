package top.andnux.libbase.web;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import top.andnux.libbase.annotation.KeepClass;

@KeepClass
public class BaseNativeInterface  {

    protected top.andnux.libbase.web.BaseWebFragment fragment;
    protected Map<String, Object> param;
    protected String func;
    protected String callback;
    protected WebView webView;

    public BaseNativeInterface(top.andnux.libbase.web.BaseWebFragment fragment, WebView webView) {
        this.fragment = fragment;
        this.webView = webView;
    }

    public void evaluateJavascript(String script) {
        this.webView.evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.e("TAG", value);
            }
        });
    }

    @JavascriptInterface
    public final void post(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            func = jsonObject.optString("func");
            String paramStr = jsonObject.optString("param");
            callback = jsonObject.optString("callback");
            JSONObject paramJSON = new JSONObject(paramStr);
            param = new ConcurrentHashMap<>();
            Iterator<String> keys = paramJSON.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object opt = paramJSON.opt(key);
                param.put(key, opt);
            }
            Method method = this.getClass().getMethod(func);
            method.setAccessible(true);
            method.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
