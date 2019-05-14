package top.andnux.libbase.image.impl;

import android.widget.ImageView;

import top.andnux.libbase.image.BaseImageLoader;
import top.andnux.libbase.utils.BitmapUtil;

public class Base64ImageLoader extends BaseImageLoader {

    @Override
    public void display(ImageView view, String url) {
        show(view,url,BitmapUtil.stringToBitmap(url));
    }

    @Override
    public void onLoadError() {

    }
}
