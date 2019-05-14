package top.andnux.libbase.permission;

import androidx.annotation.NonNull;

import java.util.List;

public interface PermissionSuccessCallback {
    void onPermissionsSuccess(int requestCode);
}
