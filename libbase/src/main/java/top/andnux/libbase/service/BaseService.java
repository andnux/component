package top.andnux.libbase.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import top.andnux.libbase.utils.InjectUtil;

public abstract class BaseService extends Service {

    public abstract void onStarted(Intent intent);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            InjectUtil.injectServiceValue(this, intent);
            onStarted(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
