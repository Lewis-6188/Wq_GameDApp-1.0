package com.game.wanq.uu.model.bean;

/**
 * @autor:lzh 创建时间 : 2017-12-25
 **/
public class TUsersCollection {
    public String pid;    //编号
    public String uid;  //用户ID
    public Integer type;  //收藏类型 (1 游戏)
    public String typeObjid;  //收藏对象ID
    public String time;  //收藏时间
    public String name;  //名称
    public String topimg;  //大图
    public Double score;  //评分


    public TUsersCollection(String pid, String uid, Integer type, String typeObjid, String time, String name, String topimg, Double score) {
        this.pid = pid;
        this.uid = uid;
        this.type = type;
        this.typeObjid = typeObjid;
        this.time = time;
        this.name = name;
        this.topimg = topimg;
        this.score = score;
    }

}
