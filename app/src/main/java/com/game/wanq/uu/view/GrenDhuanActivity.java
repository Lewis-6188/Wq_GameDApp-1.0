package com.game.wanq.uu.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.game.wanq.app.R;

/**
 * Created by Lewis.Liu on 2018/1/11.
 */

public class GrenDhuanActivity extends Activity implements View.OnClickListener {
    private LinearLayout dhuanfanhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_gren_dhuan_layout);
        dhuanfanhui = (LinearLayout) findViewById(R.id.dhuanfanhui);
        dhuanfanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dhuanfanhui:
                finish();
                break;
        }
    }
}
