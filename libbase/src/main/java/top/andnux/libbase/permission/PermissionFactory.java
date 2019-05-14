package top.andnux.libbase.permission;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.annotation.NonNull;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;
import top.andnux.libbase.R;
import top.andnux.libbase.manager.ActivityLifecycleManager;

import java.util.ArrayList;
import java.util.List;

public class PermissionFactory {

    private static final String TAG = "PermissionFactory";

    private static PermissionFactory mInstance = null;
    private int reqCode;
    private List<String> mPermissions;
    private PermissionDeniedCallback mDeniedCallback;
    private PermissionGrantedCallback mGrantedCallback;
    private PermissionSuccessCallback mSuccessCallback;
    private String mRationale;

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }
        if (!granted.isEmpty()) {
            if (mGrantedCallback != null) {
                mGrantedCallback.onPermissionsGranted(requestCode, granted);
            }
        }

        // Report denied permissions, if any.
        if (!denied.isEmpty()) {
            if (mDeniedCallback != null) {
                mDeniedCallback.onPermissionsDenied(requestCode, denied);
            }
        }
        // If 100% successful, call annotated methods
        if (!granted.isEmpty() && denied.isEmpty()) {
            //全部同意
            runMethods(requestCode);
        }
    }

    private void runMethods(int requestCode) {
        this.mPermissions = new ArrayList<>();
        if (mSuccessCallback != null) {
            mSuccessCallback.onPermissionsSuccess(requestCode);
        }
    }

    private PermissionFactory() {
        this.mPermissions = new ArrayList<>();
        this.reqCode = 0x999;
        this.mRationale = "为了APP正常允许，请全部授予权限";
        this.mDeniedCallback = (requestCode, perms) -> {
            Log.e(TAG, "PermissionFactory() mDeniedCallback called" + perms);
            Activity currentActivity = ActivityLifecycleManager.getInstance().getCurrentActivity();
            AppSettingsDialog build = new AppSettingsDialog.Builder(currentActivity)
                    .setRationale(this.mRationale)
                    .setTitle("权限提醒")
                    .setNegativeButton("取消")
                    .setPositiveButton("确定")
                    .build();
            build.show();
        };
        this.mGrantedCallback = (requestCode, perms) -> {
            Log.e(TAG, "PermissionFactory() mGrantedCallback called" + perms);
        };
        this.mSuccessCallback = requestCode -> {
            Log.e(TAG, "PermissionFactory() mSuccessCallback called");
        };
    }

    public static PermissionFactory getInstance() {
        if (mInstance == null) {
            synchronized (PermissionFactory.class) {
                if (mInstance == null) {
                    mInstance = new PermissionFactory();
                }
            }
        }
        return new PermissionFactory();
    }

    public PermissionFactory setReqCode(int reqCode) {
        this.reqCode = reqCode;
        return this;
    }

    public PermissionFactory addPermission(String permission) {
        this.mPermissions.add(permission);
        return this;
    }

    public PermissionFactory setPermissionGrantedCallback(PermissionGrantedCallback callback) {
        this.mGrantedCallback = callback;
        return this;
    }

    public PermissionFactory setPermissionDeniedCallback(PermissionDeniedCallback callback) {
        this.mDeniedCallback = callback;
        return this;
    }

    public PermissionFactory setPermissionSuccessCallback(PermissionSuccessCallback callback) {
        this.mSuccessCallback = callback;
        return this;
    }


    public PermissionFactory setRationale(String rationale) {
        this.mRationale = rationale;
        return this;
    }

    public void request() {
        String[] strings = this.mPermissions.toArray(new String[this.mPermissions.size()]);
        Activity currentActivity = ActivityLifecycleManager.getInstance().getCurrentActivity();
        if (EasyPermissions.hasPermissions(currentActivity, strings)) {
            if (this.mSuccessCallback != null) {
                this.mSuccessCallback.onPermissionsSuccess(this.reqCode);
            }
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(currentActivity, this.reqCode, strings)
                            .setRationale(this.mRationale)
                            .setPositiveButtonText("确定")
                            .setNegativeButtonText("取消")
                            .setTheme(R.style.BaseAppTheme)
                            .build());
        }
    }
}
