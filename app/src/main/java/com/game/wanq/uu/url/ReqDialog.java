package com.game.wanq.uu.url;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.wanq.app.R;

/**
 * Created by Lewis.Liu on 2017/12/29.
 */

public class ReqDialog extends Dialog {
    public ReqDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.wanq_loading_dialog, null);// 得到加载view
        setContentView(v);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img); // main.xml中的ImageView
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.wanq_load_animation);// 加载动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);// 使用ImageView显示动画
    }
}
