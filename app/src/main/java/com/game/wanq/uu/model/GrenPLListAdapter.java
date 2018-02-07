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
import com.game.wanq.uu.model.bean.TGRComment;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.RatingBarView;

import java.util.List;

public class GrenPLListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TGRComment> tComments;

    public GrenPLListAdapter(Context context, List<TGRComment> tComments) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.tComments = tComments;
    }

    @Override
    public int getCount() {
        return tComments != null ? tComments.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return tComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.wanq_gren_pl_items_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.grenplicon = (ImageView) convertView.findViewById(R.id.grenplicon);
            viewHolder.grenplname = (TextView) convertView.findViewById(R.id.grenplname);
            viewHolder.grplbarview = (RatingBarView) convertView.findViewById(R.id.grplbarview);
            viewHolder.grenpltext = (TextView) convertView.findViewById(R.id.grenpltext);
            viewHolder.grentitmeOld = (TextView) convertView.findViewById(R.id.grentitmeOld);
            viewHolder.grenzan = (TextView) convertView.findViewById(R.id.grenzan);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (((MyListView) parent).isOnMeasure) {
            return convertView;//如果是onMeasure调用的就立即返回
        }

        final TGRComment tgrComment = tComments.get(position);
        Glide.with(mContext).load(tgrComment.typeObjicon).priority(Priority.HIGH).into(viewHolder.grenplicon);
        viewHolder.grenplname.setText(tgrComment.typeObjname);
        viewHolder.grplbarview.setStarMark(Float.parseFloat(tgrComment.score / 2 + ""));
        viewHolder.grplbarview.setMarkOk(false);
        viewHolder.grenpltext.setText(tgrComment.content);
        viewHolder.grentitmeOld.setText(UtilsTools.getInstance(mContext).CalculateTime(tgrComment.time));
        viewHolder.grenzan.setText(tgrComment.zanCount + "");
        return convertView;
    }

    private class ViewHolder {
        private ImageView grenplicon;
        private TextView grenplname, grenpltext, grentitmeOld, grenzan;
        private RatingBarView grplbarview;
    }

}
