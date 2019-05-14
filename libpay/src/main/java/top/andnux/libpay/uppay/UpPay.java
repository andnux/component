package top.andnux.libpay.uppay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import top.andnux.libpay.Pay;
import top.andnux.libpay.PayListener;

public abstract class UpPay implements Pay {

    private PayListener mPayListener;
    private final String mMode = "00";
    private Context mContext;

    public class ActivityResultFragment extends Fragment {
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (data == null) {
                return;
            }
            String msg = "";
            /*
             * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
             */
            Bundle extras = data.getExtras();
            String str = null;
            if (extras != null) {
                str = extras.getString("pay_result");
            }
            if ("success".equalsIgnoreCase(str)) {
                // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
                // result_data结构见c）result_data参数说明
                if (data.hasExtra("result_data")) {
                    String result = extras.getString("result_data");
                    try {
                        JSONObject resultJson = new JSONObject(result);
                        String sign = resultJson.getString("sign");
                        String dataOrg = resultJson.getString("data");
                        // 此处的verify建议送去商户后台做验签
                        // 如要放在手机端验，则代码必须支持更新证书
                        verify(dataOrg, sign, mMode, mPayListener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mPayListener != null) {
                            mPayListener.onFailure(msg);
                        }
                    }
                }
                // 结果result_data为成功时，去商户后台查询一下再展示成功
                msg = "支付成功！";
                if (mPayListener != null) {
                    mPayListener.onSuccess(msg);
                }
            } else if ("fail".equalsIgnoreCase(str)) {
                msg = "支付失败！";
                if (mPayListener != null) {
                    mPayListener.onFailure(msg);
                }
            } else if ("cancel".equalsIgnoreCase(str)) {
                msg = "用户取消了支付";
                if (mPayListener != null) {
                    mPayListener.onCancel(msg);
                }
            }
        }
    }

    abstract void verify(String dataOrg, String sign, String mode, PayListener listener);

    @Override
    public void pay(Activity activity, String json, PayListener listener) {
        mContext = activity;
        mPayListener = listener;
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            FragmentManager manager = fragmentActivity.getSupportFragmentManager();
            manager.beginTransaction().add(android.R.id.content,
                    new ActivityResultFragment()).commit();
        }
        int ret = UPPayAssistEx.startPay(activity, null, null, json, mMode);
        if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
            // 需要重新安装控件
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("完成购买需要安装银联支付控件，是否安装?")
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx.installUPPayPlugin(mContext);
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(true)
                    .create();
            dialog.show();
        }
    }
}
