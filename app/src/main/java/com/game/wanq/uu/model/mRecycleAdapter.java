package com.game.wanq.uu.model;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.THome;
import com.game.wanq.uu.view.whget.GameSYScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis.Liu on 2017/12/21.
 */

public class mRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<THome> mList;
    private final int TYPE_ITEM_ = 0xff00;//item Shell
    private final int TYPE_ITEM_ONE = 0xff01;//item 一组
    private final int TYPE_ITEM_TWO = 0xff02;//item 二组
    private MyItemClickListener mItemClickListener;
    private LayoutInflater inflater;

    public mRecycleAdapter(Context context, List<THome> mList) {
        this.mContext = context;
        this.mList = mList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM_:
                return new HolderType0(inflater.inflate(R.layout.wanq_home_ooitem, parent, false));
            case TYPE_ITEM_ONE:
                return new HolderType1(inflater.inflate(R.layout.wanq_home_oneitem, parent, false), mItemClickListener);
            case TYPE_ITEM_TWO:
                return new HolderType2(inflater.inflate(R.layout.wanq_home_two_layout, parent, false), mItemClickListener);
            // return new HolderType2(inflater.inflate(R.layout.wanq_home_twoitem, parent, false), mItemClickListener);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        if (holder instanceof HolderType0) {
            bindType0((HolderType0) holder, position);
        } else if (holder instanceof HolderType1) {
            bindType1((HolderType1) holder, position);
        } else if (holder instanceof HolderType2) {
            bindType2((HolderType2) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int showType = mList.get(position).showType;
        if (showType == Config.ooBaner) {
            return TYPE_ITEM_;
        } else if (showType == Config.oneItem) {
            return TYPE_ITEM_ONE;
        } else if (showType == Config.twoItem) {
            return TYPE_ITEM_TWO;
        } else {
            return TYPE_ITEM_ONE;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return gridManager.getSpanCount();
//                    int type = getItemViewType(position);
//                    switch (type) {
//                        case TYPE_ITEM_:
//                        case TYPE_ITEM_ONE:
//                            return gridManager.getSpanCount();
//                        case TYPE_ITEM_TWO:
//                            return 3;
//                        default:
//                            return gridManager.getSpanCount();
//                    }
                }
            });
        }
    }

    private void bindType0(HolderType0 holder0, int position) {
    }

    private void bindType1(HolderType1 holder1, int position) {
        THome tHome = null;
        List<String> gameID = new ArrayList<>();
        try {
            String datas = mList.get(position).datas;
            JSONArray jsonArray = new JSONArray(datas);
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object1 = jsonArray.getJSONObject(j);
                int type = object1.getInt("type");
                int showType = object1.getInt("showType");
                String typeObjid = object1.getString("typeObjid").trim();
                String typeObjname = object1.getString("typeObjname").trim();
                String image = object1.getString("image").trim();
                String tips1 = object1.getString("tips1").trim();
                String tips2 = object1.getString("tips2").trim();
                String tips3 = object1.getString("tips3").trim();
                Double score = object1.getDouble("score");
                String intro = object1.getString("intro").trim();
                tHome = new THome(type, typeObjid, typeObjname, showType, image, tips1, tips2, tips3, score, intro);
                gameID.add(typeObjid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tHome != null) {
            Glide.with(mContext).load(tHome.image).placeholder(R.mipmap.jiazai).error(R.mipmap.lodinserr).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(holder1.image1);
            holder1.gameName.setText(tHome.typeObjname);
            holder1.intro.setText(tHome.intro);
            holder1.tips2.setText(tHome.tips2);
            if (!TextUtils.isEmpty(tHome.tips3)) {
                holder1.imetitle.setVisibility(View.VISIBLE);
            } else {
                holder1.imetitle.setVisibility(View.GONE);
            }
            holder1.gameId = gameID;
        }
    }

    private void bindType2(HolderType2 holder2, int position) {
        try {
            List<THome> lbels = new ArrayList<>();
            List<String> gameID = new ArrayList<>();
            GanmeSYScrollViewAdapter mAdapter = new GanmeSYScrollViewAdapter(mContext, lbels);
            String datas = mList.get(position).datas;
            JSONArray jsonArray = new JSONArray(datas);
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object1 = jsonArray.getJSONObject(j);
                int type = object1.getInt("type");
                int showType = object1.getInt("showType");
                String typeObjid = object1.getString("typeObjid").trim();
                String typeObjname = object1.getString("typeObjname").trim();
                String image = object1.getString("image").trim();
                String tips1 = object1.getString("tips1").trim();
                String tips2 = object1.getString("tips2").trim();
                String tips3 = object1.getString("tips3").trim();
                Double score = object1.getDouble("score");
                String intro = object1.getString("intro").trim();
                lbels.add(new THome(type, typeObjid, typeObjname, showType, image, tips1, tips2, tips3, score, intro));
                holder2.gameId.add(typeObjid);
            }
            holder2.id_gamesyScrollView.initDatas(mAdapter, lbels.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HolderType0 extends RecyclerView.ViewHolder {
        private HolderType0(View itemView) {
            super(itemView);
        }
    }

    private class HolderType1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private ImageView image1;
        private TextView gameName, intro, tips2;
        private LinearLayout imetitle;
        private List<String> gameId;

        private HolderType1(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.imetitle = (LinearLayout) itemView.findViewById(R.id.imetitle);
            this.image1 = (ImageView) itemView.findViewById(R.id.image1);
            this.gameName = (TextView) itemView.findViewById(R.id.gameName);
            this.intro = (TextView) itemView.findViewById(R.id.intro);
            this.tips2 = (TextView) itemView.findViewById(R.id.tips2);
            this.mListener = myItemClickListener;
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                if (gameId != null)
                    mListener.onItemClick(v, gameId.get(0));
            }
        }
    }


    private class HolderType2 extends RecyclerView.ViewHolder implements GameSYScrollView.OnItemClickListener {
        private MyItemClickListener mListener;
        private GameSYScrollView id_gamesyScrollView;
        private List<String> gameId = new ArrayList<>();

        private HolderType2(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.id_gamesyScrollView = (GameSYScrollView) itemView.findViewById(R.id.id_gamesyScrollView);
            this.mListener = myItemClickListener;
            this.id_gamesyScrollView.setOnItemClickListener(this);
        }

        @Override
        public void onClickSYGame(View view, int position) {
            if (gameId != null)
                mListener.onItemClick(view, gameId.get(position));
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, String position);
    }

    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}


