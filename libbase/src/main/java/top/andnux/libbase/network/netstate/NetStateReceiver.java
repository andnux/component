package top.andnux.libbase.network.netstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetStateReceiver extends BroadcastReceiver {

    private NetType mNetState;
    private NetStateListener mNetListener;

    public NetStateListener getNetListener() {
        return mNetListener;
    }

    public void setNetListener(NetStateListener mNetListener) {
        this.mNetListener = mNetListener;
    }

    public NetStateReceiver() {
        mNetState = NetType.NONE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (intent == null || intent.getAction() == null) {
            Log.e("TAG", "网络异常了");
            return;
        }
        if (intent.getAction().equalsIgnoreCase(
                "android.net.conn.CONNECTIVITY_CHANGE")) {
            mNetState = NetUtil.getNetState();
            if (NetUtil.isNetworkAvailable()) {
                Log.e("TAG", "网络连接了");
                if (this.mNetListener != null){
                    this.mNetListener.onConnect(mNetState);
                }
            } else {
                Log.e("TAG", "无网络");
                if (this.mNetListener != null){
                    this.mNetListener.onDisConnect();
                }
            }
        }
    }
}
