package com.game.wanq.uu.model.bean;

/**
 * @autor:lzh 创建时间 : 2017-12-25
 **/
public class TUsers {
    public String pid;    //编号
    public String loginName;  //登录名
    public String nickName;  //称昵
    public String icon;  //图标
    public String password;  //密码
    public String birthday;  //生日
    public Integer sex;  //性别(0-女 1-男)
    public String phone;  //电话号码
    public String intro;  //简介
    public String rigstTime;  //注册时间


    public TUsers(String pid, String loginName, String nickName, String icon, String password, String birthday, Integer sex, String phone, String intro, String rigstTime) {
        this.pid = pid;
        this.loginName = loginName;
        this.nickName = nickName;
        this.icon = icon;
        this.password = password;
        this.birthday = birthday;
        this.sex = sex;
        this.phone = phone;
        this.intro = intro;
        this.rigstTime = rigstTime;
    }

}
