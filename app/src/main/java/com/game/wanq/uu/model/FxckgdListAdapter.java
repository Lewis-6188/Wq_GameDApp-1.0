package com.game.wanq.uu.model;

import android.content.Context;
import android.content.Intent;
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
import com.game.wanq.uu.view.whget.RatingBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FxckgdListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TGame> mDatas;
    private String pids = "1234";

    public FxckgdListAdapter(Context context, List<TGame> mDatas) {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.wanq_fx_ckgd_items_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.fxckgdicon = (ImageView) convertView.findViewById(R.id.fxckgdicon);
            viewHolder.fxckgdname = (TextView) convertView.findViewById(R.id.fxckgdname);
            viewHolder.fxckgdbarview = (RatingBarView) convertView.findViewById(R.id.fxckgdbarview);
            viewHolder.fxckgdbartext = (TextView) convertView.findViewById(R.id.fxckgdbartext);
            viewHolder.fxckgdscrollview = (FxgdHorizontalScrollView) convertView.findViewById(R.id.fxckgdscrollview);
            viewHolder.fxckgdxiazai = (ImageView) convertView.findViewById(R.id.fxckgdxiazai);
            viewHolder.fxckgdDownFView = (DownFView) convertView.findViewById(R.id.fxckgdDownFView);
            viewHolder.intemText = (LinearLayout) convertView.findViewById(R.id.intemText);
            viewHolder.xiazai = (RelativeLayout) convertView.findViewById(R.id.xiazai);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final TGame tGame = mDatas.get(position);
        Glide.with(mContext).load(tGame.icon).priority(Priority.HIGH).into(viewHolder.fxckgdicon);
        viewHolder.fxckgdname.setText(tGame.name);
        viewHolder.fxckgdbartext.setText(tGame.score + "");
        viewHolder.fxckgdbarview.setStarMark(Float.parseFloat(tGame.score / 2 + ""));
        viewHolder.fxckgdbarview.setMarkOk(false);
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
                viewHolder.fxckgdscrollview.initDatas(mAdapter, mList.size());
                viewHolder.fxckgdscrollview.setOnItemClickListener(new FxgdHorizontalScrollView.OnItemClickListener() {
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
        viewHolder.intemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GameXqingActivity.class);
                intent.putExtra(Config.DETAIL, tGame.pid);
                mContext.startActivity(intent);
            }
        });
        final DownloadUtil down = new DownloadUtil();
        viewHolder.xiazai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tGame.isDown) {
                    tGame.isDown = true;
                    viewHolder.fxckgdDownFView.setVisibility(View.VISIBLE);
                    viewHolder.fxckgdxiazai.setVisibility(View.GONE);
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
                                    viewHolder.fxckgdDownFView.setProgress(progress);
                                }

                                @Override
                                public void onDownloadFailed() {

                                }
                            });
                } else {
                    tGame.isDown = false;
                    viewHolder.fxckgdDownFView.setVisibility(View.GONE);
                    viewHolder.fxckgdxiazai.setVisibility(View.VISIBLE);
                    down.cancel();
                }

            }
        });

        return convertView;
    }

    private class ViewHolder {
        private ImageView fxckgdicon;
        private TextView fxckgdname, fxckgdbartext;
        private RatingBarView fxckgdbarview;
        private FxgdHorizontalScrollView fxckgdscrollview;
        private ImageView fxckgdxiazai;
        private DownFView fxckgdDownFView;
        private LinearLayout intemText;
        private RelativeLayout xiazai;
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
}
