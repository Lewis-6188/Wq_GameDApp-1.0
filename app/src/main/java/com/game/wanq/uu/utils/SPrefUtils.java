/**
 *
 */
package com.game.wanq.uu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * @Author: [6188] 2017年2月24日 上午10:41:47
 * @Desc: <p>
 * </p>
 */
public class SPrefUtils {
    private static SPrefUtils mPrefUtils;

    public static SPrefUtils getInstance(Context context) {
        if (mPrefUtils == null) {
            mPrefUtils = new SPrefUtils(context);
        }
        return mPrefUtils;
    }

    private SPrefUtils(Context context) {
        this.mContext = context;
        encodeSpKey();
    }

    private Context mContext;
    private SharedPreferences mSdkPreference;
    // SharedPreferences文件名-kit
    private String SUPER_FILE_NAME;
    public String USER_ID;// 用户ID
    public String USER_NAME;
    public String USER_ICON;// 用户ICON
    public String USER_NICKNAME;//
    public String USER_PHONE;// 用户手机号
    public String USER_RIGSTTIME;


    private void encodeSpKey() {
        int pkgKey = mContext.getPackageName().replace(".", "").length();
        SUPER_FILE_NAME = encodeString(".ai_sp_files", pkgKey);
        USER_PHONE = encodeString(".userphone", pkgKey);
        USER_NAME = encodeString(".username", pkgKey);
        USER_ID = encodeString(".usreijsd_id", pkgKey);
        USER_ICON = encodeString(".userehuicon", pkgKey);
        USER_NICKNAME = encodeString(".asifhasfihasf", pkgKey);
        USER_RIGSTTIME = encodeString(".userresgistiame", pkgKey);
    }

    /**
     * 获取SDK内核文件记录信息
     */
    private SharedPreferences getSDKSharedPreferences() {
        if (mSdkPreference == null) {
            mSdkPreference = mContext.getSharedPreferences(SUPER_FILE_NAME, Context.MODE_PRIVATE);
        }
        return mSdkPreference;
    }

    public void putString(String key, String value) {
        try {
            SharedPreferences pre = getSDKSharedPreferences();
            if (!TextUtils.isEmpty(value)) {
                value = java.net.URLEncoder.encode(value, "GBK");
                byte ptext[] = value.getBytes("GBK");// 将字符串转换成byte类型数组，实质是各个字符的二进制形式
                BigInteger mbig = new BigInteger(ptext);// 二进制串转换为一个大整数
                pre.edit().putString(key, mbig.toString()).commit();
            } else {
                pre.edit().putString(key, value).commit();
            }
        } catch (UnsupportedEncodingException e) {
        }
    }

    public String getString(String key, String defaultString) {
        SharedPreferences pre = getSDKSharedPreferences();
        String src = pre.getString(key, null);
        if (!TextUtils.isEmpty(src)) {
            BigInteger sixthtest = new BigInteger(src);
            byte[] mt = sixthtest.toByteArray();// m为密文的BigInteger类型
            try {
                src = (new String(mt, "GBK"));
                src = java.net.URLDecoder.decode(src, "GBK");
            } catch (UnsupportedEncodingException e) {
            }
        } else {
            src = defaultString;
        }
        return src;
    }

    public void putLong(String key, long value) {
        SharedPreferences pre = getSDKSharedPreferences();
        pre.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defaultLong) {
        SharedPreferences pre = getSDKSharedPreferences();
        return pre.getLong(key, defaultLong);
    }

    public void putInt(String key, int value) {
        SharedPreferences pre = getSDKSharedPreferences();
        pre.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defaultValue) {
        SharedPreferences pre = getSDKSharedPreferences();
        return pre.getInt(key, defaultValue);
    }

    public void putBoot(String key, Boolean value) {
        SharedPreferences pre = getSDKSharedPreferences();
        pre.edit().putBoolean(key, value).commit();
    }

    public Boolean getBoot(String key, Boolean defaultValue) {
        SharedPreferences pre = getSDKSharedPreferences();
        return pre.getBoolean(key, defaultValue);
    }

    public String encodeString(String content, int key) {
        return caesar(1, content, key);
    }

    /**
     * 凯撒算法， 1加密；-1解密，明文， 密匙
     */
    private String caesar(int flag, String content, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char ichar = content.charAt(i);
            if (ichar >= 'a' && ichar <= 'z') { // 是小写字母
                ichar += flag * (key % 26);
                if (ichar < 'a') {
                    ichar += 26; // 向左超界
                } else if (ichar > 'z') {
                    ichar -= 26; // 向右超界
                }
            } else if (ichar >= 'A' && ichar <= 'Z') { // 是大写字母
                ichar += flag * (key % 26);
                if (ichar < 'A') {
                    ichar += 26;
                } else if (ichar > 'Z') {
                    ichar -= 26;
                }
            }
            result.append(ichar);
        }
        return result.toString();
    }
}
