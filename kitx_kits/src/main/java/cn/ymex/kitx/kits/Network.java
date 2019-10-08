package cn.ymex.kitx.kits;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresPermission;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;


public class Network {


    public static void openNetworkSettings() {
        try {
            Kits.getApplication().startActivity(
                    new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }


    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm =
                (ConnectivityManager) Kits.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return null;
        return cm.getActiveNetworkInfo();
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) Kits.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI;
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isMobileData() {
        NetworkInfo info = getActiveNetworkInfo();
        return null != info
                && info.isAvailable()
                && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }


    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkType getNetworkType() {
        if (isEthernet()) {
            return NetworkType.NETWORK_ETHERNET;
        }
        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetworkType.NETWORK_2G;

                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetworkType.NETWORK_3G;

                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetworkType.NETWORK_4G;

                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return NetworkType.NETWORK_3G;
                        } else {
                            return NetworkType.NETWORK_UNKNOWN;
                        }
                }
            } else {
                return NetworkType.NETWORK_UNKNOWN;
            }
        }
        return NetworkType.NETWORK_NO;
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static boolean isEthernet() {
        final ConnectivityManager cm =
                (ConnectivityManager) Kits.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (info == null) return false;
        NetworkInfo.State state = info.getState();
        if (null == state) return false;
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
    }


    /**
     * 判断 wifi 是否打开
     * 权限要求：<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = getWifiManager();
        if (manager == null) return false;
        return manager.isWifiEnabled();
    }

    /**
     * 打开或关闭 wifi
     * 权限要求:
     * <p>{@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     */
    @RequiresPermission(CHANGE_WIFI_STATE)
    public static void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = getWifiManager();
        if (manager == null) return;
        if (enabled == manager.isWifiEnabled()) return;
        manager.setWifiEnabled(enabled);
    }

    public static WifiManager getWifiManager() {
        return (WifiManager) Kits.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @RequiresPermission(INTERNET)
    public static boolean isAvailableByPing(String ip) {
        try {
            // ping网址3次
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);

            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            log("network ping : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                return true;
            }
        } catch (IOException e) {
            log("network ping:" + e.getLocalizedMessage());
        } catch (InterruptedException e) {
            log("network ping:" + e.getLocalizedMessage());
        }
        return false;
    }


    /**
     * 网络是否可用
     * 权限要求：<uses-permission android:name="android.permission.INTERNET" />
     * 会阻塞线程，要异步请求
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailableByDns(final String domain) {
        final String realDomain = TextUtils.isEmpty(domain) ? "www.baidu.com" : domain;
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(realDomain);
            return inetAddress != null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void log(String message) {
        Log.i("net work:", message);
    }

    public enum NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }


    public interface OnNetworkStatusChangedListener {
        void onDisconnected();

        void onConnected(NetworkType networkType);
    }


    protected static final class NetworkChangedReceiver extends BroadcastReceiver {


        public static NetworkChangedReceiver getInstance() {
            return LazyHolder.INSTANCE;
        }

        private NetworkType mType;
        private Set<OnNetworkStatusChangedListener> mListeners = new HashSet<>();

        public void registerListener(final OnNetworkStatusChangedListener listener) {
            if (listener == null) return;
            int preSize = mListeners.size();
            mListeners.add(listener);
            if (preSize == 0 && mListeners.size() == 1) {
                mType = getNetworkType();
                IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                Kits.getApplication().registerReceiver(NetworkChangedReceiver.getInstance(), intentFilter);
            }
        }

        public void unregisterListener(final OnNetworkStatusChangedListener listener) {
            if (listener == null) return;

            int preSize = mListeners.size();
            mListeners.remove(listener);
            if (preSize == 1 && mListeners.size() == 0) {
                Kits.getApplication().unregisterReceiver(NetworkChangedReceiver.getInstance());
            }

        }

        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                NetworkType networkType = getNetworkType();
                if (mType == networkType) {
                    return;
                }
                mType = networkType;
                if (networkType == NetworkType.NETWORK_NO) {
                    for (OnNetworkStatusChangedListener listener : mListeners) {
                        listener.onDisconnected();
                    }
                } else {
                    for (OnNetworkStatusChangedListener listener : mListeners) {
                        listener.onConnected(networkType);
                    }
                }
            }
        }

        private static class LazyHolder {
            private static final NetworkChangedReceiver INSTANCE = new NetworkChangedReceiver();
        }

    }

    public static class NetworkLifecycleObserver implements LifecycleObserver {

        private OnNetworkStatusChangedListener listener;

        public NetworkLifecycleObserver(OnNetworkStatusChangedListener listener) {
            this.listener = listener;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void onCreate() {
            NetworkChangedReceiver.getInstance()
                    .registerListener(listener);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause(LifecycleOwner owner) {
            NetworkChangedReceiver.getInstance().mListeners.remove(listener);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume(LifecycleOwner owner) {

            if (!NetworkChangedReceiver.getInstance().mListeners.contains(listener)) {
                NetworkChangedReceiver.getInstance().mListeners.add(listener);
            }

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            NetworkChangedReceiver.getInstance()
                    .unregisterListener(listener);
        }
    }
}
