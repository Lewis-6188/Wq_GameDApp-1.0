package com.game.wanq.uu.view;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.GrenFragmentPagerAdapter;
import com.game.wanq.uu.model.GrenPLListAdapter;
import com.game.wanq.uu.model.ShoucGanmeAdapter;
import com.game.wanq.uu.model.bean.TGRComment;
import com.game.wanq.uu.model.bean.TUsersCollection;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.SPrefUtils;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.whget.MyGridView;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.MyScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginCGFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private TabLayout logincg_tad;
    private GrenFragmentPagerAdapter adapter;
    private ViewPager logincg_pager;
    private View view_01, view_02;//两个待滑动的view
    private ImageView titleLimage, titleRgeisicon, grenXGZL, sqfqi;
    private MyGridView shoucangGrid, wanguoGrid;
    private MyListView grenyxpl;
    private List<TGRComment> tgrComments;
    private ShoucGanmeAdapter shoucAdapter;
    private GrenPLListAdapter grenPLListAdapter;
    private List<TUsersCollection> mCollections;
    private TextView titleContText;
    private LinearLayout wanguo, shouchang, grenShezhi, grenZhangdan, grenDhuan;
    private TextView wanguoText, shouchangText;
    private int mFirstY = 0, mCurrentY = 0;
    private MyScrollView scroll_view;
    private SPrefUtils mSP;
    private LinearLayout weidl;
    private CircleImageView grentx_tabplimage;
    private TextView userid, usernamems, gerendianz, fensinum, guanzhunum;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String iconURL = mSP.getString(mSP.USER_ICON, "");
                    if (!TextUtils.isEmpty(iconURL))
                        Glide.with(LoginCGFragment.this).load(iconURL).priority(Priority.HIGH).into(grentx_tabplimage);
                    titleContText.setText(mSP.getString(mSP.USER_NICKNAME, ""));
                    int leng = mSP.getString(mSP.USER_ID, "").length();
                    userid.setText("ID：" + mSP.getString(mSP.USER_ID, "").substring(leng - 10, leng));
                    usernamems.setText(mSP.getString(mSP.USER_NAME, ""));
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 0) {
                            JSONObject object = jsonObject.getJSONObject("datas");
                            int followNumber = object.getInt("followNumber");
                            int fansNumber = object.getInt("fansNumber");
                            int zanNumber = object.getInt("zanNumber");
                            gerendianz.setText("  " + zanNumber + "");
                            fensinum.setText("粉丝  " + fansNumber + "");
                            guanzhunum.setText("关注  " + followNumber + "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
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
                case 4:
                    shoucAdapter = new ShoucGanmeAdapter(getActivity(), mCollections);
                    wanguoGrid.setAdapter(shoucAdapter);
                    UtilsTools.getInstance(getActivity()).setListViewHeightBasedOnChildren(wanguoGrid);
                    wanguoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), GameXqingActivity.class);
                            intent.putExtra(Config.DETAIL, mCollections.get(position).typeObjid);
                            startActivity(intent);
                        }
                    });
                    break;
                case 5:
                    grenPLListAdapter = new GrenPLListAdapter(getActivity(), tgrComments);
                    grenyxpl.setAdapter(grenPLListAdapter);
                    grenyxpl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), GameXqingActivity.class);
                            intent.putExtra(Config.DETAIL, tgrComments.get(position).typeObjid);
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_gren_logincg_layout, container, false);
        mSP = SPrefUtils.getInstance(getActivity());
        titleLimage = (ImageView) view.findViewById(R.id.title_logincgxq).findViewById(R.id.titleLimage);
        titleRgeisicon = (ImageView) view.findViewById(R.id.title_logincgxq).findViewById(R.id.titleRgeisicon);
        titleContText = (TextView) view.findViewById(R.id.title_logincgxq).findViewById(R.id.titleContText);
        titleLimage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.fanhui));
        titleRgeisicon.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.shezhi));
        titleLimage.setVisibility(View.GONE);
        titleRgeisicon.setVisibility(View.GONE);
        titleContText.setText("未登录");
        logincg_tad = (TabLayout) view.findViewById(R.id.logincg_tad);
        scroll_view = (MyScrollView) view.findViewById(R.id.scroll_view);
        logincg_pager = (ViewPager) view.findViewById(R.id.logincg_pager);
        grenShezhi = (LinearLayout) view.findViewById(R.id.grenShezhi);
        grenZhangdan = (LinearLayout) view.findViewById(R.id.grenZhangdan);
        grenDhuan = (LinearLayout) view.findViewById(R.id.grenDhuan);
        grenXGZL = (ImageView) view.findViewById(R.id.grenXGZL);
        sqfqi = (ImageView) view.findViewById(R.id.sqfqi);
        weidl = (LinearLayout) view.findViewById(R.id.weidl);
        weidl.setOnClickListener(this);
        grentx_tabplimage = (CircleImageView) view.findViewById(R.id.grentx_tabplimage);
        userid = (TextView) view.findViewById(R.id.userid);
        usernamems = (TextView) view.findViewById(R.id.usernamems);

        gerendianz = (TextView) view.findViewById(R.id.gerendianz);
        fensinum = (TextView) view.findViewById(R.id.fensinum);
        guanzhunum = (TextView) view.findViewById(R.id.guanzhunum);


        grenShezhi.setOnClickListener(this);
        grenZhangdan.setOnClickListener(this);
        grenDhuan.setOnClickListener(this);
        grenXGZL.setOnClickListener(this);
        sqfqi.setOnClickListener(this);
        scroll_view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String text = mSP.getString(mSP.USER_ID, "");
        if (!TextUtils.isEmpty(text)) {

            getUserDatas();

            getfindUserRelNumer();
            view_01 = getActivity().getLayoutInflater().inflate(R.layout.wanq_gren_szhiyx_layout, null);
            view_02 = getActivity().getLayoutInflater().inflate(R.layout.wanq_gren_szhipl_layout, null);

            grenyxpl = (MyListView) view_02.findViewById(R.id.grenyxpl);

            wanguo = (LinearLayout) view_01.findViewById(R.id.wanguo);
            shouchang = (LinearLayout) view_01.findViewById(R.id.shouchang);
            wanguoText = (TextView) view_01.findViewById(R.id.wanguoText);
            shouchangText = (TextView) view_01.findViewById(R.id.shouchangText);
            wanguoGrid = (MyGridView) view_01.findViewById(R.id.wanguoGrid);
            shoucangGrid = (MyGridView) view_01.findViewById(R.id.shoucangGrid);
            adapter = new GrenFragmentPagerAdapter(getActivity());
            adapter.addV(view_01, "游戏");
            adapter.addV(view_02, "评论");
            logincg_pager.setAdapter(adapter);
            logincg_pager.setCurrentItem(0);
            logincg_tad.setupWithViewPager(logincg_pager);
            LinearLayout linearLayout = (LinearLayout) logincg_tad.getChildAt(0);
            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            linearLayout.setDividerDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.layout_divider_vertical));
            wanguo.setOnClickListener(this);
            shouchang.setOnClickListener(this);
            getwanguolist(0, 10);
            getPLList();

        } else {
            userid.setText("未登录");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wanguo:
                if (TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                } else {
                    wanguoText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    shouchangText.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                    wanguoGrid.setVisibility(View.VISIBLE);
                    shoucangGrid.setVisibility(View.GONE);
                    if (mCollections != null) mCollections.clear();
                    getwanguolist(0, 10);
                }
                break;
            case R.id.shouchang:
                if (TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                } else {
                    if (mCollections != null)
                        mCollections.clear();
                    wanguoText.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                    shouchangText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    shoucangGrid.setVisibility(View.VISIBLE);
                    wanguoGrid.setVisibility(View.GONE);
                    getCollectionlist(0, 10);
                }
                break;
            case R.id.grenShezhi:
                if (TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                } else
                    UtilsTools.getInstance(getActivity()).startClass(SZHIActivity.class);
                break;
            case R.id.grenZhangdan:
                if (TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                } else
                    UtilsTools.getInstance(getActivity()).startClass(GrenZDanActivity.class);

                break;
            case R.id.grenDhuan:
                if (TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                } else
                    UtilsTools.getInstance(getActivity()).startClass(GrenDhuanActivity.class);
                break;
            case R.id.grenXGZL:
                if (TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                } else
                    UtilsTools.getInstance(getActivity()).startClass(GrenXGActivity.class);
                break;
            case R.id.sqfqi:
                if (TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                } else
                    getfindUserAuth();
                break;
            case R.id.weidl:
                if (TextUtils.isEmpty(mSP.getString(mSP.USER_ID, ""))) {
                    UtilsTools.getInstance(getActivity()).startClass(GrenActivity.class);
                }
                break;
        }

    }

    private void getUserDatas() {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getfindUserData, ParaTran.getInstance(getActivity()).
                    setfindUserData(), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            return;
                        }
                        JSONObject object = new JSONObject(jsonObject.getString("datas"));
                        String pid = object.getString("pid").trim();
                        String loginName = object.getString("loginName").trim();
                        String nickName = object.getString("nickName").trim();
                        String icon = object.getString("icon").trim();
                        String phone = object.getString("phone").trim();
                        String rigstTime = object.getString("rigstTime").trim();
                        SPrefUtils mSP = SPrefUtils.getInstance(getActivity());
                        mSP.putString(mSP.USER_ID, pid);
                        mSP.putString(mSP.USER_NAME, loginName);
                        mSP.putString(mSP.USER_NICKNAME, nickName);
                        mSP.putString(mSP.USER_ICON, icon);
                        mSP.putString(mSP.USER_PHONE, phone);
                        mSP.putString(mSP.USER_RIGSTTIME, rigstTime);
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

    private void getfindUserRelNumer() {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getfindUserRelNumer, ParaTran.getInstance(getActivity()).
                    setfindUserRelNumer(), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            return;
                        }
                        Message message = Message.obtain();
                        message.obj = result;
                        message.what = 2;
                        mHandler.sendMessage(message);
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

    private void getfindUserAuth() {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getfindUserAuth, ParaTran.getInstance(getActivity()).
                    setfindUserAuth(), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            UtilsTools.getInstance(getActivity()).startClass(GrenSMRZActivity.class);
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

    private void getwanguolist(int pageNum, int pageSize) {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getUserwanguolist, ParaTran.getInstance(getActivity()).
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
                        mHandler.sendEmptyMessage(4);
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

    private void getPLList() {
        try {
            RequestManager.getInstance(getActivity()).httpPost(Config.getusersCommentlist,
                    ParaTran.getInstance(getActivity()).setUComment(), new ReqCallBack() {
                        @Override
                        public void onReqSuccess(String result) {
                            Log.i("6188", "--评论列表--->>" + result);
                            try {
                                tgrComments = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(result);
                                int resultType = jsonObject.getInt("result");
                                if (resultType == 1) {
                                    return;
                                }
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("datas"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String pid = object.getString("pid").trim();
                                    int type = object.getInt("type");
                                    String typeObjid = object.getString("typeObjid").trim();
                                    double score = object.getDouble("score");
                                    String content = object.getString("content").trim();
                                    int zanCount = object.getInt("zanCount");
                                    int caiCount = object.getInt("caiCount");
                                    String time = object.getString("time").trim();
                                    String typeObjname = object.getString("typeObjname").trim();
                                    String typeObjicon = object.getString("typeObjicon").trim();
                                    tgrComments.add(new TGRComment(pid, type, typeObjid, score, content, zanCount, caiCount, time, typeObjname, typeObjicon));
                                }
                                mHandler.sendEmptyMessage(5);
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
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentY = (int) event.getY();
                if (mCurrentY - mFirstY > 0) {//向下滑动
                    MainActivity.tabLinear.setVisibility(View.VISIBLE);
                } else if (mFirstY - mCurrentY > 0) {//向上滑动
                    MainActivity.tabLinear.setVisibility(View.GONE);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}