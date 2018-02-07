package com.game.wanq.uu.view.whget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.view.emoticon.StringUtils;

/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author pengjingnag
 * @see [相关类/方法]
 * @since [产品/模板版本]
 * @deprecated
 */
public class CollapsibleTextView extends LinearLayout implements View.OnClickListener {

    /**
     * default text show max lines
     */
    private static final int DEFAULT_MAX_LINE_COUNT = 6;

    //
    private static final int COLLAPSIBLE_STATE_NONE = 0;
    private static final int COLLAPSIBLE_STATE_SHRINKUP = 1;
    private static final int COLLAPSIBLE_STATE_SPREAD = 2;
    public final TextView mDesc;
    private final TextView mDescOp;
    private String str;
    private Context mContext;
    private int mState;
    //默认false
    private boolean flag;

    public CollapsibleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View inflate = inflate(context, R.layout.wanq_collapsible_textview, this);
        mDesc = (TextView) inflate.findViewById(R.id.desc_tv);
        mDescOp = (TextView) inflate.findViewById(R.id.desc_op_tv);
        mDescOp.setOnClickListener(this);


    }

    /**
     * 当View确定本身已经不再适合现有的区域是，该view本身
     * 调用这个方法，要求ParentView重新调用它的OnMeasure
     * onLayout方法来重新设置自己位置
     * 特别的当view的layoutparameter发生改变，并且它的值还没能应用到view上，这时候
     * 适合调用这个方法，
     * 此外有个invalidate：view本身迫使view重画
     */
    public final void setDesc(String str) {
        this.str = str;
        mState = COLLAPSIBLE_STATE_SPREAD;
        mDesc.setText(StringUtils.getEmotionContent(getContext(), mDesc, str));
        requestLayout();


    }

    /**
     * onLayout方法是ViewGroup中子View的布局方法，
     * 用于放置子View的位置。放置子View很简单，
     * 只需在重写onLayout方法，然后获取子View的实例，
     * 调用子View的layout方法实现布局。在实际开发中，
     * 一般要配合onMeasure测量方法一起使用。
     * <p>
     * 父视图的子视图的个数为0，就会执行一次。
     * 否则就会执行多次。因为开始时父试图中是没有子试图的。
     * 但是当你从xml文件中加载子试图或者在java代码中添加子试图时，
     * 父试图的状态会发生变化，这个变化会引起onlayout甚至是onmeasure。
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //此处flag相当重要，onLayout执行完，即设为true，使得只执行一次。
        if (!flag) {
            flag = true;
            /**
             * 文本行数比设定的还小
             //             */
            if (mDesc.getLineCount() <= DEFAULT_MAX_LINE_COUNT) {
                mState = COLLAPSIBLE_STATE_NONE;
                mDescOp.setVisibility(GONE);
                mDesc.setMaxLines(DEFAULT_MAX_LINE_COUNT + 1);

            } else {
                /**
                 * 抛到UI线程
                 */
                post(new InnerRunnable());
            }


        }


    }

    @Override
    public void onClick(View v) {
        flag = false;
        requestLayout();
        mDesc.setEllipsize(null);
    }

    private class InnerRunnable implements Runnable {
        @Override
        public void run() {
            if (mState == COLLAPSIBLE_STATE_SPREAD) {
                mDesc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
                mDescOp.setVisibility(VISIBLE);
                mDescOp.setText("显示全部");
                mDesc.setEllipsize(TextUtils.TruncateAt.END);
                mState = COLLAPSIBLE_STATE_SHRINKUP;
            } else if (mState == COLLAPSIBLE_STATE_SHRINKUP) {
                mDesc.setMaxLines(Integer.MAX_VALUE);
                mDescOp.setVisibility(VISIBLE);
                mDescOp.setText("收起");
                mState = COLLAPSIBLE_STATE_SPREAD;
            }
        }
    }
}
