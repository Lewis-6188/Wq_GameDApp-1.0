package com.game.wanq.uu.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lewis.Liu on 2018/1/21.
 */

public class TBBqian implements Serializable {
    public String pid;    //编号
    public String name;  //名称
    public List<TGame> tGames;

    public TBBqian(String pid, String name, List<TGame> tGames) {
        this.pid = pid;
        this.name = name;
        this.tGames = tGames;

    }
}
