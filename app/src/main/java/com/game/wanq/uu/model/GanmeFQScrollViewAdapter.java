package com.game.wanq.uu.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.game.wanq.app.R;

import java.util.List;

public class GanmeFQScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDatas;

    public GanmeFQScrollViewAdapter(Context context, List<String> mDatas) {
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
            convertView = mInflater.inflate(R.layout.wanq_index_gamefq_gallery_item, parent, false);
            viewHolder.llayout = convertView.findViewById(R.id.llayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder {
        private LinearLayout llayout;
    }

}
