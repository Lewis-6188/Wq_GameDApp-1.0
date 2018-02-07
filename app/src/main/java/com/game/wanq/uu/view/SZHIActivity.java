package com.game.wanq.uu.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.game.wanq.app.R;

/**
 * Created by Lewis.Liu on 2018/1/6.
 */

public class SZHIActivity extends Activity implements View.OnClickListener {
    private LinearLayout shezhifanhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_gren_szhi_layout);
        shezhifanhui = (LinearLayout) this.findViewById(R.id.shezhifanhui);
        shezhifanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shezhifanhui:
                finish();
                break;
        }
    }
}
