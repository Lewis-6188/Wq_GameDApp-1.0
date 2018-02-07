/**
 *
 */
package com.game.wanq.uu.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: [6188] 2017年7月7日 下午2:52:31
 * @Desc: <p>
 * </p>
 */
public class UtilsTools {
    private static UtilsTools mTools;

    public static UtilsTools getInstance(Context context) {
        if (mTools == null) {
            mTools = new UtilsTools(context);
        }
        return mTools;
    }

    private UtilsTools(Context context) {
        this.mContext = context;
    }

    private Context mContext;

    public void startClass(Class mClass) {
        Intent intent = new Intent(mContext, mClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 获取Sdcard卡路径
     *
     * @return
     * @author LiuYi
     * @data 2015年7月4日 下午6:24:01
     */
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        } else {
        }
        return "";
    }

    /**
     * 创建文件夹
     *
     * @author LiuYi
     * @data 2015年7月4日 下午6:24:58
     * @param context
     * @return
     */
    private final String PAPERFILE = "wanlaiwanqu";//磁盘中文件夹名称
    private String filepath = "";//下载文件路径

    public String isExistsFilePath() {
        if (!TextUtils.isEmpty(getSDPath())) {
            filepath = getSDPath() + File.separator + PAPERFILE;
            File file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
            }
            return filepath;
        }
        return filepath;
    }

    /**
     * 启动apk的安装界面
     */
    public void startInstallActivity(String path) {
        try {
            if (TextUtils.isEmpty(path)) {
                return;
            }
            File apkFile = new File(path);
            if (!apkFile.exists()) {
                return;
            }
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mContext.startIntentSender(pendingIntent.getIntentSender(), null, Intent.FLAG_ACTIVITY_NEW_TASK,
                    Intent.FLAG_ACTIVITY_NEW_TASK, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //由过去的某一时间,计算距离当前的时间
    public String CalculateTime(String time) {
        long nowTime = System.currentTimeMillis();  //获取当前时间的毫秒数
        String msg = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//指定时间格式
        Date setTime = null;  //指定时间
        try {
            setTime = sdf.parse(time);  //将字符串转换为指定的时间格式
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long reset = setTime.getTime();   //获取指定时间的毫秒数
        long dateDiff = nowTime - reset;
        if (dateDiff < 0) {
            msg = "输入的时间不对";
        } else {
            long dateTemp1 = dateDiff / 1000; //秒
            long dateTemp2 = dateTemp1 / 60; //分钟
            long dateTemp3 = dateTemp2 / 60; //小时
            long dateTemp4 = dateTemp3 / 24; //天数
            long dateTemp5 = dateTemp4 / 30; //月数
            long dateTemp6 = dateTemp5 / 12; //年数
            if (dateTemp6 > 0) {
                msg = dateTemp6 + "年前";
            } else if (dateTemp5 > 0) {
                msg = dateTemp5 + "个月前";
            } else if (dateTemp4 > 0) {
                msg = dateTemp4 + "天前";
            } else if (dateTemp3 > 0) {
                msg = dateTemp3 + "小时前";
            } else if (dateTemp2 > 0) {
                msg = dateTemp2 + "分钟前";
            } else if (dateTemp1 > 0) {
                msg = "刚刚";
            }
        }
        return msg;
    }

    public void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 4;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }
        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(15, 15, 15, 15);
        listView.setLayoutParams(params);

    }
}
