package top.andnux.libbase.downloader;

import android.content.Context;

public class Downloader {

    private final Context mContext;

    private Downloader(Context context) {
        this.mContext = context;
    }

    public static Downloader with(Context context) {
        return new Downloader(context);
    }
}
