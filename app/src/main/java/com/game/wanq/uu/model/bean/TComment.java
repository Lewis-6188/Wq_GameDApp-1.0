package com.game.wanq.uu.model.bean;

import java.io.Serializable;

/**
 * @autor:lzh 创建时间 : 2017-12-25
 **/
public class TComment implements Serializable {
    public String pid;    //编号
    public String rpid;    //回复评论编号
    public String uid;  //评论用户ID
    public String ruid;    //回复用户编号
    public Integer type;  //评轮类型（1 游戏）
    public String typeObjid;  //对象ID
    public Double score;  //评分
    public String content;  //内容
    public String phoneType;  //手机名称
    public Integer zanCount;  //点赞数
    public Integer caiCount;  //踩数
    public String time;  //评论时间
    public String returnComment; //回复评论列表
    public String uidName;    //评论用户名称
    public String rUidName;    //回复用户名称
    public String uidIcon; //评论用户ICON
    public String rUidIcon; //回复用户ICON
    public boolean isCai;
    public boolean isZan;

    public TComment(String pid, String rpid, String uid, String ruid, Integer type, String typeObjid,
                    Double score, String content, String phoneType, Integer zanCount, Integer caiCount,
                    String time, String returnComment, String uidName, String rUidName, String uidIcon,
                    String rUidIcon, boolean isCai, boolean isZan) {
        this.pid = pid;
        this.rpid = rpid;
        this.uid = uid;
        this.ruid = ruid;
        this.type = type;
        this.typeObjid = typeObjid;
        this.score = score;
        this.content = content;
        this.phoneType = phoneType;
        this.zanCount = zanCount;
        this.caiCount = caiCount;
        this.time = time;
        this.returnComment = returnComment;
        this.uidName = uidName;
        this.rUidName = rUidName;
        this.uidIcon = uidIcon;
        this.rUidIcon = rUidIcon;
        this.isCai = isCai;
        this.isZan = isZan;
    }


}
