package com.game.wanq.uu.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.THome;

import java.util.List;

public class GanmeSYScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<THome> mDatas;

    public GanmeSYScrollViewAdapter(Context context, List<THome> mDatas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.wanq_home_twoitem, parent, false);
            convertView.setTag(viewHolder);
            viewHolder.image1 = (ImageView) convertView.findViewById(R.id.image2);
            viewHolder.gameName = (TextView) convertView.findViewById(R.id.gameName);
            viewHolder.intro = (TextView) convertView.findViewById(R.id.intro);
            viewHolder.score = (TextView) convertView.findViewById(R.id.score);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        THome tHome = mDatas.get(position);//override(220, 98).
        Glide.with(mContext).load(tHome.image).placeholder(R.mipmap.jiazai).error(R.mipmap.lodinserr).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(viewHolder.image1);
        viewHolder.gameName.setText(tHome.typeObjname);
        viewHolder.score.setText(tHome.score + "");
        viewHolder.intro.setText(tHome.intro);
        return convertView;
    }

    private class ViewHolder {
        private ImageView image1;
        private TextView gameName, intro, score;
    }

}
