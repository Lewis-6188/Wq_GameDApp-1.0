package com.game.wanq.uu.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.Tbiqian;

import java.util.List;

public class FXCKGDHorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Tbiqian> mDatas;

    public FXCKGDHorizontalScrollViewAdapter(Context context, List<Tbiqian> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.wanq_fxianckgd_index_gallery_item, parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.id_fxhorizontalText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(mDatas.get(position).name);
        return convertView;
    }

    private class ViewHolder {
        TextView text;
    }

}
