package cn.ymex.kitx.kits;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Device {


    private static String KITX_ID = "kitx.device.id";
    private static String KITX_ID_PATH = File.separator + ".kitx.id";
    private static String DEF_MAC_ADDRESS = "02:00:00:00:00:00";

    /**
     * 存储自定义的唯一设备标识
     *
     * @param kitxID 设备标识
     */
    public static void storageKitxID(String kitxID) {
        Preferences.instance().put(KITX_ID, kitxID);
        File extFile = FileExt.getExternalStorageDirectory();
        try {
            FileExt.writeText(new File(extFile.getAbsoluteFile() + KITX_ID_PATH), kitxID);
        } catch (IOException e) {
            e.printStackTrace();
            Kits.log("系统存储DeviceID失败！" + e.getLocalizedMessage());
        }
    }


    /**
     * 同步KitId
     */
    public static void synKitId() {
        String kitId = Preferences.instance().get(KITX_ID, "");
        String fKitId = getKitxIdInFile();

        if (Empty.isEmpty(kitId) || Empty.isEmpty(fKitId)) {
            if (!Empty.isEmpty(kitId)) {
                storageKitxID(kitId);
                return;
            }
            if (!Empty.isEmpty(fKitId)) {
                storageKitxID(fKitId);
            }


        }
    }

    public static String getKitxId() {
        String kitId = Preferences.instance().get(KITX_ID, "");
        if (Empty.isEmpty(kitId)) {
            kitId = getKitxIdInFile();
        }
        return kitId;
    }

    private static String getKitxIdInFile() {
        File extFile = FileExt.getExternalStorageDirectory();
        String did = "";
        if (extFile != null) {
            try {
                did = FileExt.readText(new File(extFile.getAbsoluteFile() + KITX_ID_PATH));
            } catch (IOException e) {
                e.printStackTrace();
                Kits.log("获取存储DeviceID失败！" + e.getLocalizedMessage());
            }
        }
        return did;
    }

    /**
     * 获取MAC地址
     * Mac 指的就是我们设备网卡的唯一设别码，该码全球唯一，一般称为物理地址，硬件地址用来定义设备的位置
     *
     * @return mac
     */
    @RequiresPermission(allOf = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    })
    public static String getMacAddress() {
        String mac;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault();
        } else {
            mac = getMacFromHardware();
        }
        if (Empty.isEmpty(mac) || DEF_MAC_ADDRESS.equals(mac)) {
            WifiManager wifi = getWifiManager();
            if (Empty.isNotNull(wifi) && !wifi.isWifiEnabled()) {
                wifi.setWifiEnabled(true);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mac = getMacDefault();
                } else {
                    mac = getMacFromHardware();
                }
                wifi.setWifiEnabled(false);
            }
        }
        return mac;
    }


    /**
     * 获取MAC地址(<android 6.0)
     *
     * @return mac address
     */
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    private static String getMacDefault() {

        Empty.checkNull(Kits.getApplication());

        WifiManager wifi = getWifiManager();

        WifiInfo info = wifi.getConnectionInfo();
        if (Empty.isNotNull(info)) {
            return info.getMacAddress();
        }

        return DEF_MAC_ADDRESS;
    }


    /**
     * 获取MAC地址(android 6.0 +)
     * 从网络接口获取，找到接口是 wlan0
     * <p>
     * 参考1：https://www.jianshu.com/p/16d4ff4c4cbe
     * 参考2：https://blog.csdn.net/u012400885/article/details/53505597/
     *
     * @return mac
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return DEF_MAC_ADDRESS;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEF_MAC_ADDRESS;
    }


    /**
     * 获取WifiManager
     *
     * @return
     */
    public static WifiManager getWifiManager() {
        return (WifiManager) Kits.getApplication().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 获取TelephonyManager
     *
     * @return
     */
    public static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) Kits.getApplication().getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
    }


    /**
     * 获取手机卡主卡IMEI /DeviceId
     * IMEI 与SIM 卡是绑定关系 用于区别移动终端设备
     *
     * @return IMEI
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getIMEI() {
        TelephonyManager telephonyManager = getTelephonyManager();
        String imei = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            imei = telephonyManager.getImei();
        } else {
            imei = telephonyManager.getDeviceId();       //取出 IMEI
        }
        return Empty.or(imei,"");
    }

    /**
     * 获取手机卡主卡IMSI
     * IMSI 与SIM 卡是绑定关系 用于区别移动用户的有效信息
     *
     * @return IMEI
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getIMSI() {
        TelephonyManager telephonyManager = getTelephonyManager();
        String imsi = telephonyManager.getSubscriberId();
        return Empty.or(imsi,"");
    }

    /**
     * 获取手机卡主卡ICCID
     * ICCID为IC卡的唯一识别号码，共有20位数字组成，其编码格式为：XXXXXX 0MFSS YYGXX XXXX。
     * 分别介绍如下： 前六位运营商代码：
     * 中国移动的为：898600；898602；898604；898607 ，
     * 中国联通的为：898601、898606、898609，
     * 中国电信898603。
     *
     * @return ICCID
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getICCID() {
        TelephonyManager telephonyManager = getTelephonyManager();
        return telephonyManager.getSimSerialNumber();
    }


    /**
     * 获取 ANDROID_ID
     * 在设备首次运行的时候，系统会随机生成一64位的数字，并把这个数值以16进制保存下来，这个16进制的数字就是ANDROID_ID，但是如果手机恢复出厂设置这个值会发生改变。
     * android 8.0 这个值 有新的变化 ， 参考：https://developer.android.com/about/versions/oreo/android-8.0-changes.html
     *
     * @return AndroidID
     */
    public static String getAndroidID() {
        return Settings.System.getString(Kits.getApplication().getContentResolver(), Settings.System.ANDROID_ID);
    }


    /**
     * 手机序列号
     *
     * @return SerialNumber
     */
    @Deprecated
    public static String getSerialNumber() {
        return android.os.Build.SERIAL;
    }

    /**
     * 手机设备信息
     *
     * @return android.os.Build.FINGERPRINT;
     */
    public static String getFingerprin() {
        return android.os.Build.FINGERPRINT;
    }


    public static PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException {
        Empty.checkNull(Kits.getApplication());
        return Kits.getApplication().getPackageManager().getPackageInfo(Kits.getApplication().getPackageName(), 0);

    }

    /**
     * 获取App版本名称
     *
     * @return app version name
     */
    public static String getAppVersionName() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageInfo();
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Kits.log(e.getLocalizedMessage());
        }
        return "";
    }

    /**
     * 获取App版本名称
     *
     * @return app version name
     */
    public static long getAppVersionCode() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageInfo();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return packageInfo.getLongVersionCode();
            } else {
                return packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Kits.log(e.getLocalizedMessage());
        }
        return 0;
    }
}
