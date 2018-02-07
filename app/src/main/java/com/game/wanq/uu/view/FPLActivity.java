package com.game.wanq.uu.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.view.whget.PLRatingBarView;

import org.json.JSONObject;

/**
 * Created by Lewis.Liu on 2018/1/18.
 */

public class FPLActivity extends Activity implements View.OnClickListener {
    private LinearLayout fplfanhui, fplfabiao;
    private TextView fpltext, ffplshebei;
    private PLRatingBarView ffplfanshu;
    private EditText ffbianneirong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_fpl_fabiao_layout);
        fplfanhui = (LinearLayout) this.findViewById(R.id.fplfanhui);
        fplfabiao = (LinearLayout) this.findViewById(R.id.fplfabiao);
        fpltext = (TextView) this.findViewById(R.id.fpltext);
        fplfanhui.setOnClickListener(this);
        fplfabiao.setOnClickListener(this);
        fpltext.setText(GameXqingActivity.tGame.name);
        ffplfanshu = (PLRatingBarView) this.findViewById(R.id.ffplfanshu);
        ffplfanshu.setStar(getIntent().getIntExtra("ffenshu", 1), true);
        ffplshebei = (TextView) this.findViewById(R.id.ffplshebei);
        ffplshebei.setText(android.os.Build.MODEL);
        ffbianneirong = (EditText) this.findViewById(R.id.ffbianneirong);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fplfanhui:
                finish();
                break;
            case R.id.fplfabiao:
                String text = ffbianneirong.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    setPLFB("", 1, GameXqingActivity.tGame.pid, "", ffplfanshu.getStarCount() + "", text);
                } else {
                    Toast.makeText(this, "编辑点内容吧", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setPLFB(String ruid, int gameType,
                         String typeObjid, String rpid, String score, String content) {
        try {
            RequestManager.getInstance(this).httpPost(Config.getPLFB, ParaTran.getInstance(this).
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
                        finish();
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
