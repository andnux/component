package top.andnux.compat;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

public class StatusBarCompat {

    private WeakReference<Activity> mReference;
    private boolean mScreenMode = false;

    private StatusBarCompat(Activity activity) {
        mReference = new WeakReference<>(activity);
    }
    //沉浸式全屏模式

    public static StatusBarCompat with(Activity activity) {
        return new StatusBarCompat(activity);
    }

    public StatusBarCompat screenMode(boolean isPull) {
        mScreenMode = isPull;
        return this;
    }

    public void start() {
        Activity activity = mReference.get();
        Window window = activity.getWindow();
        if (mScreenMode) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }
}
