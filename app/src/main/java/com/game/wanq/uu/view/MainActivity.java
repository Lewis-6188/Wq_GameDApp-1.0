package com.game.wanq.uu.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;
import com.game.wanq.uu.view.bmenu.FragmentSwitchTool;

public class MainActivity extends Activity {
    private LinearLayout llShouye, llFaxian, llPhbang, llGren;
    private ImageView ivShouye, ivFaxian, ivPhbang, ivGren;
    private TextView tvShouye, tvFaxian, tvPhbang, tvGren;
    private FragmentSwitchTool tool;
    public static LinearLayout tabLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLinear = (LinearLayout) findViewById(R.id.tabLinear);
        llShouye = (LinearLayout) findViewById(R.id.llShouye);
        llFaxian = (LinearLayout) findViewById(R.id.llFaxian);
        llPhbang = (LinearLayout) findViewById(R.id.llPhbang);
        llGren = (LinearLayout) findViewById(R.id.llGren);
        ivShouye = (ImageView) findViewById(R.id.ivShouye);
        ivFaxian = (ImageView) findViewById(R.id.ivFaxian);
        ivPhbang = (ImageView) findViewById(R.id.ivPhbang);
        ivGren = (ImageView) findViewById(R.id.ivGren);
        tvShouye = (TextView) findViewById(R.id.tvShouye);
        tvFaxian = (TextView) findViewById(R.id.tvFaxian);
        tvPhbang = (TextView) findViewById(R.id.tvPhbang);
        tvGren = (TextView) findViewById(R.id.tvGren);
        tool = new FragmentSwitchTool(getFragmentManager(), R.id.flContainer);
        tool.setClickableViews(llShouye, llFaxian, llPhbang, llGren);
        tool.addSelectedViews(new View[]{ivShouye, tvShouye}).addSelectedViews(new View[]{ivFaxian, tvFaxian})
                .addSelectedViews(new View[]{ivPhbang, tvPhbang}).addSelectedViews(new View[]{ivGren, tvGren});
        tool.setFragments(ShouyeFragment.class, FaxianFragment.class, PhbangFragment.class, LoginCGFragment.class);
        tool.changeTag(llShouye);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
