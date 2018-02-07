package com.game.wanq.uu.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.TGame;

import java.util.List;

public class GanmeScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TGame> mDatas;

    public GanmeScrollViewAdapter(Context context, List<TGame> mDatas) {
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
            convertView = mInflater.inflate(R.layout.wanq_index_game_gallery_item, parent, false);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_index_game_gallery_item_image);
            viewHolder.yxxname = (TextView) convertView.findViewById(R.id.yxxname);
            viewHolder.yxleixing = (TextView) convertView.findViewById(R.id.yxleixing);
            viewHolder.yxpingfen = (TextView) convertView.findViewById(R.id.yxpingfen);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(mDatas.get(position).icon).priority(Priority.HIGH).into(viewHolder.mImg);
        viewHolder.yxxname.setText(mDatas.get(position).name);
        viewHolder.yxleixing.setText(mDatas.get(position).manufacturerName);
        viewHolder.yxpingfen.setText(mDatas.get(position).score + "");
        return convertView;
    }

    private class ViewHolder {
        private ImageView mImg;
        private TextView yxxname, yxleixing, yxpingfen;
    }

}
