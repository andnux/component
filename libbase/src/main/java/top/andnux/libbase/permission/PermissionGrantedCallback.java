package top.andnux.libbase.permission;

import androidx.annotation.NonNull;

import java.util.List;

public interface PermissionGrantedCallback {
    void onPermissionsGranted(int requestCode,
                              @NonNull List<String> perms);
}
