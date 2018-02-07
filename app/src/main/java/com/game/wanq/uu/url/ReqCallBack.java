package com.game.wanq.uu.url;

public interface ReqCallBack {
    void onReqSuccess(String result);//响应成功

    void onFailure(String result);//响应失败
}
