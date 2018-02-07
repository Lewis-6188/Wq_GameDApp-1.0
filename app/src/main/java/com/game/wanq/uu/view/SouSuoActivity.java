/**
 *
 */
package com.game.wanq.uu.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.bean.Tbiqian;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.DrawableUtils;
import com.game.wanq.uu.view.emoticon.DisplayUtils;
import com.game.wanq.uu.view.whget.BQFlowLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: [6188] 2017年9月13日 下午5:09:56
 * @Desc: <p>
 * </p>
 */
public class SouSuoActivity extends Activity implements View.OnClickListener {
    private LinearLayout aearch_fanhui, sousuolinyout;
    private EditText ss_sp;
    private List<String> mHistoryKeywords;//记录文本
    private ArrayAdapter<String> mArrAdapter;//搜索历史适配器
    private SharedPreferences mPref;//使用SharedPreferences记录搜索历史
    private LinearLayout mSearchHistoryLl;
    public final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";
    private List<Tbiqian> mList;
    private Map<String, String> map;
    private BQFlowLayout flowLayout;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    displayUI();
                    break;
                case 2:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_ss_aearch_layout);
        mPref = getSharedPreferences("input", Activity.MODE_PRIVATE);
        aearch_fanhui = (LinearLayout) this.findViewById(R.id.aearch_fanhui);
        aearch_fanhui.setOnClickListener(this);
        ss_sp = (EditText) this.findViewById(R.id.ss_sp);
        ss_sp.setOnClickListener(this);
        sousuolinyout = (LinearLayout) this.findViewById(R.id.sousuolinyout);
        sousuolinyout.setOnClickListener(this);
        flowLayout = (BQFlowLayout) findViewById(R.id.flowlayout);
        flowLayout.setSpace(DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5));
        flowLayout.setPadding(DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5),
                DisplayUtils.dp2Px(this, 5), DisplayUtils.dp2Px(this, 5));
        getSSBQAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aearch_fanhui:
                finish();
                break;
            case R.id.sousuolinyout:
                String text = ss_sp.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    save();
                    startActivity("", text);
                }
                break;
            case R.id.ss_sp:
                initSearchHistory();
                break;
            case R.id.clear_history_btn:
                cleanHistory();
                break;
            default:
                break;
        }
    }

    private void getSSBQAll() {
        try {
            RequestManager.getInstance(this).httpPost(Config.getSousuoBQlist, null, new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            return;
                        }
                        mList = new ArrayList<>();
                        map = new HashMap<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("datas");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String pid = object.getString("pid");
                            String name = object.getString("labelName");
                            mList.add(new Tbiqian(pid, name));
                            map.put(name, pid);
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

    private void displayUI() {
        for (int i = 0; i < mList.size(); i++) {
            final String data = mList.get(i).name;
            final String pid = mList.get(i).pid;
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
                    startActivity(pid, "");
                }
            });
            flowLayout.addView(tv);
        }
    }

    //初始化搜索历史记录
    public void initSearchHistory() {
        mSearchHistoryLl = (LinearLayout) findViewById(R.id.search_history_ll);
        ListView listView = (ListView) findViewById(R.id.search_history_lv);
        findViewById(R.id.clear_history_btn).setOnClickListener(this);
        String history = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        mHistoryKeywords = new ArrayList<>();
        if (!TextUtils.isEmpty(history)) {
            List<String> list = new ArrayList<String>();
            for (Object o : history.split(",")) {
                list.add((String) o);
            }
            mHistoryKeywords = list;
        }
        if (mHistoryKeywords.size() > 0) {
            mSearchHistoryLl.setVisibility(View.VISIBLE);
        } else {
            mSearchHistoryLl.setVisibility(View.GONE);
        }
        mArrAdapter = new ArrayAdapter<String>(this, R.layout.item_search_history, mHistoryKeywords);
        listView.setAdapter(mArrAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ss_sp.setText(mHistoryKeywords.get(i));
                startActivity("", mHistoryKeywords.get(i));
                mSearchHistoryLl.setVisibility(View.GONE);
            }
        });
        mArrAdapter.notifyDataSetChanged();
    }

    //储存搜索历史
    public void save() {
        String text = ss_sp.getText().toString();
        String oldText = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        if (!TextUtils.isEmpty(text) && !(oldText.contains(text))) {
            if (mHistoryKeywords.size() > 10) {
                return;//最多保存条数
            }
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_SEARCH_HISTORY_KEYWORD, text + "," + oldText);
            editor.commit();
            mHistoryKeywords.add(0, text);
        }
        mArrAdapter.notifyDataSetChanged();
    }

    //清除历史纪录
    public void cleanHistory() {
        mPref = getSharedPreferences("input", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(KEY_SEARCH_HISTORY_KEYWORD).commit();
        mHistoryKeywords.clear();
        mArrAdapter.notifyDataSetChanged();
        mSearchHistoryLl.setVisibility(View.GONE);
    }

    private void startActivity(String pid, String name) {
        Intent intent = new Intent(SouSuoActivity.this, SouSuoDatasActivity.class);
        intent.putExtra("cdpid", pid);
        intent.putExtra("cdname", name);
        startActivity(intent);
//        Message message = Message.obtain();
//        message.obj = pid + "#" + name;
//        message.what = 2;
//        mHandler.sendMessage(message);
    }

}
