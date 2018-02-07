package com.game.wanq.uu.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.PLHFItemAdapter;
import com.game.wanq.uu.model.bean.TComment;
import com.game.wanq.uu.model.bean.TGame;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.emoticon.DisplayUtils;
import com.game.wanq.uu.view.emoticon.EmotionGvAdapter;
import com.game.wanq.uu.view.emoticon.EmotionPagerAdapter;
import com.game.wanq.uu.view.emoticon.EmotionUtils;
import com.game.wanq.uu.view.emoticon.StringUtils;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.RatingBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Lewis.Liu on 2018/1/16.
 */

public class HFActivity extends Activity implements View.OnClickListener {
    private TComment tComment, tCommentdia;
    private TGame tGame;
    private List<TComment> tComments;
    private LinearLayout huifufanhui;
    private ImageView yxhficon;
    private TextView yxhfname, yxhfpftext;
    private RatingBarView yxhfpinfen, plhfuratingbar;
    private CircleImageView hfIicon;
    private TextView plhfusername, plhfutext, plhfshebei, hfcaitext, hfzantext, hfzplshij, pltsutext;
    private LinearLayout plhfcai, plhfzan;
    private ImageView caihfimag, zanhfimag;
    private MyListView plhfList;
    private TextView plhfzxu, plhfdxu, plhftshu;
    private LinearLayout plhffab;
    private EditText plfabtext;
    private PLHFItemAdapter mAdapter;
    private Button diahuof, diacheng, diatshu, diaquxiao;
    private Dialog plhfdia;
    private TextView plhfusernames;
    private LinearLayout biqoqingqihuan;
    private ImageView biaoqiangImage;
    private boolean isBqing;
    private LinearLayout ll_emotion_dashboard;
    private ViewPager vp_emotion_dashboard;
    private EmotionPagerAdapter emotionPagerGvAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    plfabtext.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case 2:
                    String hflist = tComments.get(0).returnComment;
                    if (!TextUtils.isEmpty(hflist)) {
                        initPL(hflist);
                    } else {
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_hf_layout);
        huifufanhui = (LinearLayout) this.findViewById(R.id.huifufanhui);
        huifufanhui.setOnClickListener(this);
        biqoqingqihuan = (LinearLayout) this.findViewById(R.id.biqoqingqihuan);
        biqoqingqihuan.setOnClickListener(this);
        biaoqiangImage = (ImageView) this.findViewById(R.id.biaoqiangImage);
        ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
        vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
        plfabtext = (EditText) this.findViewById(R.id.plfabtext);
        initEmotion();

        tGame = (TGame) getIntent().getExtras().getSerializable("gameobject");
        if (tGame != null) {
            yxhficon = (ImageView) this.findViewById(R.id.yxhficon);
            yxhfname = (TextView) this.findViewById(R.id.yxhfname);
            yxhfpinfen = (RatingBarView) this.findViewById(R.id.yxhfpinfen);
            yxhfpftext = (TextView) this.findViewById(R.id.yxhfpftext);
            pltsutext = (TextView) this.findViewById(R.id.pltsutext);
            pltsutext.setOnClickListener(this);
            Glide.with(this).load(tGame.icon).priority(Priority.HIGH).into(yxhficon);
            yxhfname.setText(tGame.name);
            yxhfpftext.setText(tGame.score + "");
            yxhfpinfen.setStarMark(Float.parseFloat(tGame.score / 2 + ""));
            yxhfpinfen.setMarkOk(false);
        }
        tComment = (TComment) getIntent().getExtras().getSerializable("hfobject");
        if (tComment != null) {
            hfIicon = (CircleImageView) this.findViewById(R.id.hfIicon);
            plhfusername = (TextView) this.findViewById(R.id.plhfusername);
            plhfuratingbar = (RatingBarView) this.findViewById(R.id.plhfuratingbar);
            plhfutext = (TextView) this.findViewById(R.id.plhfutext);
            plhfshebei = (TextView) this.findViewById(R.id.plhfshebei);
            hfcaitext = (TextView) this.findViewById(R.id.hfcaitext);
            hfzantext = (TextView) this.findViewById(R.id.hfzantext);
            hfzplshij = (TextView) this.findViewById(R.id.hfzplshij);
            plhfcai = (LinearLayout) this.findViewById(R.id.plhfcai);
            plhfzan = (LinearLayout) this.findViewById(R.id.plhfzan);
            caihfimag = (ImageView) this.findViewById(R.id.caihfimag);
            zanhfimag = (ImageView) this.findViewById(R.id.zanhfimag);
            Glide.with(this).load(tComment.uidIcon).priority(Priority.HIGH).into(hfIicon);
            plhfusername.setText(tComment.uidName);
            plhfuratingbar.setStarMark(Float.parseFloat(tComment.score / 2 + ""));
            plhfuratingbar.setMarkOk(false);
            plhfutext.setText(StringUtils.getEmotionContent(this, plhfshebei, tComment.content));
            plhfshebei.setText(tComment.phoneType);
            hfcaitext.setText(tComment.caiCount + "");
            hfzantext.setText(tComment.zanCount + "");
            hfzplshij.setText(tComment.time);
            plhfcai.setOnClickListener(this);
            plhfzan.setOnClickListener(this);

            initPL(tComment.returnComment);

            plhfzxu = (TextView) this.findViewById(R.id.plhfzxu);
            plhfzxu.setOnClickListener(this);
            plhfdxu = (TextView) this.findViewById(R.id.plhfdxu);
            plhfdxu.setOnClickListener(this);
            plhffab = (LinearLayout) this.findViewById(R.id.plhffab);
            plhffab.setOnClickListener(this);
        }
    }

    private void initPL(String hfDatas) {
        try {
            if (!TextUtils.isEmpty(hfDatas)) {
                if (tComments != null) {
                    tComments.clear();
                } else {
                    tComments = new ArrayList<>();
                }
                JSONArray jsonArray = new JSONArray(hfDatas);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String pid = object.getString("pid").trim();
                    String rpid = object.getString("rpid").trim();
                    String uid = object.getString("uid").trim();
                    String ruid = object.getString("ruid").trim();
                    int type = object.getInt("type");
                    String typeObjid = object.getString("typeObjid").trim();
                    double score = object.getDouble("score");
                    String content = object.getString("content").trim();
                    String phoneType = object.getString("phoneType").trim();
                    int zanCount = object.getInt("zanCount");
                    int caiCount = object.getInt("caiCount");
                    String time = object.getString("time").trim();
                    String uidName = object.getString("uidName").trim();
                    String rUidName = object.getString("rUidName").trim();
                    String uidIcon = object.getString("uidIcon").trim();
                    String rUidIcon = object.getString("rUidIcon").trim();
                    tComments.add(new TComment(pid, rpid, uid, ruid, type, typeObjid,
                            score, content, phoneType, zanCount, caiCount,
                            time, "", uidName, rUidName, uidIcon, rUidIcon, false, false));
                }
                plhftshu = (TextView) this.findViewById(R.id.plhftshu);
                plhftshu.setText("(" + tComments.size() + ")");
                plhfList = (MyListView) this.findViewById(R.id.plhfList);
                mAdapter = new PLHFItemAdapter(this, tComments);
                UtilsTools.getInstance(this).setListViewHeight(plhfList);
                plhfList.setAdapter(mAdapter);
                plhfusernames = (TextView) this.findViewById(R.id.plhfusernames);
                plhfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tCommentdia = tComments.get(position);
                        showhuifu();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.huifufanhui:
                finish();
                break;
            case R.id.plhfzxu:
                if (mAdapter != null) {
                    plhfzxu.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    plhfdxu.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                    Collections.sort(tComments, new Comparator<TComment>() {
                        @Override
                        public int compare(TComment lhs, TComment rhs) {
                            Date date1 = stringToDate(lhs.time);
                            Date date2 = stringToDate(rhs.time);
                            // 对日期字段进行升序，如果欲降序可采用after方法
                            if (date1.before(date2)) {
                                return 1;
                            }
                            return -1;
                        }
                    });
                    mAdapter.notdATSX(tComments);
                }
                break;
            case R.id.plhfdxu:
                if (mAdapter != null) {
                    plhfdxu.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    plhfzxu.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                    Collections.sort(tComments, new Comparator<TComment>() {
                        @Override
                        public int compare(TComment lhs, TComment rhs) {
                            Date date1 = stringToDate(lhs.time);
                            Date date2 = stringToDate(rhs.time);
                            // 对日期字段进行升序，如果欲降序可采用after方法
                            if (date1.after(date2)) {
                                return 1;
                            }
                            return -1;
                        }
                    });
                    mAdapter.notdATSX(tComments);
                }
                break;
            case R.id.plhffab:
                String content = plfabtext.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    if (tCommentdia != null) {
                        tComment = tCommentdia;
                    }
                    setPLFB(tComment.uid, 1,
                            tGame.pid, tComment.rpid, tComment.score + "", content, tComment.pid);
                } else {
                    Toast.makeText(this, "写点内容再发吧", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.biqoqingqihuan:
                if (!isBqing) {
                    isBqing = true;
                    biaoqiangImage.setImageDrawable(this.getResources().getDrawable(R.mipmap.jianpan));
                    ll_emotion_dashboard.setVisibility(View.VISIBLE);
                } else {
                    isBqing = false;
                    biaoqiangImage.setImageDrawable(this.getResources().getDrawable(R.mipmap.biaoqing));
                    ll_emotion_dashboard.setVisibility(View.GONE);
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


                break;
            case R.id.plhfcai:
                if (!tComment.isCai) {
                    caihfimag.setImageDrawable(this.getResources().getDrawable(R.mipmap.cai1));
                    tComment.caiCount = tComment.caiCount + 1;
                    tComment.isCai = true;
                } else {
                    caihfimag.setImageDrawable(this.getResources().getDrawable(R.mipmap.cai2));
                    tComment.caiCount = tComment.caiCount - 1;
                    tComment.isCai = false;
                }
                hfcaitext.setText(tComment.caiCount + "");
                getPLList(tComment.type, tComment.pid, "0");
                break;
            case R.id.plhfzan:
                if (!tComment.isZan) {
                    zanhfimag.setImageDrawable(this.getResources().getDrawable(R.mipmap.zan2));
                    tComment.zanCount = tComment.zanCount + 1;
                    tComment.isZan = true;
                } else {
                    zanhfimag.setImageDrawable(this.getResources().getDrawable(R.mipmap.zan));
                    tComment.zanCount = tComment.zanCount - 1;
                    tComment.isZan = false;
                }
                hfzantext.setText(tComment.zanCount + "");
                getPLList(tComment.type, tComment.pid, "1");
                break;
            case R.id.diahuof:
                plhfdia.dismiss();
                plhfusernames.setVisibility(View.VISIBLE);
                String contents = "<font color=" + this.getResources().getColor(R.color.colorPrimaryTXTHse) + ">" + "回复" + "</font>" +
                        "<font color=" + this.getResources().getColor(R.color.colorPrimaryDark) + ">" + "  " + tCommentdia.uidName + "</font>";
                plhfusernames.setText(Html.fromHtml(contents));
                plfabtext.setText("");
                plfabtext.setFocusable(true);
                plfabtext.setFocusableInTouchMode(true);
                plfabtext.requestFocus();
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                break;
            case R.id.diacheng:
                tCommentdia = null;
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                plhfdia.dismiss();
                break;
            case R.id.diatshu:
                tCommentdia = null;
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                plhfdia.dismiss();
                UtilsTools.getInstance(this).startClass(TShuActivity.class);
                break;
            case R.id.diaquxiao:
                tCommentdia = null;
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                plhfdia.dismiss();
                break;
            case R.id.pltsutext:
                UtilsTools.getInstance(this).startClass(TShuActivity.class);
                break;
        }
    }

    private void getPLList(int gameType, String typeObjid, String upOfDown) {
        try {
            RequestManager.getInstance(this).httpPost(Config.getPLCZ, ParaTran.getInstance(this).
                    setPLZC(gameType, typeObjid, upOfDown), new ReqCallBack() {
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

    private void setPLFB(final String ruid, int gameType,
                         String typeObjid, String rpid, String score, String content, final String pid) {
        try {
            RequestManager.getInstance(this).httpGet(Config.getPLFB, ParaTran.getInstance(this).
                    setPLFB(ruid, gameType,
                            typeObjid, rpid, score, content), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            return;
                        }
                        mHandler.sendEmptyMessage(1);
                        getPLList(1, tGame.pid);
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

    private void getPLList(int gameType, String typeObjid) {
        try {
            RequestManager.getInstance(this).httpGet(Config.getPLList,
                    ParaTran.getInstance(this).setPLList(gameType, typeObjid), new ReqCallBack() {
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
                                    String returnComment = object.getJSONArray("returnComment").toString();
                                    tComments.add(new TComment("", "", "", "", 1, "",
                                            0.0, "", "", 0, 0,
                                            "", returnComment, "", "", "", "", false, false));
                                    mHandler.sendEmptyMessage(2);
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


    private Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    private void showhuifu() {
        plhfdia = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.wanq_plhf_item_dialog_layout, null);
        plhfdia.setContentView(inflate);
        diahuof = (Button) inflate.findViewById(R.id.diahuof);
        diacheng = (Button) inflate.findViewById(R.id.diacheng);
        diatshu = (Button) inflate.findViewById(R.id.diatshu);
        diaquxiao = (Button) inflate.findViewById(R.id.diaquxiao);
        diahuof.setOnClickListener(this);
        diacheng.setOnClickListener(this);
        diatshu.setOnClickListener(this);
        diaquxiao.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        layoutParams.width = this.getResources().getDisplayMetrics().widthPixels - 80;
        inflate.setLayoutParams(layoutParams);
        Window dialogWindow = plhfdia.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        plhfdia.show();
    }

    //初始化表情面板内容
    private void initEmotion() {
        // 获取屏幕宽度
        int gvWidth = DisplayUtils.getScreenWidthPixels(this);
        // 表情边距
        int spacing = DisplayUtils.dp2px(this, 8);
        // GridView中item的宽度
        int itemWidth = (gvWidth - spacing * 8) / 7;
        int gvHeight = itemWidth * 3 + spacing * 4;

        List<GridView> gvs = new ArrayList<GridView>();
        List<String> emotionNames = new ArrayList<String>();
        // 遍历所有的表情名字
        for (String emojiName : EmotionUtils.emojiMap.keySet()) {
            emotionNames.add(emojiName);
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 20) {
                GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing, itemWidth, gvHeight);
                gvs.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<String>();
            }
        }
        // 检查最后是否有不足20个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing, itemWidth, gvHeight);
            gvs.add(gv);
        }
        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
        vp_emotion_dashboard.setAdapter(emotionPagerGvAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gvWidth, gvHeight);
        vp_emotion_dashboard.setLayoutParams(params);
    }

    //创建显示表情的GridView
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth,
                                           int gvHeight) {
        GridView gv = new GridView(this);// 创建GridView
        gv.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBS));//Color.rgb(233, 233, 233)
        gv.setSelector(android.R.color.transparent);
        gv.setNumColumns(7);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        EmotionGvAdapter adapter = new EmotionGvAdapter(this, emotionNames, itemWidth);// 给GridView设置表情图片
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemAdapter = parent.getAdapter();
                if (itemAdapter instanceof EmotionGvAdapter) {
                    // 点击的是表情
                    EmotionGvAdapter emotionGvAdapter = (EmotionGvAdapter) itemAdapter;
                    if (position == emotionGvAdapter.getCount() - 1) {
                        // 如果点击了最后一个回退按钮,则调用删除键事件
                        plfabtext.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    } else {
                        // 如果点击了表情,则添加到输入框中
                        String emotionName = emotionGvAdapter.getItem(position);
                        // 获取当前光标位置,在指定位置上添加表情图片文本
                        int curPosition = plfabtext.getSelectionStart();
                        StringBuilder sb = new StringBuilder(plfabtext.getText().toString());
                        sb.insert(curPosition, emotionName);
                        // 特殊文字处理,将表情等转换一下
                        plfabtext.setText(StringUtils.getEmotionContent(HFActivity.this, plfabtext, sb.toString()));
                        // 将光标设置到新增完表情的右侧
                        plfabtext.setSelection(curPosition + emotionName.length());
                    }

                }
            }
        });
        return gv;
    }
}
