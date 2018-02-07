package com.game.wanq.uu.model.bean;

/**
 * @autor:lzh 创建时间 : 2018-01-22
 **/
public class TBanner {
    public String pid;    //编号
    public String image;  //图片
    public Integer type;  //对象类型
    public String typeObjid;  //对象ID
    public String typeObjname;//对象名称


    public TBanner(String pid, String image, Integer type, String typeObjid, String typeObjname) {
        this.pid = pid;
        this.image = image;
        this.type = type;
        this.typeObjid = typeObjid;
        this.typeObjname = typeObjname;
    }


}
