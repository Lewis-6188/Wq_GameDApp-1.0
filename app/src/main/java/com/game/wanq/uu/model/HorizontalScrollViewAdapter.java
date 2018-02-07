package com.game.wanq.uu.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;

import java.util.List;

public class HorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDatas;

    public HorizontalScrollViewAdapter(Context context, List<String> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.wanq_index_gallery_item, parent, false);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_index_gallery_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(mDatas.get(position)).priority(Priority.HIGH).into(viewHolder.mImg);
        return convertView;
    }

    private class ViewHolder {
        ImageView mImg;
    }

}
