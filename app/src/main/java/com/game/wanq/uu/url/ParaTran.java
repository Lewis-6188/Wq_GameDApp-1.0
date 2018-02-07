package com.game.wanq.uu.url;

import android.content.Context;

import com.game.wanq.uu.utils.SPrefUtils;

import java.util.HashMap;

/**
 * Created by Lewis.Liu on 2017/12/29.
 */

public class ParaTran {
    private static ParaTran mTran;

    public static ParaTran getInstance(Context context) {
        if (mTran == null) mTran = new ParaTran(context);
        return mTran;
    }

    private ParaTran(Context context) {
        this.mContext = context;
//        this.paramsValue = new HashMap<String, String>();
        this.mSP = SPrefUtils.getInstance(mContext);
    }

    private Context mContext;
    private SPrefUtils mSP;
    //    private HashMap<String, String> paramsValue;
//    private final String IMEI = "mid";
//    private final String ANDROID_ID = "androidid";// 设备imei号
//    private final String MODEL = "model";// 型号
//    private final String MAC = "mac";// wifimac地址
//    private final String IPV4 = "ipv4";// 终端用户IPv4地址
//    private final String MANUFACTURER = "manufacturer";// 手机厂商
//    private final String PHONETYPE = "phonetype";// 设备类型，1:表示手机，0表示：平板
//    private final String NETWORKNAME = "networkname";// 当前上网网络运营商
//    private final String SIMNETWORKNAME = "simnetworkname";// SIM卡所属运营商
//    private final String SYSTEMVERSION = "osversion";// 操作系统版本号
//    private final String APPLICATION_VERSION = "version";// 当前应用版本号
//    private final String PNAME = "pack";// 当前应用包名
    private final String PAGENUM = "pageNum";//页码
    private final String PAGESIZE = "pageSize";//页码中数据条数
    private final String PID = "pid";//游戏ID
    private final String USERID = "userId";//用户ID
    private final String CODE = "code";//手机号
    private final String loginName = "loginName";//默认账号
    private final String UID = "uid";//评论ID
    private final String RUID = "ruid";//回复评论ID
    private final String SCORE = "score";//回复评论ID
    private final String TYPE = "type";//游戏类型
    private final String TYPEOBJID = "typeObjid";//评论对象ID
    private final String UPOFDOWN = "upOfDown";//1 赞  0 踩
    private final String CONTENT = "content";//内容
    private final String PHONETYPE = "phoneType";//手机名称
    private final String RPID = "rpid";
    private final String ISINSTALL = "isInstall";
    private final String NICKNAME = "nickName";  //称昵
    private final String ICON = "icon";  //图标
    private final String PASSWORD = "password";  //密码
    private final String BIRTHDAY = "birthday";  //生日
    private final String SEX = "sex";  //性别(0-女 1-男)
    private final String PHONE = "phone";  //电话号码
    private final String INTRO = "intro";  //简介
    private final String RIGSTTIME = "rigstTime";  //注册时间
    private final String REALNAME = "realName";//真实姓名
    private final String CARDTYPE = "cardType";//证件类型(1 身份证 )
    private final String CARDNUM = "cardNum";//证件号码
    private final String TIME = "time";
    private final String LABELID = "labelId";
    private final String MANUFACTURERID = "manufacturerId";
    private final String SORT = "sort";
    private final String NAME = "name";
    private final String GAMEID = "gameId";
    private final String SHOWTYPE = "showType";

    public HashMap<String, String> setBanner(String showType) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(SHOWTYPE, showType);
        return paramsValue;
    }

    public HashMap<String, String> setRelLabel(String gameId, String labelid) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(USERID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(GAMEID, gameId);
        paramsValue.put(LABELID, labelid);
        return paramsValue;
    }

    public HashMap<String, String> setUserLabel(String gameId) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(GAMEID, gameId);
        return paramsValue;
    }

    public HashMap<String, String> setSubscribe(String gameId) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(GAMEID, gameId);
        return paramsValue;
    }

    public HashMap<String, String> setUComment() {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        return paramsValue;
    }

    public HashMap<String, String> setPHlist(int pageNum, int pageSize, String labelId, String manufacturerId, String sort) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(PAGENUM, pageNum + "");
        paramsValue.put(PAGESIZE, pageSize + "");
        paramsValue.put(LABELID, labelId);
        paramsValue.put(MANUFACTURERID, manufacturerId);
        paramsValue.put(SORT, sort);
        return paramsValue;
    }

    public HashMap<String, String> setCSlist(int pageNum, int pageSize) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(PAGENUM, pageNum + "");
        paramsValue.put(PAGESIZE, pageSize + "");
        return paramsValue;
    }

    public HashMap<String, String> setUserCollectionlist(int pageNum, int pageSize) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(PAGENUM, pageNum + "");
        paramsValue.put(PAGESIZE, pageSize + "");
        return paramsValue;
    }

    public HashMap<String, String> setpdateUserCollection(int type, String typeObjid) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(TYPE, type + "");
        paramsValue.put(TYPEOBJID, typeObjid);
        return paramsValue;
    }

    public HashMap<String, String> setuserAuth(String realName, String phone, String cardNum, String time, int sex) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(REALNAME, realName);
        paramsValue.put(SEX, sex + "");
        paramsValue.put(PHONE, phone);
        paramsValue.put(CARDTYPE, 1 + "");
        paramsValue.put(CARDNUM, cardNum);
        paramsValue.put(TIME, time);
        return paramsValue;
    }

    public HashMap<String, String> setfindUserAuth() {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        return paramsValue;
    }

    public HashMap<String, String> setfindUserRelNumer() {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        return paramsValue;
    }

    public HashMap<String, String> setupdateUserData(String nicename, String birthday, Integer sex, String phone, String intro) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(PID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(loginName, mSP.getString(mSP.USER_NAME, ""));
        paramsValue.put(NICKNAME, nicename);
        paramsValue.put(BIRTHDAY, birthday);
        paramsValue.put(SEX, sex + "");
        paramsValue.put(PHONE, phone);
        paramsValue.put(INTRO, intro);
        paramsValue.put(RIGSTTIME, mSP.getString(mSP.USER_RIGSTTIME, ""));
        return paramsValue;
    }

    public HashMap<String, String> setfindUserData() {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        return paramsValue;
    }

    public HashMap<String, String> setDownload(int gameType, String typeObjid, String pid, boolean isDownd, boolean isInstall) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(TYPE, gameType + "");
        paramsValue.put(TYPEOBJID, typeObjid);
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        if (isDownd)
            paramsValue.put(PID, pid);
        if (isInstall)
            paramsValue.put(ISINSTALL, 1 + "");
        return paramsValue;
    }

    public HashMap<String, String> setPLFB(String ruid, int gameType,
                                           String typeObjid, String rpid, String score, String content) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(RUID, ruid);
        paramsValue.put(TYPE, gameType + "");
        paramsValue.put(TYPEOBJID, typeObjid);
        paramsValue.put(RPID, rpid);
        paramsValue.put(SCORE, score);
        paramsValue.put(CONTENT, content);
        paramsValue.put(PHONETYPE, android.os.Build.MODEL);
        return paramsValue;
    }

    public HashMap<String, String> setPLZC(int gameType, String typeObjid, String upOfDown) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(TYPE, gameType + "");
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(TYPEOBJID, typeObjid);
        paramsValue.put(UPOFDOWN, upOfDown);
        return paramsValue;
    }

    public HashMap<String, String> setPLList(int gameType, String typeObjid) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(UID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(TYPE, gameType + "");
        paramsValue.put(TYPEOBJID, typeObjid);
        return paramsValue;
    }

    public HashMap<String, String> setSSGameList(int paNum, int paSize, String labelId, String name) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(PAGENUM, paNum + "");
        paramsValue.put(PAGESIZE, paSize + "");
        paramsValue.put(USERID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(LABELID, labelId);
        paramsValue.put(NAME, name);
        return paramsValue;
    }

    public HashMap<String, String> setGameList(int paNum, int paSize, String labelId) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(PAGENUM, paNum + "");
        paramsValue.put(PAGESIZE, paSize + "");
        paramsValue.put(USERID, mSP.getString(mSP.USER_ID, ""));
        paramsValue.put(LABELID, labelId);
        return paramsValue;
    }

    public HashMap<String, String> setLogin(String phone, String code, String password) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(loginName, phone);
        paramsValue.put(CODE, code);
        paramsValue.put(PASSWORD, password);
        return paramsValue;
    }

    public HashMap<String, String> setRgister(String phone, String code) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(PHONE, phone);
        paramsValue.put(CODE, code);
        paramsValue.put(loginName, phone);
        paramsValue.put(PASSWORD, phone + "123456");
        return paramsValue;
    }

    public HashMap<String, String> setPhone(String phone) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(PHONE, phone);
        return paramsValue;
    }

    public HashMap<String, String> setHomeDatas(int paNum, int paSize) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(PAGENUM, paNum + "");
        paramsValue.put(PAGESIZE, paSize + "");
        return paramsValue;
    }

    public HashMap<String, String> setDetailDatas(String pid, String userId) {
        HashMap<String, String> paramsValue = setParameter();
        paramsValue.put(PID, pid);
        paramsValue.put(USERID, userId);
        return paramsValue;
    }


    private HashMap<String, String> setParameter() {
//        ParaAcquisition mAcquisition = ParaAcquisition.getInstance(mContext);
//        paramsValue.put(IMEI, mAcquisition.imei());
//        paramsValue.put(ANDROID_ID, mAcquisition.getAndroidId());
//        paramsValue.put(MODEL, mAcquisition.getModel());
//        paramsValue.put(MANUFACTURER, mAcquisition.getProduct());
//        paramsValue.put(PHONETYPE, mAcquisition.getPhoneType());
//        paramsValue.put(PNAME, mContext.getPackageName());
//        paramsValue.put(NETWORKNAME, mAcquisition.getNetworkName());
//        paramsValue.put(SIMNETWORKNAME, mAcquisition.carrier());
//        paramsValue.put(SYSTEMVERSION, mAcquisition.getAndroidver());
//        paramsValue.put(MAC, mAcquisition.getMacAddress());
//        paramsValue.put(IPV4, mAcquisition.getIpAddress());
//        paramsValue.put(APPLICATION_VERSION, mAcquisition.getVersionName());
        return new HashMap<String, String>();
    }
}
