package com.game.wanq.uu.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.FxckgdListAdapter;
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
 * Created by Lewis.Liu on 2018/1/21.
 */

public class FxCKGDActivity extends Activity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private LinearLayout fxfanhui;
    private TextView fxtext;
    private String tbPid, tbName;
    private int iNum;
    private List<TGame> tGames;
    private MyListView fxckgdList;
    private FxckgdListAdapter mFxGdAdapter;
    private RefreshLayout swipeLayout;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    writeExternalStorage();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_fx_ckgd_layout);
        Intent intent = getIntent();
        tbPid = intent.getStringExtra("bqianPID");
        tbName = intent.getStringExtra("bqianName");
        fxtext = (TextView) this.findViewById(R.id.fxtext);
        fxfanhui = (LinearLayout) this.findViewById(R.id.fxfanhui);
        fxfanhui.setOnClickListener(this);
        fxtext.setText(tbName);
        fxckgdList = (MyListView) this.findViewById(R.id.fxckgdList);
        swipeLayout = (RefreshLayout) this.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.color_bule2, R.color.color_bule, R.color.color_bule2,
                R.color.color_bule3);
        swipeLayout.setOnLoadListener(this);
        swipeLayout.setOnRefreshListener(this);
        tGames = new ArrayList<>();
        mFxGdAdapter = new FxckgdListAdapter(FxCKGDActivity.this, tGames);
        getGanmeList(10);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fxfanhui:
                finish();
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
                    fxckgdList.setAdapter(mFxGdAdapter);
                    mFxGdAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void getGanmeList(int paSize) {
        try {
            RequestManager.getInstance(this).httpPost(Config.getGameList, ParaTran.getInstance(this).
                    setGameList(iNum, paSize, tbPid), new ReqCallBack() {
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
        swipeLayout.setRefreshing(false);
        getGanmeList(10);
    }

    @Override
    public void onLoad() {
        iNum = ++iNum;
        getGanmeList(10);
        swipeLayout.setRefreshing(false);
    }
}
