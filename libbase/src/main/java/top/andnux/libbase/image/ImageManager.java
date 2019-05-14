package top.andnux.libbase.image;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ImageManager {

    private static final ImageManager ourInstance = new ImageManager();

    public static ImageManager getInstance() {
        return ourInstance;
    }

    private IImageLoader mImageLoader;
    private Application mContext;

    private ImageManager() {

    }

    public IImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void setImageLoader(IImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    public void display(ImageView view, String url) {
        mImageLoader = ImageLoaderFactory.getInstance()
                .getImageLoader(url);
        if (mImageLoader == null) {
            throw new RuntimeException("没有找到合适的图片加载器 url=" + url);
        }
        mImageLoader.display(view, url);
    }

    public void init(Context context) {
        if (context instanceof Application) {
            this.mContext = (Application) context;
        } else {
            this.mContext = (Application) context.getApplicationContext();
        }
    }

    public <T> void compress(List<T> photos, String targetDir, OnCompressListener listener) {
        if (TextUtils.isEmpty(targetDir)) {
            targetDir = getPath();
        }
        Luban.with(this.mContext)
                .load(photos)
                .ignoreBy(100)
                .setTargetDir(targetDir)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(listener).launch();
    }

    public <T> void compress(List<T> photos, OnCompressListener listener) {
        compress(photos, null, listener);
    }

    public String getPath() {
        return this.mContext.getCacheDir().getAbsolutePath();
    }
}
