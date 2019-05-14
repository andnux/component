package top.andnux.libbase.network.netstate;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallback extends ConnectivityManager.NetworkCallback {

    private NetStateListener mNetStateListener;

    public NetworkCallback(NetStateListener mNetStateListener) {
        this.mNetStateListener = mNetStateListener;
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.e("TAG", "网络已经连接");

    }

    /**
     * 网络丢失的回调
     */
    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Log.e("TAG", "网络已中断");
        if (mNetStateListener != null) {
            mNetStateListener.onDisConnect();
        }
    }

    /**
     * 按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
     * <p>
     * 之后在仔细的研究
     */
    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        Log.e("TAG", "网络状态发生变化");
        NetType mNetState = NetUtil.getNetState();
        if (NetUtil.isNetworkAvailable()) {
            if (this.mNetStateListener != null){
                this.mNetStateListener.onConnect(mNetState);
            }
        } else {
            if (this.mNetStateListener != null){
                this.mNetStateListener.onDisConnect();
            }
        }

    }
}
