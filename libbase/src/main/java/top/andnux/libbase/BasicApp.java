package top.andnux.libbase;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.*;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import top.andnux.libbase.manager.ActivityLifecycleManager;
import top.andnux.libbase.network.netstate.NetStateManager;

public class BasicApp extends Application {

    private static  Application mApplication;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context)
                        ;//.setTimeFormat(new SimpleDateFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public static Application getApplication() {
        return mApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mApplication = this;
        registerActivityLifecycleCallbacks(
                ActivityLifecycleManager.getInstance());
        NetStateManager.getInstance().init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
