/**
 *
 */
package com.game.wanq.uu.url;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Author: [6188] 2017年3月7日 上午10:28:39
 * @Desc: <p>
 * </p>
 */
public class ParaAcquisition {
    private static ParaAcquisition mInfoUtils;

    public static ParaAcquisition getInstance(Context context) {
        if (mInfoUtils == null) {
            mInfoUtils = new ParaAcquisition(context);
        }
        return mInfoUtils;
    }

    private ParaAcquisition(Context context) {
        this.mContext = context.getApplicationContext();
        this.tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private Context mContext;
    private TelephonyManager tm;
    // 手机网络数据连接类型
    private final String NETWORN_2G = "2g";
    private final String NETWORN_3G = "3g";
    private final String NETWORN_4G = "4g";
    private final String NETWORN_WIFI = "wifi";
    private final String NETWORN_MOBILE = "未知网络";
    /**
     * 没有网络连接
     */
    private final String NETWORN_NONE = "NETWORN_NONE";

    // 当前设备网络运营商
    /**
     * 移动
     */
    private static final String NETWORKNAME_YD = "ChinaMobile";
    /**
     * 联通
     */
    private static final String NETWORKNAME_LT = "ChinaUnicom";
    /**
     * 电信
     */
    private static final String NETWORKNAME_DX = "ChinaTelecom";
    /**
     * 未知
     */
    private static final String NETWORKNAME_UN = "UNKNOWN";

    /**
     * 获取设备imei
     */
    public String imei() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getDeviceId();
    }

    /**
     * 获取手机型号
     */
    public String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 电话类型 ,1:表示手机，0表示：平板
     */
    public String getPhoneType() {
        return tm.getPhoneType() + "";
    }

    /**
     * 手机厂商
     */
    public String getProduct() {
        return android.os.Build.PRODUCT;
    }

    /**
     * android id
     */
    public String getAndroidId() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Android系统版本
     */
    public String getAndroidver() {
        return android.os.Build.VERSION.SDK_INT + "";
    }

    /**
     * 获取mac
     */
    public String getMacAddress() {
        String mac = "";
        // 获取wifi管理器
        WifiManager wifiMng = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfor = wifiMng.getConnectionInfo();
        if (wifiInfor != null)
            mac = wifiInfor.getMacAddress();
        return mac;
    }

    public String getVersionName() {
        // 获取packagemanager的实例
        String version = null;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取IP地址
     */
    public String getIpAddress() {
        try {
            String permission = "android.permission.ACCESS_WIFI_STATE";
            if (existPermissions(permission)) {
                WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) {
                    int ipInt = wifiManager.getConnectionInfo().getIpAddress();
                    if (ipInt != 0) {
                        return intToIpString(ipInt);
                    }
                }
            }
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }

    private String intToIpString(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * NETWORKNAME_UN:unknown NETWORKNAME_YD:移动 NETWORKNAME_LT:联通 NETWORKNAME_DX:电信
     */
    public String carrier() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        String imsi = tm.getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                return NETWORKNAME_YD;
            } else if (imsi.startsWith("46001")) {
                return NETWORKNAME_LT;
            } else if (imsi.startsWith("46003")) {
                return NETWORKNAME_DX;
            }
        }
        return NETWORKNAME_UN;
    }

    /**
     * 获取当前网络连接类型
     */
    @SuppressWarnings("deprecation")
    public String getNetworkName() {
        // 获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 如果当前没有网络
        if (null == connManager)
            return NETWORN_NONE;
        // 获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORN_NONE;
        }
        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_WIFI;
                }
        }
        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        // 如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORN_2G;
                        // 如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORN_3G;
                        // 如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORN_4G;
                        default:
                            // 中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA")
                                    || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NETWORN_3G;
                            } else {
                                return NETWORN_MOBILE;
                            }
                    }
                }
        }
        return NETWORN_NONE;
    }

    /**
     * 判断manifest中有没有指定的权限
     */
    public boolean existPermissions(String permission) {
        try {
            if (mContext == null || permission == null) {
                return false;
            }
            String pn = mContext.getPackageName();
            if (TextUtils.isEmpty(pn)) {
                return false;
            }
            PackageManager pm = mContext.getPackageManager();
            String[] ps = pm.getPackageInfo(pn, PackageManager.GET_PERMISSIONS).requestedPermissions;
            for (int i = 0; i < ps.length; i++) {
                if (permission.equals(ps[i])) {
                    return true;
                }
            }
        } catch (NameNotFoundException e) {
        }
        return false;
    }
}
