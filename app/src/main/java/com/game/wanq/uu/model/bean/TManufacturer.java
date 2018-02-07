package com.game.wanq.uu.model.bean;

/**
 * @autor:lzh 创建时间 : 2017-12-25
 **/
public class TManufacturer {
    public String pid;    //编号
    public String name;  //名称
    public String icon;  //图标
    public Double score;  //评分
    public String intro;  //简介
    public Integer sort;  //排序


    public TManufacturer(String pid, String name, String icon, Double score, String intro, Integer sort) {
        this.pid = pid;
        this.name = name;
        this.icon = icon;
        this.score = score;
        this.intro = intro;
        this.sort = sort;
    }

}
