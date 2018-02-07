package com.game.wanq.uu.model.bean;

import java.io.Serializable;

/**
 * @autor:lzh 创建时间 : 2017-12-25
 **/
public class TGRComment implements Serializable {
    public String pid;
    public int type;
    public String typeObjid;
    public double score;
    public String content;
    public int zanCount;
    public int caiCount;
    public String time;
    public String typeObjname;
    public String typeObjicon;

    public TGRComment(String pid, int type, String typeObjid, double score, String content, int zanCount, int caiCount, String time, String typeObjname, String typeObjicon) {
        this.pid = pid;
        this.type = type;
        this.typeObjid = typeObjid;
        this.score = score;
        this.content = content;
        this.zanCount = zanCount;
        this.caiCount = caiCount;
        this.time = time;
        this.typeObjname = typeObjname;
        this.typeObjicon = typeObjicon;
    }


}
