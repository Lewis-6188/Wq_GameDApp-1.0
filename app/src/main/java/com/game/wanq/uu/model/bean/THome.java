package com.game.wanq.uu.model.bean;

import java.io.Serializable;

public class THome implements Serializable {
    public String pid;    //编号
    public Integer type;  //类型 ( 1 游戏  )
    public String typeObjid;  //类型对象ID
    public String typeObjname;  //类型对象名称
    public Integer showType;  //展示类型（1 一栏一图 2 一栏二图）
    public String image;  //展示图
    public String tips1;  //标签1
    public String tips2;  //标签2
    public String tips3;  //标签3
    public Double score;  //评分
    public String intro;  //简介
    public String datas;

    public THome(Integer showType, String datas) {
        this.showType = showType;
        this.datas = datas;
    }

    public THome(Integer type, String typeObjid, String typeObjname, Integer showType, String image, String tips1, String tips2, String tips3, Double score, String intro) {
        this.type = type;
        this.typeObjid = typeObjid;
        this.typeObjname = typeObjname;
        this.showType = showType;
        this.image = image;
        this.tips1 = tips1;
        this.tips2 = tips2;
        this.tips3 = tips3;
        this.score = score;
        this.intro = intro;
    }

}
