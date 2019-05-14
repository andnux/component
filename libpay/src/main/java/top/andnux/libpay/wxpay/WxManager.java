package top.andnux.libpay.wxpay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.Map;

import top.andnux.libpay.PayConfig;
import top.andnux.libpay.PayListener;

public class WxManager implements IWXAPIEventHandler {

    private static final WxManager ourInstance = new WxManager();

    public static WxManager getInstance() {
        return ourInstance;
    }

    private WeakReference<Activity> mReference;
    private IWXAPI api;
    private PayListener mListener;

    public void setActivity(Activity activity) {
        mReference = new WeakReference<>(activity);
    }

    public PayListener getListener() {
        return mListener;
    }

    public void setListener(PayListener listener) {
        mListener = listener;
    }

    private WxManager() {
        Map<String, String> config = PayConfig.getWxConfig();
        String id = config.get("id");
        api = WXAPIFactory.createWXAPI(getApplication(), id);
        api.registerApp(id);
    }

    public IWXAPI getApi() {
        return api;
    }

    public void setApi(IWXAPI api) {
        this.api = api;
    }

    @SuppressLint("PrivateApi")
    private Application getApplication() {
        try {
            return (Application) Class.forName(
                    "android.app.ActivityThread").getMethod(
                    "currentApplication").invoke(null, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            PayResp resps = (PayResp) resp;
            if (resp.errCode == 0) {   //付款成功
                if (mListener != null) {
                    mListener.success(resps.extData);
                }
            } else {                //付款失败
                if (mListener != null) {
                    mListener.failure();
                }
            }
            Activity activity = mReference.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
