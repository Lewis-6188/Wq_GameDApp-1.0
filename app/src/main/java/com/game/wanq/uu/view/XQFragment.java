package com.game.wanq.uu.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.BqHorizontalScrollViewAdapter;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.GanmeScrollViewAdapter;
import com.game.wanq.uu.model.HorizontalScrollViewAdapter;
import com.game.wanq.uu.model.TbXqItemAdapter;
import com.game.wanq.uu.model.bean.TComment;
import com.game.wanq.uu.model.bean.TGame;
import com.game.wanq.uu.model.bean.Tbiqian;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.whget.CollapsibleTextView;
import com.game.wanq.uu.view.whget.GameScrollView;
import com.game.wanq.uu.view.whget.MyHorizontalScrollView;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.bqHorizontalScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lewis.Liu on 2017/12/27.
 */

public class XQFragment extends Fragment implements MyHorizontalScrollView.OnItemClickListener, View.OnClickListener {

    public XQFragment newInstance() {
        return new XQFragment();
    }

    private HorizontalScrollViewAdapter mAdapter;
    private GanmeScrollViewAdapter ganmeScrollViewAdapter;
    private MyHorizontalScrollView mHorizontalScrollView;
    private bqHorizontalScrollView bqHorizontalView;
    private BqHorizontalScrollViewAdapter mBQAdapter;
    private GameScrollView id_gameScrollView;
    private CollapsibleTextView expTv1, expTv2;
    private MyListView plListView;
    private TbXqItemAdapter tbXqItemAdapter;
    private TextView xiazainum, yxbanben, yxdaxiao, yxgengxinriq, yxcs;
    private List<String> mDatas;
    private List<TGame> tGames;
    private List<Tbiqian> tbiqians;
    private TextView pltext;
    private LinearLayout plLayout, xiangqtousu;
    private List<TComment> tComments;
    private LinearLayout biaoqiangengduo;
    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ganmeScrollViewAdapter = new GanmeScrollViewAdapter(getActivity(), tGames);
                    id_gameScrollView.initDatas(ganmeScrollViewAdapter, tGames.size());
                    id_gameScrollView.setOnItemClickListener(new GameScrollView.OnItemClickListener() {
                        @Override
                        public void onClickGame(View view, int pos) {
                            Intent intent = new Intent(getActivity(), GameXqingActivity.class);
                            intent.putExtra(Config.DETAIL, tGames.get(pos).pid);
                            startActivity(intent);

                        }
                    });
                    break;
                case 2:
                    mBQAdapter = new BqHorizontalScrollViewAdapter(getActivity(), tbiqians);
                    bqHorizontalView.initDatas(mBQAdapter, tbiqians.size());
                    bqHorizontalView.setOnItemClickListener(new bqHorizontalScrollView.OnItemClickListener() {
                        @Override
                        public void onClick(View view, int pos) {
                            Intent intent = new Intent(getActivity(), FxCKGDActivity.class);
                            intent.putExtra("bqianPID", tbiqians.get(pos).pid);
                            intent.putExtra("bqianName", tbiqians.get(pos).name);
                            getActivity().startActivity(intent);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_gamexq_tabxq, container, false);
        expTv1 = (CollapsibleTextView) view.findViewById(R.id.expand_text_view);
        expTv2 = (CollapsibleTextView) view.findViewById(R.id.expand_text_view1);
        mHorizontalScrollView = (MyHorizontalScrollView) view.findViewById(R.id.id_horizontalScrollView);
        bqHorizontalView = (bqHorizontalScrollView) view.findViewById(R.id.bqHorizontalView);
        id_gameScrollView = (GameScrollView) view.findViewById(R.id.id_gameScrollView);
        biaoqiangengduo = (LinearLayout) view.findViewById(R.id.biaoqiangengduo);
        xiangqtousu = (LinearLayout) view.findViewById(R.id.xiangqtousu);
        xiazainum = (TextView) view.findViewById(R.id.xiazainum);
        yxbanben = (TextView) view.findViewById(R.id.yxbanben);
        yxdaxiao = (TextView) view.findViewById(R.id.yxdaxiao);
        yxgengxinriq = (TextView) view.findViewById(R.id.yxgengxinriq);
        yxcs = (TextView) view.findViewById(R.id.yxcs);
        xiangqtousu.setOnClickListener(this);
        plLayout = (LinearLayout) view.findViewById(R.id.plLayout);
        pltext = (TextView) view.findViewById(R.id.pltext);
        biaoqiangengduo.setOnClickListener(this);
        if (GameXqingActivity.tGame != null) {
            getBqList();
            plLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GameXqingActivity.tGame.stagesNum != 0) {
                        GameXqingActivity.pager.setCurrentItem(3);
                    } else {
                        GameXqingActivity.pager.setCurrentItem(2);
                    }
                }
            });

            plListView = (MyListView) view.findViewById(R.id.plListView);
            expTv1.setDesc(GameXqingActivity.tGame.intro);
            expTv2.setDesc(GameXqingActivity.tGame.content);
            xiazainum.setText(GameXqingActivity.tGame.downloadNum + "次下载");
            yxbanben.setText(GameXqingActivity.tGame.version);
            yxdaxiao.setText(GameXqingActivity.tGame.size);
            yxgengxinriq.setText(GameXqingActivity.tGame.updatetime);
            yxcs.setText(GameXqingActivity.tGame.manufacturerName);

            String images = GameXqingActivity.tGame.detailimg;
            if (!TextUtils.isEmpty(images)) {
                mDatas = new ArrayList<>();
                String[] imas = images.split(",");
                if (imas.length != 1) {
                    for (int i = 0; i < imas.length; i++) {
                        mDatas.add(imas[i]);
                    }
                } else {
                    mDatas.add(images);
                }
                mAdapter = new HorizontalScrollViewAdapter(getActivity(), mDatas);
                mHorizontalScrollView.initDatas(mAdapter, mDatas.size());
                mHorizontalScrollView.setOnItemClickListener(this);
            }
        }
        if (GameXqingActivity.tComments != null) {
            Log.i("6188", "---评论信息数据->>" + GameXqingActivity.tComments.size());
            pltext.setText("(" + (GameXqingActivity.tComments.size()) + ")");
            tbXqItemAdapter = new TbXqItemAdapter(getActivity(), GameXqingActivity.tComments, GameXqingActivity.tGame);
            plListView.setAdapter(tbXqItemAdapter);
            getGameList();
        }
        return view;
    }

    @Override
    public void onClick(View view, int pos) {
    }

    private void getGameList() {
        try {
            List<String> pidList = new ArrayList<>();
            String labelID = GameXqingActivity.tGame.labelList;
            if (!TextUtils.isEmpty(labelID)) {
                try {
                    JSONArray jsonArray = new JSONArray(labelID);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String pid = jsonObject.getString("pid");
                        String name = jsonObject.getString("name");
                        pidList.add(pid);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RequestManager.getInstance(getActivity()).httpPost(Config.getGameList, ParaTran.getInstance(getActivity()).
                    setGameList(0, 10, pidList.get(new Random().nextInt(pidList.size()))), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
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

    private void getBqList() {
        try {
            RequestManager.getInstance(getActivity())
                    .httpGet(Config.getfindUserLabel, ParaTran.getInstance(getActivity()).setUserLabel(GameXqingActivity.tGame.pid),
                            new ReqCallBack() {
                                @Override
                                public void onReqSuccess(String result) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        int resultType = jsonObject.getInt("result");
                                        if (resultType == 1) {
                                            return;
                                        }
                                        tbiqians = new ArrayList<>();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.biaoqiangengduo:
                startActivity(new Intent(getActivity(), BqianListActivity.class));
                break;
            case R.id.xiangqtousu:
                UtilsTools.getInstance(getActivity()).startClass(TShuActivity.class);
                break;
        }
    }
}
