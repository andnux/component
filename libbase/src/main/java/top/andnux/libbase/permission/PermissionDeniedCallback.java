package top.andnux.libbase.permission;

import androidx.annotation.NonNull;

import java.util.List;

public interface PermissionDeniedCallback {
    void onPermissionsDenied(int requestCode,
                              @NonNull List<String> perms);
}
