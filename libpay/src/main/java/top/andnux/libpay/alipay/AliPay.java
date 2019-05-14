package top.andnux.libpay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import top.andnux.libpay.Pay;
import top.andnux.libpay.PayListener;

public class AliPay implements Pay {

    private static final int SDK_PAY_FLAG = 0x88;
    private PayListener mPayListener;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if(mPayListener == null) {
                        return;
                    }
                    if(TextUtils.equals(resultStatus, "9000")) {    //支付成功
                        mPayListener.onSuccess("支付成功");
                    } else if(TextUtils.equals(resultStatus, "8000")) { //等待支付结果确认
                        mPayListener.onFailure("等待支付结果确认");
                    } else if(TextUtils.equals(resultStatus, "6001")) {		//支付取消
                        mPayListener.onCancel("支付取消");
                    } else if(TextUtils.equals(resultStatus, "6002")) {     //网络连接出错
                        mPayListener.onFailure("网络连接出错");
                    } else if(TextUtils.equals(resultStatus, "4000")) {        //支付错误
                        mPayListener.onFailure("支付错误");
                    }
                }
                default:
            }
        }

        ;
    };

    @Override
    public void pay(final Activity activity, final String json, PayListener listener) {
        mPayListener = listener;
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(json, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
