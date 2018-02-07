package com.game.wanq.uu.model;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FXGameHorizontalScrollViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TGame> mList;
    private final int TYPE = 0xff00;//item Shell
    private MyItemClickListener mItemClickListener;

    public FXGameHorizontalScrollViewAdapter(Context context, List<TGame> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE:
                return new HolderType0(LayoutInflater.from(mContext).inflate(R.layout.wanq_faxian_gameitem, parent, false), mItemClickListener);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderType0) {
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
        holder0.faxiangamename.setText(mList.get(position).name);
        Glide.with(mContext).load(mList.get(position).icon).priority(Priority.HIGH).into(holder0.faxangameimage);
    }


    private class HolderType0 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private ImageView faxangameimage;
        private TextView faxiangamename;

        private HolderType0(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.faxangameimage = (ImageView) itemView.findViewById(R.id.faxangameimage);
            this.faxiangamename = (TextView) itemView.findViewById(R.id.faxiangamename);
            this.mListener = myItemClickListener;
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, mList.get(getPosition()).pid);
            }
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, String pid);
    }

    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
