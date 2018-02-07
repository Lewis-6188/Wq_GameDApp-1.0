package com.game.wanq.uu.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.TBBqian;
import com.game.wanq.uu.view.GameXqingActivity;

import java.util.List;

public class FaxianGameListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TBBqian> mList;
    private final int TYPE = 0xff00;//item Shell
    private MyItemClickListener mItemClickListener;

    public FaxianGameListAdapter(Context context, List<TBBqian> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE:
                return new HolderType0(LayoutInflater.from(mContext).inflate(R.layout.wanq_faxian_item, parent, false), mItemClickListener);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FaxianGameListAdapter.HolderType0) {
            bindType0((HolderType0) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE;
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
                }
            });
        }
    }

    private void bindType0(HolderType0 holder0, int position) {
        TBBqian tbiqian = mList.get(position);
        holder0.yxbiaoqian.setText(tbiqian.name);
        holder0.fxitemrecyclerView.setLayoutManager(new GridLayoutManager(holder0.fxitemrecyclerView.getContext(), tbiqian.tGames.size(), GridLayoutManager.HORIZONTAL, false));
        holder0.fxitemrecyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        holder0.fxGameAdapter = new FXGameHorizontalScrollViewAdapter(mContext, tbiqian.tGames);
        holder0.fxitemrecyclerView.setAdapter(holder0.fxGameAdapter);
        holder0.fxGameAdapter.setItemClickListener(new FXGameHorizontalScrollViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, String pid) {
                Intent intent = new Intent(mContext, GameXqingActivity.class);
                intent.putExtra(Config.DETAIL, pid);
                mContext.startActivity(intent);
            }
        });
    }


    private class HolderType0 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView yxbiaoqian;
        private LinearLayout yxchakangd;
        private RecyclerView fxitemrecyclerView;
        private FXGameHorizontalScrollViewAdapter fxGameAdapter;

        private HolderType0(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.yxbiaoqian = (TextView) itemView.findViewById(R.id.yxbiaoqian);
            this.yxchakangd = (LinearLayout) itemView.findViewById(R.id.yxchakangd);
            this.fxitemrecyclerView = (RecyclerView) itemView.findViewById(R.id.fxitemrecyclerView);
            this.mListener = myItemClickListener;
            this.yxchakangd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, mList.get(getPosition()));
            }
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, TBBqian tbBqian);
    }

    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
