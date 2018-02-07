package com.game.wanq.uu.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.TUsersCollection;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.RatingBarView;

import java.util.List;

public class UserPLListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TUsersCollection> mDatas;

    public UserPLListAdapter(Context context, List<TUsersCollection> mDatas) {
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
            convertView = mInflater.inflate(R.layout.wanq_user_pllist_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userpltime = (TextView) convertView.findViewById(R.id.userpltime);
            viewHolder.useryxIcon = (ImageView) convertView.findViewById(R.id.useryxIcon);
            viewHolder.useryxname = (TextView) convertView.findViewById(R.id.useryxname);
            viewHolder.useryxfenshu = (RatingBarView) convertView.findViewById(R.id.useryxfenshu);
            viewHolder.userPlneir = (TextView) convertView.findViewById(R.id.userPlneir);
            viewHolder.userPlzan = (TextView) convertView.findViewById(R.id.userPlzan);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (((MyListView) parent).isOnMeasure) {
            return convertView;//如果是onMeasure调用的就立即返回
        }


//        TUsersCollection collection = mDatas.get(position);
//        Glide.with(mContext).load(collection.topimg).priority(Priority.HIGH).into(viewHolder.shoucimage1);
//        viewHolder.shoucganmename.setText(collection.name);
//        viewHolder.shoucpfnum.setText(collection.score + "");
//        viewHolder.shoucpfen.setStarMark(Float.parseFloat(collection.score / 2 + ""));
//        viewHolder.shoucpfen.setMarkOk(false);
        return convertView;
    }

    private class ViewHolder {
        private TextView userpltime, useryxname, userPlneir, userPlzan;
        private ImageView useryxIcon;
        private RatingBarView useryxfenshu;
    }

}
