/**
 * @projectName new-qxps-app-1.0
 * @version V1.0
 * @address http://www.yingmob.com/
 * @copyright 本内容仅限于淮安爱赢互通科技有限公司内部使用，禁止转发.
 */
package com.game.wanq.uu.view.whget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author LiuYi
 * @desc <p>
 * </p>
 * @date 2015年5月16日上午12:25:54
 */
public class MyListView extends ListView {
    public boolean isOnMeasure;

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isOnMeasure = false;
    }
}
