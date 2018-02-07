package com.game.wanq.uu.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.BqHorizontalScrollViewAdapter;
import com.game.wanq.uu.model.BqianListAdapter;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.bean.Tbiqian;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.bqHorizontalScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis.Liu on 2018/1/26.
 */

public class BqianListActivity extends Activity implements View.OnClickListener {
    private LinearLayout biaoqfanhui, biaoqtousu;
    private TextView biaoqtext;
    private LinearLayout bqianTjia;
    private MyListView biaoqlist;
    private List<Tbiqian> tbiqians, yxbqian;
    private BqianListAdapter bqianListAdapter;
    private ImageView biaoqianbji, tijianbianqian;
    private bqHorizontalScrollView bqianHorizontalView;
    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    bqianListAdapter = new BqianListAdapter(BqianListActivity.this, tbiqians);
                    biaoqlist.setAdapter(bqianListAdapter);
                    biaoqlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(BqianListActivity.this, FxCKGDActivity.class);
                            intent.putExtra("bqianPID", tbiqians.get(position).pid);
                            intent.putExtra("bqianName", tbiqians.get(position).name);
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    if (yxbqian != null && yxbqian.size() > 0) {
                        tijianbianqian.setVisibility(View.GONE);
                        bqianTjia.setVisibility(View.VISIBLE);
                        BqHorizontalScrollViewAdapter mBQAdapter = new BqHorizontalScrollViewAdapter(BqianListActivity.this, tbiqians);
                        bqianHorizontalView.initDatas(mBQAdapter, tbiqians.size());
                        bqianHorizontalView.setOnItemClickListener(new bqHorizontalScrollView.OnItemClickListener() {
                            @Override
                            public void onClick(View view, int pos) {
                                Intent intent = new Intent(BqianListActivity.this, FxCKGDActivity.class);
                                intent.putExtra("bqianPID", tbiqians.get(pos).pid);
                                intent.putExtra("bqianName", tbiqians.get(pos).name);
                                startActivity(intent);
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_bqianlist_layout);
        biaoqtousu = (LinearLayout) this.findViewById(R.id.biaoqtousu);
        biaoqfanhui = (LinearLayout) this.findViewById(R.id.biaoqfanhui);
        biaoqtext = (TextView) this.findViewById(R.id.biaoqtext);
        biaoqlist = (MyListView) this.findViewById(R.id.biaoqlist);
        biaoqianbji = (ImageView) this.findViewById(R.id.biaoqianbji);
        tijianbianqian = (ImageView) this.findViewById(R.id.tijianbianqian);
        bqianHorizontalView = (bqHorizontalScrollView) this.findViewById(R.id.bqianHorizontalView);
        bqianTjia = (LinearLayout) this.findViewById(R.id.bqianTjia);
        biaoqtext.setText(GameXqingActivity.tGame.name);
        biaoqfanhui.setOnClickListener(this);
        biaoqtousu.setOnClickListener(this);
        biaoqianbji.setOnClickListener(this);
        tijianbianqian.setOnClickListener(this);
        tbiqians = new ArrayList<>();
        yxbqian = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBqList();
        getBqYxzList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.biaoqfanhui:
                finish();
                break;
            case R.id.biaoqtousu:

                break;
            case R.id.biaoqianbji:
                startActivity(new Intent(this, TianJActivity.class));
                break;
            case R.id.tijianbianqian:
                startActivity(new Intent(this, TianJActivity.class));
                break;
        }
    }


    private void getBqList() {
        try {
            RequestManager.getInstance(this)
                    .httpGet(Config.getBqList, null,
                            new ReqCallBack() {
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
                                            tbiqians.add(new Tbiqian(pid, name));
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

    private void getBqYxzList() {
        try {
            RequestManager.getInstance(this)
                    .httpGet(Config.getfindUserLabel, ParaTran.getInstance(this).setUserLabel(GameXqingActivity.tGame.pid),
                            new ReqCallBack() {
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
                                            yxbqian.add(new Tbiqian(pid, name));
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
}
