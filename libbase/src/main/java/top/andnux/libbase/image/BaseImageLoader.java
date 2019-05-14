package top.andnux.libbase.image;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

public abstract class BaseImageLoader implements IImageLoader {

    private Handler handler = new Handler(Looper.getMainLooper());

    public abstract void onLoadError();

    protected void show(ImageView view, String url, Bitmap bitmap) {
        if (bitmap == null) {
            //处理加载失败的情况
        } else {
            if (view.getTag().toString().equals(url)) {
                handler.post(() -> view.setImageBitmap(bitmap));

            }
        }
    }
}
