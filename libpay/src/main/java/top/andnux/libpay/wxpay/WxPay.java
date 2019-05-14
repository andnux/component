package top.andnux.libpay.wxpay;

import android.app.Activity;

import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONObject;

import top.andnux.libpay.Pay;
import top.andnux.libpay.PayListener;

public class WxPay implements Pay {

    @Override
    public void pay(Activity activity, String json, PayListener listener) {
        try {
            JSONObject object = new JSONObject(json);
            PayReq request = new PayReq();
            request.appId = object.optString("appId");
            request.partnerId = object.optString("partnerId");
            request.prepayId = object.optString("prepayId");
            request.packageValue = object.optString("packageValue");
            request.nonceStr = object.optString("nonceStr");
            request.timeStamp = object.optString("timeStamp");
            request.sign = object.optString("sign");
            WxManager instance = WxManager.getInstance();
            instance.setListener(listener);
            instance.getApi().sendReq(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
