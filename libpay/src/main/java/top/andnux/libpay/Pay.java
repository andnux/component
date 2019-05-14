package top.andnux.libpay;

import android.app.Activity;

public interface Pay {

    void pay(Activity activity, String json, PayListener listener);
}
