package top.andnux.libpay.wxpay;

import android.app.Activity;
import android.text.TextUtils;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.json.JSONObject;

import top.andnux.libpay.Pay;
import top.andnux.libpay.PayListener;

public class WxPay implements Pay {

    private String mAppId;
    private String mSecretKey;
    private WxManager instance = WxManager.getInstance();

    public WxPay(String appId, String secretKey) {
        mAppId = appId;
        mSecretKey = secretKey;
        instance.init(appId,secretKey);
    }

    @Override
    public void pay(Activity activity, String json, PayListener listener) {
        IWXAPI mWXApi = instance.getApi();
        if (!(mWXApi.isWXAppInstalled() &&
                mWXApi.getWXAppSupportAPI()
                        >= Build.PAY_SUPPORTED_SDK_INT)){
            if (listener != null){
                listener.onFailure("未安装微信或微信版本过低");
            }
            return;
        }

        try {
            JSONObject object = new JSONObject(json);
            if(TextUtils.isEmpty(object.optString("appid")) ||
                    TextUtils.isEmpty(object.optString("partnerid"))
                    || TextUtils.isEmpty(object.optString("prepayid")) ||
                    TextUtils.isEmpty(object.optString("package")) ||
                    TextUtils.isEmpty(object.optString("noncestr")) ||
                    TextUtils.isEmpty(object.optString("timestamp")) ||
                    TextUtils.isEmpty(object.optString("sign"))) {
                if(listener != null) {
                    listener.onFailure("支付参数错误");
                }
                return;
            }
            PayReq request = new PayReq();
            request.appId = object.optString("appId");
            request.partnerId = object.optString("partnerId");
            request.prepayId = object.optString("prepayId");
            request.packageValue = object.optString("packageValue");
            request.nonceStr = object.optString("nonceStr");
            request.timeStamp = object.optString("timeStamp");
            request.sign = object.optString("sign");
            instance.setListener(listener);
            mWXApi.sendReq(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
