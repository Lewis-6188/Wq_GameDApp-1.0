package com.game.wanq.uu.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.FxckgdListAdapter;
import com.game.wanq.uu.model.GrenFragmentPagerAdapter;
import com.game.wanq.uu.model.bean.TGame;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.PermissionUtils;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis.Liu on 2018/1/22.
 */

public class SouSuoDatasActivity extends Activity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    private String cdpid, cdname;
    private LinearLayout aearchdata_fanhui, aearchdatalinyout;
    private EditText ss_spdatas;
    private List<TGame> tGames;
    private TabLayout sousuotad;
    private ViewPager suosoupager;
    private GrenFragmentPagerAdapter adapter;
    private View view_game, view_cs;
    private MyListView suosougameList;
    private int iNum;
    private FxckgdListAdapter mFxGdAdapter;
    private RefreshLayout swipeLayout;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new GrenFragmentPagerAdapter(SouSuoDatasActivity.this);
                    adapter.addV(view_game, "游戏", tGames.size());
                    adapter.addV(view_cs, "厂商", 0);
                    suosoupager.setAdapter(adapter);
                    suosoupager.setCurrentItem(0);
                    sousuotad.setupWithViewPager(suosoupager);
                    LinearLayout linearLayout = (LinearLayout) sousuotad.getChildAt(0);
                    linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                    linearLayout.setDividerDrawable(ContextCompat.getDrawable(SouSuoDatasActivity.this,
                            R.drawable.layout_divider_vertical));
                    setUpTabBadge();

                    writeExternalStorage();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_sousuo_datas_layout);
        view_game = this.getLayoutInflater().inflate(R.layout.wanq_sousuo_list_layout, null);
        view_cs = this.getLayoutInflater().inflate(R.layout.wanq_sousuocs_list_layout, null);
        Intent intent = getIntent();
        cdpid = intent.getStringExtra("cdpid");
        cdname = intent.getStringExtra("cdname");
        ss_spdatas = (EditText) this.findViewById(R.id.ss_spdatas);
        aearchdatalinyout = (LinearLayout) this.findViewById(R.id.aearchdatalinyout);
        aearchdata_fanhui = (LinearLayout) this.findViewById(R.id.aearchdata_fanhui);
        aearchdata_fanhui.setOnClickListener(this);
        aearchdatalinyout.setOnClickListener(this);
        sousuotad = (TabLayout) this.findViewById(R.id.suosoutad);
        suosoupager = (ViewPager) this.findViewById(R.id.suosoupager);
        suosougameList = (MyListView) view_game.findViewById(R.id.suosougameList);
        swipeLayout = (RefreshLayout) view_game.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.color_bule2, R.color.color_bule, R.color.color_bule2,
                R.color.color_bule3);
        swipeLayout.setOnLoadListener(this);
        swipeLayout.setOnRefreshListener(this);
        tGames = new ArrayList<>();
        mFxGdAdapter = new FxckgdListAdapter(SouSuoDatasActivity.this, tGames);
        getGameList(10);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aearchdata_fanhui:
                finish();
                break;
            case R.id.aearchdatalinyout:
                String text = ss_spdatas.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    cdname = text;
                    iNum = 0;
                    tGames.clear();
                    getGameList(10);
                }
                break;
        }
    }

    public void writeExternalStorage() {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    break;
                case PermissionUtils.CODE_CAMERA:
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    suosougameList.setAdapter(mFxGdAdapter);
                    mFxGdAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    // 设置Tablayout上的标题的角标
    private void setUpTabBadge() {
        for (int i = 0; i < adapter.mView.size(); i++) {
            TabLayout.Tab tab = sousuotad.getTabAt(i);
            View customView = tab.getCustomView(); // 更新Badge前,先remove原来的customView,否则Badge无法更新
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }
            tab.setCustomView(adapter.getTabItemView(i));// 更新CustomView
        }
        sousuotad.getTabAt(sousuotad.getSelectedTabPosition()).getCustomView().setSelected(true);  // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
    }

    private void getGameList(int sizi) {
        try {
            RequestManager.getInstance(this).httpPost(Config.getGameList, ParaTran.getInstance(this).
                    setSSGameList(iNum, sizi, cdpid, cdname), new ReqCallBack() {
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
                            String pkgname = "";
                            try {
                                pkgname = object.getString("pkgname").trim();
                            } catch (Exception e) {
                            }
                            String icon = "";
                            try {
                                icon = object.getString("icon").trim();
                            } catch (Exception e) {
                            }
                            String url = "";
                            try {
                                url = object.getString("url").trim();
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
                            tGames.add(new TGame(pid, name, icon, pkgname, score, "", manufacturerName, 0,
                                    "", "", url, "", "", "", 0, 0, 0
                                    , 0, labelList, false));
                        }
                        mHandler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        mHandler.sendEmptyMessage(1);
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
        swipeLayout.setRefreshing(false);
        iNum = 0;
        tGames.clear();
        getGameList(10);

    }

    @Override
    public void onLoad() {
        swipeLayout.setLoading(false);
        iNum = ++iNum;
        getGameList(5);
    }
}
