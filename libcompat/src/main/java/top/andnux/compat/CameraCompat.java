package top.andnux.compat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;
import java.lang.ref.WeakReference;

public class CameraCompat {

    private WeakReference<Activity> mWeakReference;
    private int mRequestCode = 0x00;
    private String mCameraSavePath;

    private CameraCompat(Activity activity) {
        mWeakReference = new WeakReference<>(activity);
    }

    public CameraCompat withRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    public CameraCompat withCameraCompat(String cameraSavePath) {
        mCameraSavePath = cameraSavePath;
        return this;
    }

    public static CameraCompat with(Activity activity) {
        return new CameraCompat(activity);
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Activity context = mWeakReference.get();
        Uri uri = null;
        if (TextUtils.isEmpty(mCameraSavePath)) {
            mCameraSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + System.currentTimeMillis() + ".jpg";
        }
        File file = new File(mCameraSavePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = context.getPackageName() + ".fileProvider";
            uri = FileProvider.getUriForFile(context, authority, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, mRequestCode);
    }
}
