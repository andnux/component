package top.andnux.libbase.image.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import top.andnux.libbase.cache.ICache;
import top.andnux.libbase.cache.MemoryCache;
import top.andnux.libbase.image.BaseImageLoader;
import top.andnux.libbase.manager.ThreadManager;
import top.andnux.libbase.utils.IOUtil;

public class HttpImageLoader extends BaseImageLoader {

    private ICache<String, Bitmap> mCache;

    public HttpImageLoader() {
        mCache = new MemoryCache<>();
    }

    @Override
    public void display(ImageView view, String url) {
        view.setTag(url);
        Bitmap bitmap = mCache.get(url);
        if (bitmap != null) {
            show(view, url, bitmap);
        } else {
            ThreadManager.getInstance().execute(() -> {
                Bitmap download = download(url);
                show(view, url, download);
                mCache.put(url,download);
            });
        }
    }

    private Bitmap download(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        InputStream input = null;
        try {
            URL mUrl = new URL(url);
            conn = (HttpURLConnection) mUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setConnectTimeout(15 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                input = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(input);
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }

    @Override
    public void onLoadError() {

    }
}
