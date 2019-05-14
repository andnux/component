package top.andnux.libbase.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 跟网络相关的工具类
 */
public final class NetUtils {

    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException( "cannot be instantiated" );
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected( Context context ) {
        ConnectivityManager connectivity = (ConnectivityManager) context.
                getSystemService( Context.CONNECTIVITY_SERVICE );
        if ( null != connectivity ){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if ( null != info && info.isConnected() ){
                if ( info.getState() == NetworkInfo.State.CONNECTED ){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi( Context context ) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        if ( cm == null ){
            return false;
        }
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断是否是3G网络
     * @param context
     * @return
     */
    public static boolean is3G(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    public static int getNetWorkState(Context context) {
        return 0;
    }

    /**
     * 判断移动网络连接是否正常
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if ( context != null ) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );   //获取移动网络信息
            if ( mMobileNetworkInfo != null ) {
                return mMobileNetworkInfo.isAvailable();    //getState()方法是查询是否连接了数据网络
            }
        }
        return false;
    }

    /**
     *  判断WiFi和移动流量是否已连接
     * @param context
     * @return
     */
    public static boolean checkNetworkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi =connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile =connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isAvailable()||mobile.isAvailable()){  //getState()方法是查询是否连接了数据网络
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting( Activity activity ){
        Intent intent = new Intent( "/" );
        ComponentName cm = new ComponentName( "com.android.settings", "com.android.settings.WirelessSettings" );
        intent.setComponent( cm );
        intent.setAction( "android.intent.action.VIEW" );
        activity.startActivityForResult( intent, 0 );
    }
}
