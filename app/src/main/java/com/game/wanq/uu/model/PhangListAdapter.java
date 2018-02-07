package com.game.wanq.uu.model;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.bean.TGame;
import com.game.wanq.uu.model.bean.Tbiqian;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.DownloadUtil;
import com.game.wanq.uu.utils.UtilsTools;
import com.game.wanq.uu.view.FxCKGDActivity;
import com.game.wanq.uu.view.GameXqingActivity;
import com.game.wanq.uu.view.whget.DownFView;
import com.game.wanq.uu.view.whget.FxgdHorizontalScrollView;
import com.game.wanq.uu.view.whget.MyListView;
import com.game.wanq.uu.view.whget.RatingBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhangListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TGame> mDatas;
    private String pids = "1234";

    public PhangListAdapter(Context context, List<TGame> mDatas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.wanq_phang_items_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.phangicon = (ImageView) convertView.findViewById(R.id.phangicon);
            viewHolder.phangname = (TextView) convertView.findViewById(R.id.phangname);
            viewHolder.phangbarview = (RatingBarView) convertView.findViewById(R.id.phangbarview);
            viewHolder.phangbartext = (TextView) convertView.findViewById(R.id.phangbartext);
            viewHolder.phangscrollview = (FxgdHorizontalScrollView) convertView.findViewById(R.id.phangscrollview);
            viewHolder.phanglint = (LinearLayout) convertView.findViewById(R.id.phanglint);
            viewHolder.phangLxiazai = (RelativeLayout) convertView.findViewById(R.id.phangLxiazai);
            viewHolder.phangxiazai = (ImageView) convertView.findViewById(R.id.phangxiazai);
            viewHolder.phangDownFView = (DownFView) convertView.findViewById(R.id.phangDownFView);
            viewHolder.yuyue = (RelativeLayout) convertView.findViewById(R.id.yuyue);
            viewHolder.phangyuyue = (ImageView) convertView.findViewById(R.id.phangyuyue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (((MyListView) parent).isOnMeasure) {
            return convertView;//如果是onMeasure调用的就立即返回
        }

        final TGame tGame = mDatas.get(position);
        Glide.with(mContext).load(tGame.icon).priority(Priority.HIGH).into(viewHolder.phangicon);
        viewHolder.phangname.setText(tGame.name);
        viewHolder.phangbartext.setText(tGame.score + "");
        viewHolder.phangbarview.setStarMark(Float.parseFloat(tGame.score / 2 + ""));
        viewHolder.phangbarview.setMarkOk(false);
        String biqian = tGame.labelList;
        if (!TextUtils.isEmpty(biqian) && !biqian.equals("[]")) {
            try {
                final List<Tbiqian> mList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(biqian);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String pid = jsonObject.getString("pid");
                    String name = jsonObject.getString("name");
                    mList.add(new Tbiqian(pid, name));
                }
                FXCKGDHorizontalScrollViewAdapter mAdapter = new FXCKGDHorizontalScrollViewAdapter(mContext, mList);
                viewHolder.phangscrollview.initDatas(mAdapter, mList.size());
                viewHolder.phangscrollview.setOnItemClickListener(new FxgdHorizontalScrollView.OnItemClickListener() {
                    @Override
                    public void onClick(View view, int pos) {
                        Intent intent = new Intent(mContext, FxCKGDActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("bqianPID", mList.get(pos).pid);
                        intent.putExtra("bqianName", mList.get(pos).name);
                        mContext.startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        viewHolder.phanglint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GameXqingActivity.class);
                intent.putExtra(Config.DETAIL, tGame.pid);
                mContext.startActivity(intent);
            }
        });
        if (tGame.subscribe == 1) {
            if (!TextUtils.isEmpty(tGame.url)) {
                viewHolder.phangLxiazai.setVisibility(View.VISIBLE);
                viewHolder.yuyue.setVisibility(View.GONE);
                final DownloadUtil down = new DownloadUtil();
                viewHolder.phangLxiazai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!tGame.isDown) {
                            tGame.isDown = true;
                            viewHolder.phangDownFView.setVisibility(View.VISIBLE);
                            viewHolder.phangxiazai.setVisibility(View.GONE);
                            setDownStart(1, tGame.pid, "", false, false);
                            down.download(tGame.url, UtilsTools.getInstance(mContext).isExistsFilePath(),
                                    new DownloadUtil.OnDownloadListener() {
                                        @Override
                                        public void onDownloadSuccess(String path) {
                                            setDownStart(1, tGame.pid, pids, true, false);
                                            UtilsTools.getInstance(mContext).startInstallActivity(path);
                                        }

                                        @Override
                                        public void onDownloading(int progress) {
                                            viewHolder.phangDownFView.setProgress(progress);
                                        }

                                        @Override
                                        public void onDownloadFailed() {

                                        }
                                    });
                        } else {
                            tGame.isDown = false;
                            viewHolder.phangDownFView.setVisibility(View.GONE);
                            viewHolder.phangxiazai.setVisibility(View.VISIBLE);
                            down.cancel();
                        }

                    }
                });
            }
        } else if (tGame.subscribe == 0) {
            viewHolder.phangLxiazai.setVisibility(View.GONE);
            viewHolder.yuyue.setVisibility(View.VISIBLE);
            if (!tGame.subscribeType) {//预约
                viewHolder.phangyuyue.setBackground(mContext.getResources().getDrawable(R.drawable.phangbqian_background));
            } else {
                viewHolder.phangyuyue.setBackground(mContext.getResources().getDrawable(R.drawable.phangbqian_background));
            }
            viewHolder.yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSubscribe(tGame, tGame.subscribeType);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView phangicon;
        private TextView phangname, phangbartext;
        private RatingBarView phangbarview;
        private FxgdHorizontalScrollView phangscrollview;
        private LinearLayout phanglint;
        private ImageView phangxiazai, phangyuyue;
        private DownFView phangDownFView;
        private RelativeLayout phangLxiazai, yuyue;
    }

    private void setDownStart(int gameType, String typeObjid, String pid, final boolean isDownd, boolean isInstall) {
        try {
            RequestManager.getInstance(mContext).httpPost(Config.getDownload,
                    ParaTran.getInstance(mContext).setDownload(gameType, typeObjid, pid, isDownd, isInstall), new ReqCallBack() {
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

    private void setSubscribe(final TGame tGame, final boolean isTye) {
        try {
            RequestManager.getInstance(mContext).httpPost(Config.getSubscribe,
                    ParaTran.getInstance(mContext).setSubscribe(tGame.pid), new ReqCallBack() {
                        @Override
                        public void onReqSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int resultType = jsonObject.getInt("result");
                                if (resultType == 1) {
                                    return;
                                }
                                if (isTye) {
                                    tGame.subscribeType = false;
                                } else {
                                    tGame.subscribeType = true;
                                }
                                notifyDataSetChanged();
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
