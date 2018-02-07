package com.game.wanq.uu.view.whget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Lewis.Liu on 2017/12/26.
 */

public class MyScrollView extends ScrollView {
//    private int mFirstY;
//    private int mCurrentY;
//    private LinearLayout mView;
    private onScrollChangedListener mListener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onScrollChanged(t);
        }
    }

//    public void setView(LinearLayout view) {
//        this.mView = view;
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mFirstY = (int) event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mCurrentY = (int) event.getY();
//                if (mCurrentY - mFirstY > 0) {//向下滑动
//                    mView.setVisibility(View.VISIBLE);
//                } else if (mFirstY - mCurrentY > 0) {//向上滑动
//                    mView.setVisibility(View.GONE);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    public void addOnScrollChangedListener(onScrollChangedListener listener) {
        mListener = listener;
    }

    public interface onScrollChangedListener {
        void onScrollChanged(int t);
    }
}
