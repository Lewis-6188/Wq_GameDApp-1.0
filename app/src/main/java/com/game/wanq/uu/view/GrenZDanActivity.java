package com.game.wanq.uu.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.game.wanq.app.R;

/**
 * Created by Lewis.Liu on 2018/1/11.
 */

public class GrenZDanActivity extends Activity implements View.OnClickListener {
    private LinearLayout zdanfanhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_gren_zdan_layout);
        zdanfanhui = (LinearLayout) findViewById(R.id.zdanfanhui);
        zdanfanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zdanfanhui:
                finish();
                break;
        }
    }
}
