package com.game.wanq.uu.view.whget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.game.wanq.app.R;

/**
 * Created by Lewis.Liu on 2018/1/15.
 */

public class DownFView extends View {


    // 画最外边圆环的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    //矩形画笔
    private Paint mRectPaint;
    // 圆环颜色
    private int mRingColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 矩形宽度
    private float rectWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress;
    // 最外层圆的宽度
    private int mcircleWidth = 6;

    public DownFView(Context context) {
        this(context, null);
    }

    public DownFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);// 获取自定义的属性
        initVariable();
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TasksCompletedView, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
        mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
        rectWidth = typeArray.getDimension(R.styleable.TasksCompletedView_myrectWidth, 15);
        mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);
        mRingRadius = mRadius + mStrokeWidth / 2 - mcircleWidth / 2;
    }


    private void initVariable() {
        //最外层圆形画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mRingColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mcircleWidth);
        //动态圆弧画笔
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        //动态弧形的宽度
        mRingPaint.setStrokeWidth(mStrokeWidth);
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(mRingColor);
        mRectPaint.setStyle(Paint.Style.FILL);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        //画出最外层的圆
        canvas.drawCircle(mXCenter, mYCenter, mRadius + mcircleWidth, mCirclePaint);
        //画出里面的矩形
        canvas.drawRect(mXCenter - rectWidth, mYCenter - rectWidth, mXCenter + rectWidth, mYCenter + rectWidth, mRectPaint);
        if (mProgress > 0) {//动态画圆环
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawArc(oval, -90, ((float) mProgress / mTotalProgress) * 360, false, mRingPaint); //
        }
    }

    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();
    }
}