package com.game.wanq.uu.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.FXHorizontalScrollViewAdapter;
import com.game.wanq.uu.model.FaxianGameListAdapter;
import com.game.wanq.uu.model.bean.TBBqian;
import com.game.wanq.uu.model.bean.TBanner;
import com.game.wanq.uu.model.bean.TGame;
import com.game.wanq.uu.model.bean.Tbiqian;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.SPrefUtils;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.banner.Banner;
import com.game.wanq.uu.view.banner.BannerConfig;
import com.game.wanq.uu.view.banner.ImageLoaderInterface;
import com.game.wanq.uu.view.banner.listener.OnBannerListener;
import com.game.wanq.uu.view.whget.FxHorizontalScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FaxianFragment extends Fragment implements View.OnClickListener, OnBannerListener {

    private Banner mBanner;
    private FxHorizontalScrollView mHorizontalScrollView;
    private List<Tbiqian> mList;
    private List<TGame> tGames;
    private List<TBBqian> tbBqians;
    private List<TBanner> banners;
    private List<String> bannersNAME;
    private List<String> bannersUrl;
    private FXHorizontalScrollViewAdapter mAdapter;
    private LinearLayout titleLLayout, titleRLayout;
    private CircleImageView titleLimage;
    private RecyclerView fxrecyclerView;
    private FaxianGameListAdapter fxGameAdapter;
    private SPrefUtils mSP;
    private int size;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mList != null && mList.size() > 0) {
                        mAdapter = new FXHorizontalScrollViewAdapter(getActivity(), mList);
                        mHorizontalScrollView.initDatas(mAdapter, mList.size());
                        mHorizontalScrollView.setOnItemClickListener(new FxHorizontalScrollView.OnItemClickListener() {
                            @Override
                            public void onClick(View view, int pos) {
                                Intent intent = new Intent(getActivity(), FxCKGDActivity.class);
                                intent.putExtra("bqianPID", mList.get(pos).pid);
                                intent.putExtra("bqianName", mList.get(pos).name);
                                getActivity().startActivity(intent);
                            }
                        });
                        if (mList.size() >= 5) size = 6;
                        size = size - 1;
                        tbBqians = new ArrayList<>();
                        getGanmeList(size);
                    }
                    break;
                case 2:
                    if (size >= 0) {
                        getGanmeList(size);
                    } else {
                        fxGameAdapter = new FaxianGameListAdapter(getActivity(), tbBqians);
                        fxrecyclerView.setAdapter(fxGameAdapter);
                        fxGameAdapter.setItemClickListener(new FaxianGameListAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View view, TBBqian tbBqian) {
                                Intent intent = new Intent(getActivity(), FxCKGDActivity.class);
                                intent.putExtra("bqianPID", tbBqian.pid);
                                intent.putExtra("bqianName", tbBqian.name);
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                    break;
                case 3:
                    mBanner.setImages(bannersUrl).setBannerTitles(bannersNAME)
                            .setBannerStyle(BannerConfig.NOT_INDICATOR)//CIRCLE_INDICATOR_TITLE_INSIDE
                            .setImageLoader(new ImageLoaderInterface<View>() {
                                @Override
                                public void displayImage(Context context, Object path, View imageView) {
                                    Glide.with(context).load((String) path).priority(Priority.HIGH).into((ImageView) imageView);
                                }

                                @Override
                                public View createImageView(Context context) {
                                    return null;
                                }
                            }).start();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_faxian_layout, container, false);
        mSP = SPrefUtils.getInstance(getActivity());
        titleLLayout = (LinearLayout) view.findViewById(R.id.title_faxian).findViewById(R.id.titleLLayout);
        titleLLayout.setOnClickListener(this);
        titleRLayout = (LinearLayout) view.findViewById(R.id.title_faxian).findViewById(R.id.titleRLayout);
        titleRLayout.setOnClickListener(this);
        titleLimage = (CircleImageView) view.findViewById(R.id.title_faxian).findViewById(R.id.titleLimage);
        mBanner = (Banner) view.findViewById(R.id.banner);
        mHorizontalScrollView = (FxHorizontalScrollView) view.findViewById(R.id.id_fxhorizontalScrollView);
        mBanner.setOnBannerListener(this);
        fxrecyclerView = (RecyclerView) view.findViewById(R.id.fxrecyclerView);
        fxrecyclerView.setLayoutManager(new GridLayoutManager(fxrecyclerView.getContext(), 1, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return super.canScrollVertically();
            }
        });
        fxrecyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        String user_id = mSP.getString(mSP.USER_ID, "");
        if (!TextUtils.isEmpty(user_id)) {
            Glide.with(this).load(mSP.getString(mSP.USER_ICON, "")).priority(Priority.HIGH).into(titleLimage);
        }
        getBanner();
        getBqAll();
        return view;
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
        }
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent(getActivity(), GameXqingActivity.class);
        intent.putExtra(Config.DETAIL, banners.get(position).typeObjid);
        startActivity(intent);
    }


    private void getBanner() {
        try {
            RequestManager.getInstance(getActivity()).httpGet(Config.getBannerlist, ParaTran.getInstance(getActivity()).setBanner("2"), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            mBanner.setVisibility(View.GONE);
                            return;
                        }
                        mBanner.setVisibility(View.VISIBLE);
                        banners = new ArrayList<>();
                        bannersNAME = new ArrayList<>();
                        bannersUrl = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("datas");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String pid = object.getString("pid");
                            String image = object.getString("image");
                            int type = object.getInt("type");
                            String typeObjid = object.getString("typeObjid");
                            String typeObjname = object.getString("typeObjname");
                            bannersNAME.add(typeObjname);
                            bannersUrl.add(image);
                            banners.add(new TBanner(pid, image, type, typeObjid, typeObjname));
                        }
                        mHandler.sendEmptyMessage(3);
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

    private void getBqAll() {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getBQlist, null, new ReqCallBack() {
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
                            mList.add(new Tbiqian(pid, name));
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

    private void getGanmeList(final int siz) {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getGameList, ParaTran.getInstance(getActivity()).
                    setGameList(0, 20, mList.get(siz).pid), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            mHandler.sendEmptyMessage(2);
                            return;
                        }
                        tGames = new ArrayList<>();
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
                            tGames.add(new TGame(pid, name, icon, "", score, "", manufacturerName, 0,
                                    "", "", "", "", "", "", 0, 0, 0
                                    , 0, "", "", ""));
                        }
                        tbBqians.add(new TBBqian(mList.get(siz).pid, mList.get(siz).name, tGames));
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
        } finally {
            size = size - 1;
        }
    }
}
