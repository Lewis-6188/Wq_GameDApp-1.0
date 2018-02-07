package com.game.wanq.uu.view.photo.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.game.wanq.uu.view.GrenSMRZActivity;
import com.game.wanq.uu.view.photo.adapter.FolderAdapter;
import com.game.wanq.uu.view.photo.util.Bimp;
import com.game.wanq.uu.view.photo.util.PublicWay;
import com.game.wanq.uu.view.photo.util.Res;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:48:06
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getInstance(this).getLayoutID("plugin_camera_image_file"));
		PublicWay.activityList.add(this);
		mContext = this;
		bt_cancel = (Button) findViewById(Res.getInstance(this).getWidgetID("cancel"));
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(Res.getInstance(this).getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(Res.getInstance(this).getWidgetID("headerTitle"));
		textView.setText(Res.getInstance(this).getString("photo"));
		folderAdapter = new FolderAdapter(this);
		gridView.setAdapter(folderAdapter);
	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			//清空选择的图片
			Bimp.tempSelectBitmap.clear();
			Intent intent = new Intent();
			intent.setClass(mContext, GrenSMRZActivity.class);
			startActivity(intent);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(mContext, GrenSMRZActivity.class);
			startActivity(intent);
		}
		
		return true;
	}

}
