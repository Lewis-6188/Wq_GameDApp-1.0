package com.game.wanq.uu.model.bean;

import java.io.Serializable;

/**
 * Created by Lewis.Liu on 2018/1/20.
 */

public class Tbiqian implements Serializable {
    public String pid;    //编号
    public String name;  //名称

    public Tbiqian(String pid, String name) {
        this.pid = pid;
        this.name = name;
    }
}
