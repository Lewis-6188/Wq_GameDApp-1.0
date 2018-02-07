package com.game.wanq.uu.view;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.PHHorizontalScrollViewAdapter;
import com.game.wanq.uu.model.PhangListAdapter;
import com.game.wanq.uu.model.bean.TGame;
import com.game.wanq.uu.model.bean.TManufacturer;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.SPrefUtils;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.PhHorizontalScrollView;
import com.game.wanq.uu.view.whget.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class PhbangFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    private LinearLayout titleLLayout, titleRLayout;
    private CircleImageView titleLimage;
    private SPrefUtils mSP;
    private int iNum;
    private LinearLayout lintenss;
    private List<TManufacturer> mList;
    private List<TGame> tGames;
    private String manufacturerId, sort;
    private PhHorizontalScrollView phHorizontalView;
    private PHHorizontalScrollViewAdapter phmAdapter;
    private PhangListAdapter mPHBAdapter;
    private MyListView phangListView;
    private TextView xiazaibang, xinzengbang, rewanbang, yuyuebang;
    private RefreshLayout swipeLayout;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    phmAdapter = new PHHorizontalScrollViewAdapter(getActivity(), mList);
                    phHorizontalView.initDatas(phmAdapter, mList.size());
                    phHorizontalView.setOnItemClickListener(new PhHorizontalScrollView.OnItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(View view, int pos) {
                            manufacturerId = mList.get(pos).pid;
                            sort = "download";
                            if (tGames != null) {
                                tGames.clear();
                            }
                            mPHBAdapter.notifyDataSetChanged();
                            view.setBackground(getResources().getDrawable(R.drawable.faxianbqian_background));
                            xiazaibang.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            xinzengbang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                            rewanbang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                            getCSGanmeList(10, "", manufacturerId, sort);

                        }
                    });
                    manufacturerId = mList.get(0).pid;
                    sort = "download";
                    getCSGanmeList(10, "", manufacturerId, sort);
                    break;
                case 2:
                    phangListView.setAdapter(mPHBAdapter);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_phbang_layout, container, false);
        mSP = SPrefUtils.getInstance(getActivity());
        titleLLayout = (LinearLayout) view.findViewById(R.id.title_phbang).findViewById(R.id.titleLLayout);
        titleLLayout.setOnClickListener(this);
        titleRLayout = (LinearLayout) view.findViewById(R.id.title_phbang).findViewById(R.id.titleRLayout);
        titleRLayout.setOnClickListener(this);
        titleLimage = (CircleImageView) view.findViewById(R.id.title_phbang).findViewById(R.id.titleLimage);
        String user_id = mSP.getString(mSP.USER_ID, "");
        if (!TextUtils.isEmpty(user_id)) {
            Glide.with(this).load(mSP.getString(mSP.USER_ICON, "")).priority(Priority.HIGH).into(titleLimage);
        }
        xiazaibang = (TextView) view.findViewById(R.id.xiazaibang);
        xinzengbang = (TextView) view.findViewById(R.id.xinzengbang);
        rewanbang = (TextView) view.findViewById(R.id.rewanbang);
        yuyuebang = (TextView) view.findViewById(R.id.yuyuebang);

        xiazaibang.setOnClickListener(this);
        xinzengbang.setOnClickListener(this);
        rewanbang.setOnClickListener(this);
        yuyuebang.setOnClickListener(this);

        phangListView = (MyListView) view.findViewById(R.id.phangListView);
        phHorizontalView = (PhHorizontalScrollView) view.findViewById(R.id.phHorizontalView);
        lintenss = (LinearLayout) view.findViewById(R.id.lintenss);

        swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.color_bule2, R.color.color_bule, R.color.color_bule2,
                R.color.color_bule3);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);
        tGames = new ArrayList<>();
        mPHBAdapter = new PhangListAdapter(getActivity(), tGames);
        getCSAll();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
            Glide.with(this).load(mSP.getString(mSP.USER_ICON, "")).priority(Priority.HIGH).into(titleLimage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleLLayout:
                SPrefUtils mSP = SPrefUtils.getInstance(getActivity());
                String user_id = mSP.getString(mSP.USER_ID, "");
                if (!TextUtils.isEmpty(user_id)) {
                    Glide.with(this).load(mSP.getString(mSP.USER_ICON, "")).priority(Priority.HIGH).into(titleLimage);
                    UtilsTools.getInstance(getActivity()).startClass(LoginCGActivity.class);
                } else {
                    UtilsTools.getInstance(getActivity()).startClass(LoginActivity.class);
                }
                break;
            case R.id.titleRLayout:
                UtilsTools.getInstance(getActivity()).startClass(SouSuoActivity.class);
                break;
            case R.id.xiazaibang:
                xiazaibang.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                rewanbang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                xinzengbang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                yuyuebang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                sort = "download";
                if (tGames == null) {
                    tGames = new ArrayList<>();
                }
                tGames.clear();
                mPHBAdapter.notifyDataSetChanged();
                getCSGanmeList(10, "", manufacturerId, sort);
                break;
            case R.id.xinzengbang:
                xinzengbang.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                xiazaibang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                rewanbang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                yuyuebang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                sort = "newadd";
                if (tGames == null) {
                    tGames = new ArrayList<>();
                }
                tGames.clear();
                mPHBAdapter.notifyDataSetChanged();
                getCSGanmeList(10, "", manufacturerId, sort);
                break;
            case R.id.rewanbang:
                rewanbang.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                xinzengbang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                xiazaibang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                yuyuebang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                sort = "hot";
                if (tGames == null) {
                    tGames = new ArrayList<>();
                }
                tGames.clear();
                mPHBAdapter.notifyDataSetChanged();
                getCSGanmeList(10, "", manufacturerId, sort);
                break;
            case R.id.yuyuebang:
                yuyuebang.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                xiazaibang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                xinzengbang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                rewanbang.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                sort = "subscribe";
                if (tGames == null) {
                    tGames = new ArrayList<>();
                }
                tGames.clear();
                mPHBAdapter.notifyDataSetChanged();
                getCSGanmeList(10, "", manufacturerId, sort);
                break;
        }
    }

    private void getCSAll() {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getCSlist, ParaTran.getInstance(getActivity()).
                    setCSlist(0, 100), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            return;
                        }
                        mList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("datas");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String pid = object.getString("pid");
                            String name = object.getString("name");
                            String icon = object.getString("icon");
                            Double score = object.getDouble("score");
                            String intro = object.getString("intro");
                            int sort = object.getInt("sort");
                            mList.add(new TManufacturer(pid, name, icon, score, intro, sort));
                        }
                        mHandler.sendEmptyMessage(1);
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

    private void getCSGanmeList(int paSize, String labelId, String manufacturerId, String sort) {
        try {
            RequestManager.getInstance(getActivity()).httpGet(Config.getPHBlist, ParaTran.getInstance(getActivity()).
                    setPHlist(iNum, paSize, labelId, manufacturerId, sort), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            return;
                        }
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("datas"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String pid = object.getString("pid").trim();
                            String name = "";
                            try {
                                name = object.getString("name").trim();
                            } catch (Exception e) {
                            }
                            String icon = "";
                            try {
                                icon = object.getString("icon").trim();
                            } catch (Exception e) {
                            }
                            double score = 0;
                            try {
                                score = object.getDouble("score");
                            } catch (Exception e) {
                            }
                            String manufacturerName = "";
                            try {
                                manufacturerName = object.getString("manufacturerName").trim();
                            } catch (Exception e) {
                            }
                            String labelList = "";
                            try {
                                labelList = object.getString("labelList").trim();
                            } catch (Exception e) {
                            }
                            int subscribe = 1;
                            try {
                                subscribe = object.getInt("subscribe");
                            } catch (Exception e) {
                            }
                            tGames.add(new TGame(pid, name, icon, "", score, "", manufacturerName, 0,
                                    "", "", "", "", "", "", 0, 0, 0
                                    , 0, labelList, subscribe, false));
                        }
                        mHandler.sendEmptyMessage(2);
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

    @Override
    public void onRefresh() {
        iNum = 0;
        tGames.clear();
        getCSGanmeList(10, "", manufacturerId, sort);
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onLoad() {
        iNum = ++iNum;
        getCSGanmeList(5, "", manufacturerId, sort);
        swipeLayout.setRefreshing(false);
    }
}
