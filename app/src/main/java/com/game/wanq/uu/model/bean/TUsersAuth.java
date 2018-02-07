package com.game.wanq.uu.model.bean;

/**
 * @autor:lzh 创建时间 : 2017-12-25
 **/
public class TUsersAuth {
    
    public String pid;    //编号
    public String uid;  //用户ID
    public String realName;  //真实姓名
    public Integer cardType;  //证件类型(1 身份证 )
    public String cardNum;  //证件号码
    public String time;  //时间

    public TUsersAuth(String pid, String uid, String realName, Integer cardType, String cardNum, String time) {
        this.pid = pid;
        this.uid = uid;
        this.realName = realName;
        this.cardType = cardType;
        this.cardNum = cardNum;
        this.time = time;
    }

}
