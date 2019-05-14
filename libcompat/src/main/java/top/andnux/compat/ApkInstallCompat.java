package top.andnux.compat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;

public class ApkInstallCompat {

    private static final int REQUEST_CODE_APP_INSTALL = 0x99;
    private WeakReference<Activity> mReference;
    private String mApkPath;

    private ApkInstallCompat(Activity activity) {
        mReference = new WeakReference<>(activity);
    }

    public static ApkInstallCompat with(Activity activity) {
        return new ApkInstallCompat(activity);
    }

    public void install(String apkPath) {
        mApkPath = apkPath;
        Activity activity = mReference.get();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = isHasInstallPermissionWithO(activity);
            if (!hasInstallPermission) {
                startInstallPermissionSettingActivity(activity);
                return;
            }
        }
        doInstall(apkPath);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_APP_INSTALL:
                install(mApkPath);
                break;
        }
    }

    private void doInstall(String apkPath) {
        Activity activity = mReference.get();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri = UriCompat.fromFile(activity, apkPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Activity context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }

    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Activity context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        context.startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
    }
}
