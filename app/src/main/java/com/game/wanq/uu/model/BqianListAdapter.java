package com.game.wanq.uu.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.Tbiqian;
import com.game.wanq.uu.view.whget.MyGridView;
import com.game.wanq.uu.view.whget.MyListView;

import java.util.List;

public class BqianListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Tbiqian> mDatas;

    public BqianListAdapter(Context context, List<Tbiqian> mDatas) {
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
            convertView = mInflater.inflate(R.layout.wanq_biaoq_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.bianqianname = (TextView) convertView.findViewById(R.id.bianqianname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (((MyListView) parent).isOnMeasure) {
            return convertView;//如果是onMeasure调用的就立即返回
        }
        Tbiqian tbiqian = mDatas.get(position);
        viewHolder.bianqianname.setText(tbiqian.name);
        return convertView;
    }

    private class ViewHolder {
        private TextView bianqianname;
    }

}
