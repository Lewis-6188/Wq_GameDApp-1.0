package com.game.wanq.uu.view.whget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Lewis.Liu on 2018/1/9.
 */

public class TagFrameLayout extends FrameLayout implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private final int FLING_MIN_DISTANCE = 20;// 移动最小距离
    private final int FLING_MIN_VELOCITY = 200;// 移动最大速度
    private GestureDetector mygesture;//构建手势探测器
    private View mView;

    public void addTagView(View view) {
        this.mView = view;
    }

    public TagFrameLayout(Context context) {
        this(context, null);
        this.mygesture = new GestureDetector(this);

    }

    public TagFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //setGestureListener();
        //设置Touch监听
        this.setOnTouchListener(this);
        //允许长按
        this.setLongClickable(true);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mygesture.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度（像素/秒）
        // velocityY：Y轴上的移动速度（像素/秒）
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
        //向
        if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE) {
//                     && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
        }
        //向上
        if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {

        }
        return false;
    }

}