package com.game.wanq.uu.model.bean;

import java.io.Serializable;

/**
 * @autor:lzh 创建时间 : 2017-12-25
 **/
public class TGame implements Serializable {
    public String pid;//编号
    public String name;//游戏名称
    public String icon;//游戏图标
    public String pkgname;//包名
    public Double score;//评分
    public String manufacturerPid;//商厂ID
    public String manufacturerName; //商厂名称
    public int istj;//否是推荐（0 不推荐 1 推荐）
    public String topimg;//顶部大图
    public String detailimg; //情详图列表
    public String url;//下载地址
    public String intro;//简介
    public String content;//更新内容
    public String version;//版本
    public int insertNum;//安装数
    public int downloadNum;//下载数
    public int commentNum;//评论总数
    public int stagesNum;//分期总数   0-没有
    public String labelList;//标签列表
    public String size;//游戏大小
    public boolean isDown;
    public int subscribe;
    public boolean subscribeType;
    public String updatetime;

    public TGame(String pid, String name, String icon, String pkgname, Double score, String manufacturerPid,
                 String manufacturerName, int istj, String topimg, String detailimg, String url,
                 String intro, String content, String version, int insertNum, int downloadNum,
                 int commentNum, int stagesNum, String labelList, String size, String updatetime) {
        this.pid = pid;
        this.name = name;
        this.icon = icon;
        this.pkgname = pkgname;
        this.score = score;
        this.manufacturerPid = manufacturerPid;
        this.manufacturerName = manufacturerName;
        this.istj = istj;
        this.topimg = topimg;
        this.detailimg = detailimg;
        this.url = url;
        this.intro = intro;
        this.content = content;
        this.version = version;
        this.insertNum = insertNum;
        this.downloadNum = downloadNum;
        this.commentNum = commentNum;
        this.stagesNum = stagesNum;
        this.labelList = labelList;
        this.size = size;
        this.updatetime = updatetime;
    }

    public TGame(String pid, String name, String icon, String pkgname, Double score, String manufacturerPid,
                 String manufacturerName, int istj, String topimg, String detailimg, String url,
                 String intro, String content, String version, int insertNum, int downloadNum,
                 int commentNum, int stagesNum, String labelList, int subscribe, boolean subscribeType) {
        this.pid = pid;
        this.name = name;
        this.icon = icon;
        this.pkgname = pkgname;
        this.score = score;
        this.manufacturerPid = manufacturerPid;
        this.manufacturerName = manufacturerName;
        this.istj = istj;
        this.topimg = topimg;
        this.detailimg = detailimg;
        this.url = url;
        this.intro = intro;
        this.content = content;
        this.version = version;
        this.insertNum = insertNum;
        this.downloadNum = downloadNum;
        this.commentNum = commentNum;
        this.stagesNum = stagesNum;
        this.labelList = labelList;
        this.subscribe = subscribe;
        this.subscribeType = subscribeType;
    }

    public TGame(String pid, String name, String icon, String pkgname, Double score, String manufacturerPid,
                 String manufacturerName, int istj, String topimg, String detailimg, String url,
                 String intro, String content, String version, int insertNum, int downloadNum,
                 int commentNum, int stagesNum, String labelList, boolean isDown) {
        this.pid = pid;
        this.name = name;
        this.icon = icon;
        this.pkgname = pkgname;
        this.score = score;
        this.manufacturerPid = manufacturerPid;
        this.manufacturerName = manufacturerName;
        this.istj = istj;
        this.topimg = topimg;
        this.detailimg = detailimg;
        this.url = url;
        this.intro = intro;
        this.content = content;
        this.version = version;
        this.insertNum = insertNum;
        this.downloadNum = downloadNum;
        this.commentNum = commentNum;
        this.stagesNum = stagesNum;
        this.labelList = labelList;
        this.isDown = isDown;
    }

}
