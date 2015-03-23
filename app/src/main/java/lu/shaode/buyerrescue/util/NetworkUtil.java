package lu.shaode.buyerrescue.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtil {

    public static final int NET_TYPE_UNKNOWN = 0;
    public static final int NET_TYPE_WIFI    = 1;
    public static final int NET_TYPE_MOBILE  = 2;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mNetworkInfo = connMgr.getActiveNetworkInfo();

        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }

        return false;
    }

    /**
     * if wifi connected
     * 
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前连接的网络状态
     * 
     * @return <br>
     * NetworkUtil.NET_TYPE_UNKNOWN<br>
     * NetworkUtil.NET_TYPE_WIFI<br>
     * NetworkUtil.NET_TYPE_MOBILE
     */
    public static int checkNetworkInfo(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan == null) {
            return NET_TYPE_UNKNOWN;
        }
        // wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (wifi == State.CONNECTED) {
            return NET_TYPE_WIFI;
        }
        // mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (mobile == State.CONNECTED) {
            return NET_TYPE_MOBILE;
        }

        return NET_TYPE_UNKNOWN;

    }
}
