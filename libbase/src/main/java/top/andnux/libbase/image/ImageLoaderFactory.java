package top.andnux.libbase.image;

import android.util.ArrayMap;

import java.net.URI;
import java.util.Map;

import top.andnux.libbase.image.impl.Base64ImageLoader;
import top.andnux.libbase.image.impl.HttpImageLoader;

public class ImageLoaderFactory {

    private Map<String, IImageLoader> loaderMap = null;
    private static ImageLoaderFactory factory = null;

    private ImageLoaderFactory() {
        loaderMap = new ArrayMap<>();
        loaderMap.put("data", new Base64ImageLoader());
        loaderMap.put("http", new HttpImageLoader());
        loaderMap.put("https", new HttpImageLoader());
    }

    public static ImageLoaderFactory getInstance() {
        if (factory == null) {
            synchronized (ImageLoaderFactory.class) {
                if (factory == null) {
                    factory = new ImageLoaderFactory();
                }
            }
        }
        return factory;
    }


    public void putImageLoader(String scheme, IImageLoader loader) {
        loaderMap.put(scheme, loader);
    }

    public IImageLoader getImageLoader(String uri) {
        URI tmp = URI.create(uri);
        String host = tmp.getScheme();
        return loaderMap.get(host);
    }
}
