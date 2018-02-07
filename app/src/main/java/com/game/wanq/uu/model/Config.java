package com.game.wanq.uu.model;

/**
 * Created by Lewis.Liu on 2017/12/25.
 */

public class Config {
    //    private static final String urlIp = "http://192.168.1.104:8081";
    private static final String urlIp = "http://121.40.138.84:9999";
    public static final String getHome = urlIp + "/white/api/thomeApi/findAppHomeData";//获取首页数据
    public static final String getDetail = urlIp + "/white/api/tgameApi/findGameDetail";//获取游戏详情
    public static final String getSend = urlIp + "/white/api/tusersApi/sendmsg";//获取验证码
    public static final String getRegister = urlIp + "/white/api/tusersApi/register";//注册接
    public static final String getLogin = urlIp + "/white/api/tusersApi/login";//用户登录
    public static final String getGameList = urlIp + "/white/api/tgameApi/list";//游戏列表
    public static final String getPLList = urlIp + "/white/api/tcommentApi/list";//获取评论列表
    public static final String getPLCZ = urlIp + "/white/api/tcommentApi/updateUserPosition";//评论赞踩
    public static final String getPLFB = urlIp + "/white/api/tcommentApi/userComment";//发表评论
    public static final String getDownload = urlIp + "/white/api/tdownloadApi/uploadData";//下载
    public static final String getfindUserData = urlIp + "/white/api/tusersApi/findUserData";//查询用户信息
    public static final String getupdateUserData = urlIp + "/white/api/tusersApi/updateUserData";//修改用户信息
    public static final String getfindUserRelNumer = urlIp + "/white/api/tusersApi/findUserRelNumer";//获取用户粉丝等数
    public static final String getfindUserAuth = urlIp + "/white/api/tusersauthApi/findUserAuth";//用户认证查询
    public static final String getuserAuth = urlIp + "/white/api/tusersauthApi/userAuth";//用户认证
    public static final String getupdateUserCollection = urlIp + "/white/api/tuserscollectionApi/updateUserCollection";//收藏游戏
    public static final String getUserCollectionlist = urlIp + "/white/api/tuserscollectionApi/list";//收藏列表
    public static final String getUserwanguolist = urlIp + "/white/api/tuserscollectionApi/play";//收藏列表
    public static final String getBQlist = urlIp + "/white/api/tlabelApi/list";//获取所有标签
    public static final String getCSlist = urlIp + "/white/api/tmanufacturerApi/list";//厂商
    public static final String getPHBlist = urlIp + "/white/api/tgameApi/rankingList";//排行榜游戏列表
    public static final String getSousuoBQlist = urlIp + "/white/api/tselectlabelApi/list";//搜索界面标签
    public static final String getBannerlist = urlIp + "/white/api/tbannerApi/list";//banner
    public static final String getusersCommentlist = urlIp + "/white/api/tcommentApi/usersComment";//获取评论列表(个人中心)
    public static final String getSubscribe = urlIp + "/white/api/tusersubscribeApi/updateUserSubscribe";//是否预约
    public static final String getBqList = urlIp + "/white/api/tlabelApi/list";//获取所有标签
    public static final String getfindUserLabel = urlIp + "/white/api/tgameApi/findUserLabel";//获取游戏关联标签
    public static final String getRelLabel = urlIp + "/white/api/tgameApi/userRelLabel";//获取游戏关联标签
    public static final String getDelLabel = urlIp + "/white/api/tgameApi/userDelLabel";//获取游戏关联标签

    public static final int ooBaner = 0;
    public static final int oneItem = 1;
    public static final int twoItem = 2;
    public static final String DETAIL = "gameid";
}
