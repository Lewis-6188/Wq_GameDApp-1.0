package com.game.wanq.uu.view.whget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.game.wanq.uu.model.GanmeSYScrollViewAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lewis.Liu on 2017/12/27.
 */

public class GameSYScrollView extends HorizontalScrollView implements
        View.OnClickListener {


    private int mScreenWitdh;//屏幕的宽度
    private Map<View, Integer> mViewPos;//保存View与位置的键值对
    private LinearLayout mContainer; //HorizontalListView中的LinearLayout
    private OnItemClickListener mOnClickListener;
    private int mChildWidth;//子元素的宽度
    private int mChildHeight;//子元素的高度
    private int mCountOneScreen;//每屏幕最多显示的个数
    private GanmeSYScrollViewAdapter mAdapter;
    private int mCurrentIndex;//当前最后一张图片的index
    private int mFristIndex;//当前第一张图片的下标

    public GameSYScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        this.mScreenWitdh = outMetrics.widthPixels;// 获得屏幕宽度
        this.mViewPos = new HashMap<View, Integer>();
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mContainer.getChildCount(); i++) {
//            mContainer.getChildAt(i).findViewById(R.id.llayout).setBackgroundDrawable(getResources().getDrawable(R.mipmap.fqbg2));
        }
//        v.findViewById(R.id.llayout).setBackgroundDrawable(getResources().getDrawable(R.mipmap.fqbg));
        mOnClickListener.onClickSYGame(v, mViewPos.get(v));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();
                if (scrollX >= mChildWidth) {// 如果当前scrollX为view的宽度，加载下一张，移除第一张
                    loadNextImg();
                }
                if (scrollX == 0) {// 如果当前scrollX = 0,往前设置一张，移除最后一张
                    loadPreImg();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mContainer = (LinearLayout) getChildAt(0);
    }

    //加载下一张图片
    protected void loadNextImg() {
        if (mCurrentIndex == mAdapter.getCount() - 1) { //数组边界值计算
            return;
        }
        scrollTo(0, 0);//移除第一张图片，且将水平滚动位置置0
        mViewPos.remove(mContainer.getChildAt(0));
        mContainer.removeViewAt(0);
        View view = mAdapter.getView(++mCurrentIndex, null, mContainer); //获取下一张图片，并且设置onclick事件，且加入容器中
        view.setOnClickListener(this);
        mContainer.addView(view);
        mViewPos.put(view, mCurrentIndex);
        mFristIndex++; //当前第一张图片小标
    }

    //加载前一张图片
    protected void loadPreImg() {
        if (mFristIndex == 0) {//如果当前已经是第一张，则返回
            return;
        }
        int index = mCurrentIndex - mCountOneScreen;//获得当前应该显示为第一张图片的下标
        if (index >= 0) {
            mContainer = (LinearLayout) getChildAt(0);
            int oldViewPos = mContainer.getChildCount() - 1; //移除最后一张
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);
            View view = mAdapter.getView(index, null, mContainer);//将此View放入第一个位置
            mViewPos.put(view, index);
            mContainer.addView(view, 0);
            view.setOnClickListener(this);
            scrollTo(mChildWidth, 0);//水平滚动位置向左移动view的宽度个像素
            mCurrentIndex--;//当前位置--，当前第一个显示的下标--
            mFristIndex--;
        }
    }

    public void initDatas(GanmeSYScrollViewAdapter mAdapter, int size) {
        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        final View view = mAdapter.getView(0, null, mContainer);// 获得适配器中第一个View
        mContainer.addView(view);
//        if (mChildWidth == 0 && mChildHeight == 0) {// 强制计算当前View的宽和高
//            int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//            int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//            view.measure(w, h);
//            mChildHeight = view.getMeasuredHeight();
//            mChildWidth = view.getMeasuredWidth();
//            mChildHeight = view.getMeasuredHeight();
//            mCountOneScreen = (mScreenWitdh / mChildWidth == 0) ? mScreenWitdh / mChildWidth + 1 : mScreenWitdh / mChildWidth + 2; // 计算每次加载多少个View
//        }
        initFirstScreenChildren(size);//初始化第一屏幕的元素
    }

    //加载第一屏的View
    public void initFirstScreenChildren(int mCountOneScreen) {
        mContainer = (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();
        mViewPos.clear();
        for (int i = 0; i < mCountOneScreen; i++) {
            View view = mAdapter.getView(i, null, mContainer);
            view.setOnClickListener(this);
            mContainer.addView(view);
            mViewPos.put(view, i);
            mCurrentIndex = i;
        }
    }

    public interface OnItemClickListener {
        void onClickSYGame(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }
}
