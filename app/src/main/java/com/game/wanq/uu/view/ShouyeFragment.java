package com.game.wanq.uu.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.bean.THome;
import com.game.wanq.uu.model.mRecycleAdapter;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.ReqDialog;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.SPrefUtils;
import com.game.wanq.uu.utils.UtilsTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ShouyeFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private PtrClassicFrameLayout mPtrFrame;
    private List<THome> mList;
    private mRecycleAdapter mAdapter;
    private RecyclerView recyclerView;
    private ReqDialog mDialog;
    private int iNum;
    private int mFirstY = 0, mCurrentY = 0;
    private LinearLayout titleLLayout, homesousuo, homexiaoxi;
    private CircleImageView titleLimage;
    private SPrefUtils mSP;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.setItemClickListener(new mRecycleAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, String position) {
                            Intent intent = new Intent(getActivity(), GameXqingActivity.class);
                            intent.putExtra(Config.DETAIL, position);
                            startActivity(intent);
                        }
                    });
                    mPtrFrame.refreshComplete();
                    mDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_activity_shouye, container, false);
        mSP = SPrefUtils.getInstance(getActivity());
        titleLLayout = (LinearLayout) view.findViewById(R.id.title_home).findViewById(R.id.titleLLayout);
        homesousuo = (LinearLayout) view.findViewById(R.id.title_home).findViewById(R.id.homesousuo);
        homexiaoxi = (LinearLayout) view.findViewById(R.id.title_home).findViewById(R.id.homexiaoxi);
        titleLimage = (CircleImageView) view.findViewById(R.id.title_home).findViewById(R.id.titleLimage);
        titleLLayout.setOnClickListener(this);
        homesousuo.setOnClickListener(this);
        homexiaoxi.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setOnTouchListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_grid_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, recyclerView, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, recyclerView, header);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                getHomeDatas(++iNum, 5);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mList.clear();
                mList.add(new THome(0, ""));
                getHomeDatas(0, 20);
            }

        });
        mPtrFrame.setResistance(1.7f); // you can also set foot and header separately
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(1000); // you can also set foot and header separately
        mPtrFrame.setPullToRefresh(false);// default is false
        mPtrFrame.setKeepHeaderWhenRefresh(true); // default is true
//        mPtrFrame.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrame.autoRefresh();
//            }
//        }, 1000);
        mDialog = new ReqDialog(getActivity(), R.style._dialog);
        mList = new ArrayList<>();
        mList.add(new THome(0, ""));
        mAdapter = new mRecycleAdapter(getActivity(), mList);
        getHomeDatas(0, 20);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String user_id = mSP.getString(mSP.USER_ID, "");
        if (!TextUtils.isEmpty(user_id)) {
            Glide.with(this).load(mSP.getString(mSP.USER_ICON, "")).priority(Priority.HIGH).into(titleLimage);
        }
    }

    private void getHomeDatas(int paNum, int paSize) {
        try {
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            RequestManager.getInstance(getActivity()).httpGet(Config.getHome, ParaTran.getInstance(getActivity()).setHomeDatas(paNum, paSize), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            mPtrFrame.refreshComplete();
                            mDialog.dismiss();
                            return;
                        }
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("datas"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int showType = object.getInt("showType");
                            String dataList = object.getString("dataList");
                            if (!TextUtils.isEmpty(dataList) && !dataList.equals("[]")) {
                                mList.add(new THome(showType, dataList));
                            }
                        }
                        mHandler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(String result) {
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mDialog.dismiss();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentY = (int) event.getY();
                if (mCurrentY - mFirstY > 0) {//向下滑动
                    MainActivity.tabLinear.setVisibility(View.VISIBLE);
                } else if (mFirstY - mCurrentY > 0) {//向上滑动
                    MainActivity.tabLinear.setVisibility(View.GONE);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleLLayout:
                if (!TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(LoginCGActivity.class);
                } else {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                }
                break;
            case R.id.homexiaoxi:
                if (!TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(WebActivity.class);
                } else {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                }
                break;
            case R.id.homesousuo:
                UtilsTools.getInstance(getActivity()).startClass(SouSuoActivity.class);
                break;
        }
    }
}
