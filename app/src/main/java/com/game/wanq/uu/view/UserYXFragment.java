package com.game.wanq.uu.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.GrenFragmentPagerAdapter;
import com.game.wanq.uu.model.ShoucGanmeAdapter;
import com.game.wanq.uu.model.bean.TUsersCollection;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.whget.MyGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis.Liu on 2017/12/27.
 */

public class UserYXFragment extends Fragment implements View.OnClickListener {
    public UserYXFragment newInstance() {
        return new UserYXFragment();
    }

    private TextView wanguoText, shouchangText;
    private MyGridView shoucangGrid, wanguoGrid;
    private LinearLayout wanguo, shouchang;
    private List<TUsersCollection> mCollections;
    private ShoucGanmeAdapter shoucAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    shoucAdapter = new ShoucGanmeAdapter(getActivity(), mCollections);
                    shoucangGrid.setAdapter(shoucAdapter);
                    UtilsTools.getInstance(getActivity()).setListViewHeightBasedOnChildren(shoucangGrid);
                    shoucangGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), GameXqingActivity.class);
                            intent.putExtra(Config.DETAIL, mCollections.get(position).typeObjid);
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_gren_szhiyx_layout, container, false);
        wanguo = (LinearLayout) view.findViewById(R.id.wanguo);
        shouchang = (LinearLayout) view.findViewById(R.id.shouchang);
        wanguoText = (TextView) view.findViewById(R.id.wanguoText);
        shouchangText = (TextView) view.findViewById(R.id.shouchangText);
        wanguoGrid = (MyGridView) view.findViewById(R.id.wanguoGrid);
        shoucangGrid = (MyGridView) view.findViewById(R.id.shoucangGrid);
        wanguo.setOnClickListener(this);
        shouchang.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wanguo:
                wanguoText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                shouchangText.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                wanguoGrid.setVisibility(View.VISIBLE);
                shoucangGrid.setVisibility(View.GONE);
                break;
            case R.id.shouchang:
                wanguoText.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                shouchangText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                shoucangGrid.setVisibility(View.VISIBLE);
                wanguoGrid.setVisibility(View.GONE);
                getCollectionlist(0, 10);
                break;
        }
    }

    private void getCollectionlist(int pageNum, int pageSize) {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getUserCollectionlist, ParaTran.getInstance(getActivity()).
                    setUserCollectionlist(pageNum, pageSize), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            return;
                        }
                        mCollections = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("datas");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String pid = object.getString("pid");
                            String uid = object.getString("uid");
                            int type = object.getInt("type");
                            String typeObjid = object.getString("typeObjid");
                            String time = object.getString("time");
                            String name = object.getString("name");
                            String topimg = object.getString("topimg");
                            Double score = object.getDouble("score");
                            mCollections.add(new TUsersCollection(pid, uid, type, typeObjid, time, name, topimg, score));
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
}
