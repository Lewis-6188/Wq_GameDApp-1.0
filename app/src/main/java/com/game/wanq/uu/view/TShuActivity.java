package com.game.wanq.uu.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.game.wanq.app.R;

/**
 * Created by Lewis.Liu on 2018/1/17.
 */

public class TShuActivity extends Activity implements View.OnClickListener {
    private LinearLayout tsufanhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_tshu_layout);
        tsufanhui = (LinearLayout) this.findViewById(R.id.tsufanhui);
        tsufanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tsufanhui:
                finish();
                break;
        }
    }
}
