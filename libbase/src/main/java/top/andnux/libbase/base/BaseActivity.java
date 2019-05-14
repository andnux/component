package top.andnux.libbase.base;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pub.devrel.easypermissions.EasyPermissions;
import top.andnux.libbase.network.netstate.NetStateManager;
import top.andnux.libbase.permission.PermissionFactory;
import top.andnux.libbase.utils.InjectUtil;

public abstract class BaseActivity extends AppCompatActivity  {

    private static final String TAG = "BaseActivity";

    public abstract void onCreated(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetStateManager.getInstance().registerObserver(this);
        try {
            boolean success = InjectUtil.injectActivityLayout(this);
            Intent intent = getIntent();
            if (intent != null) {
                InjectUtil.injectActivityValue(this, intent);
            }
            if (success) {
                onCreated(savedInstanceState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        NetStateManager.getInstance().unRegisterObserver(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        PermissionFactory.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
