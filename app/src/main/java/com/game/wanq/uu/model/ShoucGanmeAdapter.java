package com.game.wanq.uu.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.TUsersCollection;
import com.game.wanq.uu.view.whget.MyGridView;
import com.game.wanq.uu.view.whget.RatingBarView;

import java.util.List;

public class ShoucGanmeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TUsersCollection> mDatas;

    public ShoucGanmeAdapter(Context context, List<TUsersCollection> mDatas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.wanq_gren_shouc_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.shoucimage1 = (ImageView) convertView.findViewById(R.id.shoucimage1);
            viewHolder.shoucganmename = (TextView) convertView.findViewById(R.id.shoucganmename);
            viewHolder.shoucpfen = (RatingBarView) convertView.findViewById(R.id.shoucpfen);
            viewHolder.shoucpfnum = (TextView) convertView.findViewById(R.id.shoucpfnum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (((MyGridView) parent).isOnMeasure) {
            return convertView;//如果是onMeasure调用的就立即返回
        }
        TUsersCollection collection = mDatas.get(position);
        Glide.with(mContext).load(collection.topimg).priority(Priority.HIGH).into(viewHolder.shoucimage1);
        viewHolder.shoucganmename.setText(collection.name);
        viewHolder.shoucpfnum.setText(collection.score + "");
        viewHolder.shoucpfen.setStarMark(Float.parseFloat(collection.score / 2 + ""));
        viewHolder.shoucpfen.setMarkOk(false);
        return convertView;
    }

    private class ViewHolder {
        private ImageView shoucimage1;
        private TextView shoucganmename, shoucpfnum;
        private RatingBarView shoucpfen;
    }

}
