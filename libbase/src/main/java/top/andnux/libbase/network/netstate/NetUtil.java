package top.andnux.libbase.network.netstate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

    public static boolean isNetworkAvailable() {
        ConnectivityManager service = (ConnectivityManager) NetStateManager.getInstance().getApplication().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (service == null) return false;
        NetworkInfo[] networkInfos = service.getAllNetworkInfo();
        for (NetworkInfo info : networkInfos) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static NetType getNetState() {
        ConnectivityManager service = (ConnectivityManager) NetStateManager.getInstance().getApplication().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (service == null) return NetType.NONE;
        NetworkInfo activeNetworkInfo = service.getActiveNetworkInfo();
        if (activeNetworkInfo == null) return NetType.NONE;
        int type = activeNetworkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            return NetType.FLOW;
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }
}
