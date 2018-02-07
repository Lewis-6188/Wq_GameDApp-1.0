package com.game.wanq.uu.view.whget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Lewis.Liu on 2018/1/29.
 */

public class ScrollLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnable = true;

    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public ScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnable && super.canScrollVertically();
    }

    //设置 RecyclerView 是否可以垂直滑动
    public void setScrollEnable(boolean isEnable) {
        this.isScrollEnable = isEnable;
    }
}