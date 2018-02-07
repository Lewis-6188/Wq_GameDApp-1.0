package com.game.wanq.uu.model;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.TComment;
import com.game.wanq.uu.model.bean.TGame;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.view.HFActivity;
import com.game.wanq.uu.view.emoticon.StringUtils;
import com.game.wanq.uu.view.whget.CollapsibleTextView;
import com.game.wanq.uu.view.whget.MyGridView;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.RatingBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TbXqItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<TComment> mItems;
    private TGame tGame;

    public TbXqItemAdapter(Context context, List<TComment> mItems, TGame tGame) {
        this.mContext = context;
        this.mItems = mItems;
        this.tGame = tGame;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            mCache = new ListItemCache();
            convertView = inflater.inflate(R.layout.wanq_gamexq_tabxq_item, null);
            mCache.plIicon = convertView.findViewById(R.id.plIicon);
            mCache.plusername = convertView.findViewById(R.id.plusername);
            mCache.mRating = convertView.findViewById(R.id.custom_ratingbar);
            mCache.plcontent = convertView.findViewById(R.id.plcontent);
            mCache.plshebei = convertView.findViewById(R.id.plshebei);
            mCache.plcai = convertView.findViewById(R.id.plcai);
            mCache.caiimag = convertView.findViewById(R.id.caiimag);
            mCache.caitext = convertView.findViewById(R.id.caitext);
            mCache.plzan = convertView.findViewById(R.id.plzan);
            mCache.zanimag = convertView.findViewById(R.id.zanimag);
            mCache.zantext = convertView.findViewById(R.id.zantext);
            mCache.plhuifu = convertView.findViewById(R.id.plhuifu);
            mCache.plhuifutext = convertView.findViewById(R.id.plhuifutext);
            mCache.plLayout = convertView.findViewById(R.id.plLayout);
            mCache.plhfliebiaos = convertView.findViewById(R.id.plhfliebiaos);
            mCache.plquanbuhf = convertView.findViewById(R.id.plquanbuhf);

            convertView.setTag(mCache);
        } else {
            mCache = (ListItemCache) convertView.getTag();
        }
        if (((MyListView) parent).isOnMeasure) {
            return convertView;//如果是onMeasure调用的就立即返回
        }
        // 数据绑定
        Glide.with(mContext).load(mItems.get(position).uidIcon).priority(Priority.HIGH).into(mCache.plIicon);
        mCache.plusername.setText(mItems.get(position).uidName);
        mCache.mRating.setStarMark(Float.parseFloat(mItems.get(position).score / 2 + ""));
        mCache.mRating.setMarkOk(false);
        mCache.plcontent.setDesc(mItems.get(position).content);
        mCache.plshebei.setText(mItems.get(position).phoneType);
        mCache.caitext.setText(mItems.get(position).caiCount + "");
        mCache.zantext.setText(mItems.get(position).zanCount + "");
        String returnComment = mItems.get(position).returnComment;
        if (!TextUtils.isEmpty(returnComment)) {
            try {
                String pidold = "";
                JSONArray jsonArray = new JSONArray(returnComment);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String contents = "";
                    JSONObject object = jsonArray.getJSONObject(i);
                    String pid = object.getString("pid").trim();
                    String rpid = object.getString("rpid").trim();
                    String content = object.getString("content").trim();
                    String uidName = object.getString("uidName").trim();
                    String rUidName = object.getString("rUidName").trim();
                    if (!pidold.equals(rpid)) {
                        contents = "<font color=" + mContext.getResources().getColor(R.color.colorPrimaryHS) + ">" + uidName + "：" + "</font>" +
                                "<font color=" + mContext.getResources().getColor(R.color.colorPrimaryTXTHse) + ">" + content + "<br><br>" + "</font>";
                    } else {
                        contents = "<font color=" + mContext.getResources().getColor(R.color.colorPrimaryHS) + ">" + uidName + "</font>" +
                                "<font color=" + mContext.getResources().getColor(R.color.colorPrimaryTXTHse) + ">" + "回复" + "</font>" +
                                "<font color=" + mContext.getResources().getColor(R.color.colorPrimaryHS) + ">" + rUidName + "：" + "</font>" +
                                "<font color=" + mContext.getResources().getColor(R.color.colorPrimaryTXTHse) + ">" + content + "<br><br>" + "</font>";
                    }
                    mCache.plhfliebiaos.setText(StringUtils.getEmotionContent(mContext, mCache.plhfliebiaos, Html.fromHtml(contents).toString()));
                    pidold = rpid;
                }
                mCache.plhuifutext.setText((jsonArray.length()) + "");
                mCache.plquanbuhf.setText("全部" + (jsonArray.length()) + "条回复");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final ListItemCache finalMCache = mCache;
        mCache.plcai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mItems.get(position).isCai) {
                    finalMCache.caiimag.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.cai1));
                    mItems.get(position).caiCount = mItems.get(position).caiCount + 1;
                    mItems.get(position).isCai = true;

                } else {
                    finalMCache.caiimag.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.cai2));
                    mItems.get(position).caiCount = mItems.get(position).caiCount - 1;
                    mItems.get(position).isCai = false;
                }
                TComment tComment = mItems.get(position);
                getPLList(tComment.type, tComment.pid, "0");
                notifyDataSetChanged();


            }
        });
        mCache.plzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mItems.get(position).isZan) {
                    finalMCache.zanimag.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.zan2));
                    mItems.get(position).zanCount = mItems.get(position).zanCount + 1;
                    mItems.get(position).isZan = true;
                } else {
                    finalMCache.zanimag.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.zan));
                    mItems.get(position).zanCount = mItems.get(position).zanCount - 1;
                    mItems.get(position).isZan = false;
                }
                TComment tComment = mItems.get(position);
                getPLList(tComment.type, tComment.pid, "1");
                notifyDataSetChanged();
            }
        });
        mCache.plhuifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HFActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hfobject", mItems.get(position));
                bundle.putSerializable("gameobject", tGame);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });
        mCache.plLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HFActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hfobject", mItems.get(position));
                bundle.putSerializable("gameobject", tGame);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });
        mCache.plquanbuhf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HFActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hfobject", mItems.get(position));
                bundle.putSerializable("gameobject", tGame);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }

    private class ListItemCache {
        private CircleImageView plIicon;
        private RatingBarView mRating;
        private TextView plusername;
        private CollapsibleTextView plcontent;
        private TextView plshebei;
        private LinearLayout plcai, plzan, plhuifu, plLayout;
        private ImageView caiimag, zanimag;
        private TextView caitext, zantext, plhuifutext, plquanbuhf, plhfliebiaos;
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
