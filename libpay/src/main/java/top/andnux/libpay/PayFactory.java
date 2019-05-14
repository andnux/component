package top.andnux.libpay;

import top.andnux.libpay.alipay.AliPay;
import top.andnux.libpay.uppay.UpPay;
import top.andnux.libpay.wxpay.WxPay;

public class PayFactory {

    public static Pay getWxPay(String appId, String secretKey) {
        return new WxPay(appId,secretKey);
    }

    public static Pay getAliPay() {
        return new AliPay();
    }

    private static Pay getUpPay(UpPay pay) {
        return pay;
    }
}
