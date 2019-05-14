package top.andnux.libbase.permission;

import android.Manifest;

public enum PermissionType {

    WRITE_EXTERNAL_STORAGE(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            "我们需要访问你的SD卡权限，请同意权限。"),
    READ_EXTERNAL_STORAGE(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            "我们需要访问你的SD卡权限，请同意权限。"),
    CAMERA(
            Manifest.permission.CAMERA,
            "我们需要使用相机权限，请同意权限。"),
    CALL_PHONE(
            Manifest.permission.CALL_PHONE,
            "我们需要使用打电话权限，请同意权限。");

    private String rationale;
    private String perms;

    PermissionType(String rationale, String perms ) {
        this.rationale = rationale;
        this.perms = perms;
    }

    public String getRationale() {
        return rationale;
    }

    public String getPerms() {
        return perms;
    }
}
