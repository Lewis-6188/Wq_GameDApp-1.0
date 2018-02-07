package com.game.wanq.uu.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.model.bean.TComment;
import com.game.wanq.uu.model.bean.TGame;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.ReqDialog;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.DownloadUtil;
import com.game.wanq.uu.utils.PermissionUtils;
import com.game.wanq.uu.utils.SPrefUtils;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.whget.BadgeView;
import com.game.wanq.uu.view.whget.CustomViewPager;
import com.game.wanq.uu.view.whget.DownFView;
import com.game.wanq.uu.view.whget.MyScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lewis.Liu on 2017/12/26.
 */

public class GameXqingActivity extends FragmentActivity implements MyScrollView.onScrollChangedListener,
        ViewTreeObserver.OnGlobalLayoutListener,
        View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private MyScrollView mScrollView;
    private FrameLayout mParentView;
    private ImageView mIvTop, yxicon, yxxiazai, xiazaiTab;
    private int mHeight, mHeight2;
    private TextView CText, yxname, yxcs, instalNmus, tuijian, yxfens;
    private DownFView yxxiazai_jd, yxxiazai_jd2;
    private LinearLayout tLinear, search01, tablayout;
    private TabLayout tad;
    private String gameID;
    private ReqDialog mDialog;
    private LinearLayout fanhui;
    public static CustomViewPager pager;
    public static MyFragmentPagerAdapter adapter;
    public static TGame tGame;
    public static List<TComment> tComments;
    private DownloadUtil mDown;
    private RelativeLayout tabxiazaiLinayou;
    private String pids = "1234";
    private LinearLayout shouchangyx;
    private ImageView shouchangyximage;
    private boolean isshoucangType = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    CText.setText(tGame.name);
                    if (!TextUtils.isEmpty(tGame.topimg))
                        Glide.with(GameXqingActivity.this).load(tGame.topimg).priority(Priority.HIGH).into(mIvTop);
                    if (!TextUtils.isEmpty(tGame.icon))
                        Glide.with(GameXqingActivity.this).load(tGame.icon).priority(Priority.HIGH).into(yxicon);
                    yxname.setText(tGame.name);
                    yxcs.setText(tGame.manufacturerName);
                    instalNmus.setText(tGame.insertNum + "安装");
                    if (tGame.istj == 1) {
                        tuijian.setVisibility(View.VISIBLE);
                        tuijian.setText("编辑推荐");
                    } else if (tGame.istj == 1) {
                        tuijian.setVisibility(View.GONE);
                    }
                    yxfens.setText(tGame.score + "");
                    mDown = DownloadUtil.getInstance();

                    adapter = new MyFragmentPagerAdapter(GameXqingActivity.this, getSupportFragmentManager());
                    adapter.addFragment(new XQFragment().newInstance(), "详情", 0);
                    if (tGame.stagesNum != 0) {
                        adapter.addFragment(new FQFragment().newInstance(), "分期", tGame.stagesNum);
                    }
                    adapter.addFragment(new PLFragment().newInstance(), "评论", tGame.commentNum);
                    pager.setAdapter(adapter);
                    tad.setupWithViewPager(pager);
                    LinearLayout linearLayout = (LinearLayout) tad.getChildAt(0);
                    linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                    linearLayout.setDividerDrawable(ContextCompat.getDrawable(GameXqingActivity.this,
                            R.drawable.layout_divider_vertical));
                    setUpTabBadge();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_gamexq_layout);
        mScrollView = (MyScrollView) this.findViewById(R.id.scroll_view);
        tLinear = (LinearLayout) this.findViewById(R.id.title_xq).findViewById(R.id.tLinear);//标题栏
        fanhui = (LinearLayout) this.findViewById(R.id.title_xq).findViewById(R.id.fanhui);
        CText = (TextView) this.findViewById(R.id.title_xq).findViewById(R.id.CText); //title
        mIvTop = (ImageView) this.findViewById(R.id.iv_top); //图片
        yxicon = (ImageView) this.findViewById(R.id.yxicon); //icon
        xiazaiTab = (ImageView) this.findViewById(R.id.xiazaiTab);
        xiazaiTab.setOnClickListener(this);
        yxname = (TextView) this.findViewById(R.id.yxname);
        yxcs = (TextView) this.findViewById(R.id.yxcs);
        instalNmus = (TextView) this.findViewById(R.id.instalNmus);
        tuijian = (TextView) this.findViewById(R.id.tuijian);
        yxfens = (TextView) this.findViewById(R.id.yxfens);
        yxxiazai = (ImageView) this.findViewById(R.id.yxxiazai);
        yxxiazai.setOnClickListener(this);
        yxxiazai_jd = (DownFView) this.findViewById(R.id.yxxiazai_jd);
        yxxiazai_jd.setOnClickListener(this);
        yxxiazai_jd2 = (DownFView) this.findViewById(R.id.yxxiazai_jd2);
        yxxiazai_jd2.setOnClickListener(this);
        tabxiazaiLinayou = (RelativeLayout) this.findViewById(R.id.tabxiazaiLinayou);
        shouchangyx = (LinearLayout) this.findViewById(R.id.shouchangyx);
        shouchangyximage = (ImageView) this.findViewById(R.id.shouchangyximage);
        shouchangyx.setOnClickListener(this);
        mParentView = (FrameLayout) this.findViewById(R.id.parent);
        search01 = (LinearLayout) this.findViewById(R.id.search01);
        tablayout = (LinearLayout) this.findViewById(R.id.tablayout);
        tad = (TabLayout) this.findViewById(R.id.tad);
        pager = (CustomViewPager) this.findViewById(R.id.pager);
        mScrollView.addOnScrollChangedListener(this);
        mParentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        //---------------->>
        gameID = getIntent().getStringExtra(Config.DETAIL);
        mDialog = new ReqDialog(this, R.style._dialog);
        fanhui.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPLList();
        getGameXQDatas();
    }

    @Override
    public void onScrollChanged(int y) {
        if (y <= 0) {//未滑动
            tLinear.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTM));
        } else if (y > 0 && y < mHeight) { //滑动过程中 并且在mHeight之内
            tLinear.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTM));
        } else {//超过mHeight
            tLinear.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (y >= mHeight2) {
            tabxiazaiLinayou.setVisibility(View.VISIBLE);
            if (tad.getParent() != search01) {
                tablayout.removeView(tad);
                tablayout.removeView(tabxiazaiLinayou);
                search01.addView(tad);
                search01.addView(tabxiazaiLinayou);
            }
        } else {
            if (tad.getParent() != tablayout) {
                search01.removeView(tad);
                search01.removeView(tabxiazaiLinayou);
                tablayout.addView(tad);
                tablayout.addView(tabxiazaiLinayou);
            }
            tabxiazaiLinayou.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGlobalLayout() {
        mParentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        mHeight = mIvTop.getHeight();
        mHeight2 = tablayout.getTop();
        onScrollChanged(mScrollView.getScrollY());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shouchangyx:
                SPrefUtils mSP = SPrefUtils.getInstance(this);
                String id = mSP.getString(mSP.USER_ID, "");
                if (!TextUtils.isEmpty(id)) {
                    if (!isshoucangType) {
                        isshoucangType = true;
                        shouchangyximage.setImageDrawable(getResources().getDrawable(R.mipmap.txin2));
                    } else {
                        isshoucangType = false;
                        shouchangyximage.setImageDrawable(getResources().getDrawable(R.mipmap.txin));
                    }
                    setShoucangGame(1, tGame.pid);
                } else {
                    UtilsTools.getInstance(this).startClass(GrenActivity.class);
                }
                break;
            case R.id.fanhui:
                finish();
                break;
            case R.id.yxxiazai:
                yxxiazai_jd.setVisibility(View.VISIBLE);
                yxxiazai.setVisibility(View.GONE);
                writeExternalStorage();
                break;
            case R.id.yxxiazai_jd:
                yxxiazai.setVisibility(View.VISIBLE);
                yxxiazai_jd.setVisibility(View.GONE);
                if (mDown != null) {
                    mDown.cancel();
                }
                break;
            case R.id.xiazaiTab:
                yxxiazai_jd2.setVisibility(View.VISIBLE);
                xiazaiTab.setVisibility(View.GONE);
                writeExternalStorage();

                break;
            case R.id.yxxiazai_jd2:
                xiazaiTab.setVisibility(View.VISIBLE);
                yxxiazai_jd2.setVisibility(View.GONE);
                if (mDown != null)
                    mDown.cancel();
                break;
        }
    }


    // 设置Tablayout上的标题的角标
    private void setUpTabBadge() {
        for (int i = 0; i < adapter.mFragments.size(); i++) {
            TabLayout.Tab tab = tad.getTabAt(i);
            View customView = tab.getCustomView(); // 更新Badge前,先remove原来的customView,否则Badge无法更新
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }
            tab.setCustomView(adapter.getTabItemView(i));// 更新CustomView
        }
        tad.getTabAt(tad.getSelectedTabPosition()).getCustomView().setSelected(true);  // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new LinkedList<>();
        private List<String> mTitles = new LinkedList<>();
        private List<Integer> mBadgeCountList = new LinkedList<>();
        private Context mContext;

        public MyFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.mContext = context;
        }

        public void addFragment(Fragment fragment, String title,
                                Integer badgeCountList) {
            mFragments.add(fragment);
            mTitles.add(title);
            mBadgeCountList.add(badgeCountList);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        //       根据角标获取标题item的布局文件
        public View getTabItemView(int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.wanq_tab_layout_item, null);  // 标题布局
            TextView textView = (TextView) view.findViewById(R.id.textview);
            textView.setText(mTitles.get(position));  // 设置标题内容
            View target = view.findViewById(R.id.badgeview_target);//右上角数字标记
            BadgeView badgeView = new BadgeView(mContext);
            badgeView.setTargetView(target);
            badgeView.setEllipsize(TextUtils.TruncateAt.END);
            badgeView.setSingleLine(true);
            badgeView.setBadgeMargin(0, 6, 6, 0);
            badgeView.setTextSize(10);
            badgeView.setText(Integer.toString(mBadgeCountList.get(position)));
            if (mBadgeCountList.get(position) == 0) {
                target.setVisibility(View.GONE);
            } else {
                target.setVisibility(View.VISIBLE);

            }
            return view;
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
                    setDownStart(1, tGame.pid, "", false, false);
                    DownloadUtil.getInstance().download(tGame.url, UtilsTools.getInstance(GameXqingActivity.this).isExistsFilePath(),
                            new DownloadUtil.OnDownloadListener() {
                                @Override
                                public void onDownloadSuccess(String path) {
                                    setDownStart(1, tGame.pid, pids, true, false);
                                    installR Receiver = new installR();
                                    IntentFilter addFilter = new IntentFilter();
                                    addFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
                                    addFilter.addDataScheme("package");
                                    registerReceiver(Receiver, addFilter);
                                    String packgname = tGame.pkgname;
                                    if (TextUtils.isEmpty(packgname)) {
                                        tGame.pkgname = getPack(path);
                                    }
                                    UtilsTools.getInstance(GameXqingActivity.this).startInstallActivity(path);
                                }

                                @Override
                                public void onDownloading(int progress) {
                                    yxxiazai_jd.setProgress(progress);
                                    yxxiazai_jd2.setProgress(progress);
                                }

                                @Override
                                public void onDownloadFailed() {

                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(final int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    private void getGameXQDatas() {
        try {
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            RequestManager.getInstance(this).httpGet(Config.getDetail, ParaTran.getInstance(this).setDetailDatas(gameID, ""), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            mDialog.dismiss();
                            return;
                        }
                        JSONObject object = new JSONObject(jsonObject.getString("datas"));
                        String pid = object.getString("pid").trim();
                        String name = object.getString("name").trim();
                        String icon = object.getString("icon").trim();
                        String pkgname = object.getString("pkgname").trim();
                        Double score = 8.6;
                        try {
                            score = object.getDouble("score");
                        } catch (Exception e) {

                        }
                        String manufacturerPid = object.getString("manufacturerPid").trim();
                        String updatetime = object.getString("updatetime").trim();
                        String manufacturerName = object.getString("manufacturerName").trim();
                        int istj = object.getInt("istj");
                        String topimg = object.getString("topimg").trim();
                        String size = object.getString("size").trim();
                        String detailimg = object.getString("detailimg").trim();
                        String url = object.getString("url").trim();
                        String intro = object.getString("intro").trim();
                        String content = "";
                        try {
                            content = object.getString("content").trim();
                        } catch (Exception e) {

                        }
                        String version = object.getString("version").trim();
                        int insertNum = object.getInt("insertNum");
                        int downloadNum = object.getInt("downloadNum");
                        int commentNum = object.getInt("commentNum");
                        int stagesNum = object.getInt("stagesNum");
                        String labelList = object.getString("labelList").trim();
                        tGame = new TGame(pid, name, icon, pkgname, score, manufacturerPid, manufacturerName, istj, topimg, detailimg, url, intro, content, version, insertNum, downloadNum, commentNum
                                , stagesNum, labelList, size, updatetime);
                        mHandler.sendEmptyMessage(1);
                        mDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String result) {
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mDialog.dismiss();
        }
    }

    private void getPLList() {
        try {
            RequestManager.getInstance(this).httpGet(Config.getPLList,
                    ParaTran.getInstance(this).setPLList(1, gameID), new ReqCallBack() {
                        @Override
                        public void onReqSuccess(String result) {
                            Log.i("6188", "---评论信息->>" + result);
                            try {
                                tComments = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(result);
                                int resultType = jsonObject.getInt("result");
                                if (resultType == 1) {
                                    return;
                                }
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("datas"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String pid = object.getString("pid").trim();
                                    String uid = object.getString("uid").trim();
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
                                    String rpid = "";
                                    try {
                                        rpid = object.getString("rpid").trim();
                                    } catch (Exception e) {

                                    }
                                    String rUidIcon = "";
                                    try {
                                        rUidIcon = object.getString("rUidIcon").trim();
                                    } catch (Exception e) {

                                    }
                                    String ruid = "";
                                    try {
                                        ruid = object.getString("ruid").trim();
                                    } catch (Exception e) {

                                    }
                                    String returnComment = "";
                                    try {
                                        returnComment = object.getJSONArray("returnComment").toString();
                                    } catch (Exception e) {
                                    }
                                    tComments.add(new TComment(pid, rpid, uid, ruid, type, typeObjid,
                                            score, content, phoneType, zanCount, caiCount,
                                            time, returnComment, uidName, rUidName, uidIcon, rUidIcon, false, false));
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

    private void setDownStart(int gameType, String typeObjid, String pid, final boolean isDownd, boolean isInstall) {
        try {
            RequestManager.getInstance(this).httpPost(Config.getDownload,
                    ParaTran.getInstance(this).setDownload(gameType, typeObjid, pid, isDownd, isInstall), new ReqCallBack() {
                        @Override
                        public void onReqSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int resultType = jsonObject.getInt("result");
                                if (resultType == 1) {
                                    return;
                                }
                                if (!isDownd) {
                                    JSONObject object = jsonObject.getJSONObject("datas");
                                    pids = object.getString("pid");
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

    private void setShoucangGame(int type, String typeObjid) {
        try {
            RequestManager.getInstance(this).httpPost(Config.getupdateUserCollection,
                    ParaTran.getInstance(this).setpdateUserCollection(type, typeObjid), new ReqCallBack() {
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

    private String getPack(String path) {
        String pack = "";
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = null;
        if (info != null) {
            appInfo = info.applicationInfo;
            pack = appInfo.packageName;
        }
        return pack;
    }

    private class installR extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                String packageName = intent.getDataString().substring(8);
                if (packageName.equals(tGame.pkgname)) {
                    setDownStart(1, tGame.pid, pids, true, true);
                }
            }
        }
    }

}
