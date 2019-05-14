package top.andnux.libbase.network.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.InputStream;
import java.util.Map;

public abstract class StringCallBack implements EngineCallBack {

    protected Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onPreExecute(Context context,
                             Map<String, Object> params,
                             Map<String, String> header) {
    }

    @Override
    public void onError(Exception e) {
        Log.e("HTTP", e.getMessage());
    }

    public abstract void onSuccess(String result);

    @Override
    public void onSuccess(InputStream result) {
        try {
            final String content = SupportUtil.getContentString(result);
            result.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(content);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }
}
