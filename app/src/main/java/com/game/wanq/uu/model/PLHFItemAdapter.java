package com.game.wanq.uu.model;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.TComment;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.view.whget.CollapsibleTextView;
import com.game.wanq.uu.view.whget.MyGridView;

import org.json.JSONObject;

import java.util.List;

public class PLHFItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<TComment> mItems;

    public PLHFItemAdapter(Context context, List<TComment> mItems) {
        this.mContext = context;
        this.mItems = mItems;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void notdATSX(List<TComment> mItems) {
        this.mItems = mItems;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemCache mCache;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.wanq_plhf_items_layout, null);
            mCache = new ListItemCache();
            mCache.hfitemname = convertView.findViewById(R.id.hfitemname);
            mCache.hfitemzan = convertView.findViewById(R.id.hfitemzan);
            mCache.hfitemzanimage = convertView.findViewById(R.id.hfitemzanimage);
            mCache.hfitemzantext = convertView.findViewById(R.id.hfitemzantext);
            mCache.hfitemdata = convertView.findViewById(R.id.hfitemdata);
            mCache.hfitemconten = convertView.findViewById(R.id.hfitemconten);
            convertView.setTag(mCache);
        } else {
            mCache = (ListItemCache) convertView.getTag();
        }
        if (((MyGridView) parent).isOnMeasure) {
            return convertView;//如果是onMeasure调用的就立即返回
        }
        // 数据绑定
        if (!mCache.pidold.equals(mItems.get(position).rpid)) {
            mCache.hfitemname.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.colorPrimaryHS) + ">" + mItems.get(position).uidName + "</font>"));
        } else {
            mCache.hfitemname.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.colorPrimaryHS) + ">" + mItems.get(position).uidName + "</font>" +
                    "<font color=" + mContext.getResources().getColor(R.color.colorPrimaryTXTHse) + ">" + "回复" + "</font>" +
                    "<font color=" + mContext.getResources().getColor(R.color.colorPrimaryHS) + ">" + mItems.get(position).rUidName + "</font>"));
        }
        mCache.pidold = mItems.get(position).rpid;
        mCache.hfitemdata.setText(mItems.get(position).time);
        mCache.hfitemconten.setDesc(mItems.get(position).content);
        mCache.hfitemzantext.setText(mItems.get(position).zanCount + "");
        final ListItemCache finalMCache = mCache;
        mCache.hfitemzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mItems.get(position).isZan) {
                    finalMCache.hfitemzanimage.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.zan2));
                    mItems.get(position).zanCount = mItems.get(position).zanCount + 1;
                    mItems.get(position).isZan = true;
                } else {
                    finalMCache.hfitemzanimage.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.zan));
                    mItems.get(position).zanCount = mItems.get(position).zanCount - 1;
                    mItems.get(position).isZan = false;
                }
                finalMCache.hfitemzantext.setText(mItems.get(position).zanCount + "");
                getPLList(mItems.get(position).type, mItems.get(position).pid, "1");
            }
        });

        return convertView;
    }

    private class ListItemCache {
        private TextView hfitemname, hfitemdata;
        private LinearLayout hfitemzan;
        private ImageView hfitemzanimage;
        private TextView hfitemzantext;
        private CollapsibleTextView hfitemconten;
        private String pidold = "", contents = "";
    }

    private void getPLList(int gameType, String typeObjid, String upOfDown) {
        try {
            RequestManager.getInstance(mContext).httpPost(Config.getPLCZ, ParaTran.getInstance(mContext).
                    setPLZC(gameType, typeObjid, upOfDown), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String result) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
