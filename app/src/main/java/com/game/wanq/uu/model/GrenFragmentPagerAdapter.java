package com.game.wanq.uu.model;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.view.whget.BadgeView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lewis.Liu on 2018/1/8.
 */

public class GrenFragmentPagerAdapter extends PagerAdapter {
    private Context mContext;
    public List<View> mView;
    public List<String> mTitles;
    public List<Integer> mBadgeCountList;

    public GrenFragmentPagerAdapter(Context context) {
        this.mContext = context;
        this.mView = new LinkedList<>();
        this.mTitles = new LinkedList<>();
        this.mBadgeCountList = new LinkedList<>();
    }

    public void addV(View view, String title) {
        mView.add(view);
        mTitles.add(title);
    }

    public void addV(View view, String title, int size) {
        mView.add(view);
        mTitles.add(title);
        mBadgeCountList.add(size);
    }

    @Override
    public int getCount() {
        return mView.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(mView.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mView.get(position).setLayoutParams(params);
        container.addView(mView.get(position), 0);
        return mView.get(position);
    }

    //       根据角标获取标题item的布局文件
    public View getTabItemView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wanq_tab_layout_item, null);  // 标题布局
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTitles.get(position));  // 设置标题内容
        View target = view.findViewById(R.id.badgeview_target);//右上角数字标记
        BadgeView badgeView = new BadgeView(mContext);
        badgeView.setTargetView(target);
        badgeView.setEllipsize(TextUtils.TruncateAt.END);
        badgeView.setSingleLine(true);
        badgeView.setBadgeMargin(0, 6, 6, 0);
        badgeView.setTextSize(10);
        badgeView.setText(Integer.toString(mBadgeCountList.get(position)));
        if (mBadgeCountList.get(position) == 0) {
            target.setVisibility(View.GONE);
        } else {
            target.setVisibility(View.VISIBLE);

        }
        return view;
    }
}
