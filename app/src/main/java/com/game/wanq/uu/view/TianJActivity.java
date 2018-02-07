package com.game.wanq.uu.view;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.bean.Tbiqian;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.DrawableUtils;
import com.game.wanq.uu.view.emoticon.DisplayUtils;
import com.game.wanq.uu.view.whget.BQFlowLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis.Liu on 2018/1/26.
 */

public class TianJActivity extends Activity implements View.OnClickListener {
    private LinearLayout bqfanhui;
    private TextView bqtext;
    private BQFlowLayout gyxbqlayout, wdbqlayout;
    private List<Tbiqian> tbiqians, tbiqians1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    displayUI();
                    break;
                case 2:
                    displayUI1();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_tianjbq_layout);
        bqfanhui = (LinearLayout) this.findViewById(R.id.bqfanhui);
        bqfanhui.setOnClickListener(this);
        bqtext = (TextView) this.findViewById(R.id.bqtext);
        bqtext.setText(GameXqingActivity.tGame.name);
        gyxbqlayout = (BQFlowLayout) this.findViewById(R.id.gyxbqlayout);
        wdbqlayout = (BQFlowLayout) this.findViewById(R.id.wdbqlayout);
        gyxbqlayout.setSpace(DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5));
        gyxbqlayout.setPadding(DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5),
                DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5));
        wdbqlayout.setSpace(DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5));
        wdbqlayout.setPadding(DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5),
                DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5));
        tbiqians = new ArrayList<>();
        tbiqians1 = new ArrayList<>();


        getBqList();
        getBqList1();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bqfanhui:
                finish();
                break;
        }
    }

    private void getBqList() {
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

    private void getBqList1() {
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
                                            tbiqians1.add(new Tbiqian(pid, name));
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

    private void getTjianBQ(String labelid) {
        try {
            RequestManager.getInstance(this)
                    .httpGet(Config.getRelLabel, ParaTran.getInstance(this).setRelLabel(GameXqingActivity.tGame.pid, labelid),
                            new ReqCallBack() {
                                @Override
                                public void onReqSuccess(String result) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        int resultType = jsonObject.getInt("result");
                                        if (resultType == 1) {
                                            return;
                                        }
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

    private void getQXBQ(String labelid) {
        try {
            RequestManager.getInstance(this)
                    .httpGet(Config.getDelLabel, ParaTran.getInstance(this).setRelLabel(GameXqingActivity.tGame.pid, labelid),
                            new ReqCallBack() {
                                @Override
                                public void onReqSuccess(String result) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        int resultType = jsonObject.getInt("result");
                                        if (resultType == 1) {
                                            return;
                                        }
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

    private void displayUI() {
        for (int i = 0; i < tbiqians.size(); i++) {
            final Tbiqian tbiqian = tbiqians.get(i);
            final String data = tbiqians.get(i).name;
            final String pid = tbiqians.get(i).pid;
            TextView tv = new TextView(this);
            tv.setText(data);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setGravity(Gravity.CENTER);
            int paddingy = DisplayUtils.dp2Px(this, 7);
            int paddingx = DisplayUtils.dp2Px(this, 6);
            tv.setPadding(paddingx, paddingy, paddingx, paddingy);
            tv.setClickable(false);

            int shape = GradientDrawable.RECTANGLE;
            int radius = DisplayUtils.dp2Px(this, 4);
            int strokeWeight = DisplayUtils.dp2Px(this, 1);
            int stokeColor = getResources().getColor(R.color.colorPrimaryTXTHse);
            int stokeColor2 = getResources().getColor(R.color.colorPrimaryDark);

            GradientDrawable normalBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor, Color.WHITE);
            GradientDrawable pressedBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor2, getResources().getColor(R.color.colorPrimaryDark));
            StateListDrawable selector = DrawableUtils.getSelector(normalBg, pressedBg);
            tv.setBackgroundDrawable(selector);
            ColorStateList colorStateList = DrawableUtils.getColorSelector(getResources().getColor(R.color.colorPrimaryTXTHse), getResources().getColor(R.color.white));
            tv.setTextColor(colorStateList);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getQXBQ(pid);
                    tbiqians.remove(tbiqian);
                    mHandler.sendEmptyMessage(1);
                }
            });
            gyxbqlayout.addView(tv);
        }
    }

    private void displayUI1() {
        for (int i = 0; i < tbiqians1.size(); i++) {
            final String data = tbiqians1.get(i).name;
            final String pid = tbiqians1.get(i).pid;
            TextView tv = new TextView(this);
            tv.setText(data);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setGravity(Gravity.CENTER);
            int paddingy = DisplayUtils.dp2Px(this, 7);
            int paddingx = DisplayUtils.dp2Px(this, 6);
            tv.setPadding(paddingx, paddingy, paddingx, paddingy);
            tv.setClickable(false);

            int shape = GradientDrawable.RECTANGLE;
            int radius = DisplayUtils.dp2Px(this, 4);
            int strokeWeight = DisplayUtils.dp2Px(this, 1);
            int stokeColor = getResources().getColor(R.color.colorPrimaryTXTHse);
            int stokeColor2 = getResources().getColor(R.color.colorPrimaryDark);

            GradientDrawable normalBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor, Color.WHITE);
            GradientDrawable pressedBg = DrawableUtils.getShape(shape, radius, strokeWeight, stokeColor2, getResources().getColor(R.color.colorPrimaryDark));
            StateListDrawable selector = DrawableUtils.getSelector(normalBg, pressedBg);
            tv.setBackgroundDrawable(selector);
            ColorStateList colorStateList = DrawableUtils.getColorSelector(getResources().getColor(R.color.colorPrimaryTXTHse), getResources().getColor(R.color.white));
            tv.setTextColor(colorStateList);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getTjianBQ(pid);
                    tbiqians.add(new Tbiqian(pid, data));
                    mHandler.sendEmptyMessage(1);
                }
            });
            wdbqlayout.addView(tv);
        }
    }
}
