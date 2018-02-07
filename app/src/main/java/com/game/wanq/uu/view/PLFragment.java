package com.game.wanq.uu.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.TbXqItemAdapter;
import com.game.wanq.uu.utils.SPrefUtils;
import com.game.wanq.uu.view.whget.PLRatingBarView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lewis.Liu on 2017/12/27.
 */

public class PLFragment extends Fragment implements View.OnClickListener {
    public PLFragment newInstance() {
        return new PLFragment();
    }

    private ListView tabplListView;
    private TbXqItemAdapter tbXqItemAdapter;
    private Dialog dialogpxu, dialogsxuan;
    private Button btnmr, btnzx, btnrm, btn_cancel;
    private TextView textPxu, textSxuan;
    private LinearLayout quanbupl, wxin, sxin, sanxin, erxin, yixin, quxiao;
    private TextView fplfenshu;
    private LinearLayout fpllayout;
    private PLRatingBarView plfenshu;
    private int level = 0;
    private TextView usernamepl;
    private SPrefUtils mSP;
    private CircleImageView profile_tabplimage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_gamexq_tabpl, container, false);
        this.mSP = SPrefUtils.getInstance(getActivity());
        textPxu = (TextView) view.findViewById(R.id.textPxu);
        textSxuan = (TextView) view.findViewById(R.id.textSxuan);
        usernamepl = (TextView) view.findViewById(R.id.usernamepl);
        profile_tabplimage = (CircleImageView) view.findViewById(R.id.profile_tabplimage);
        textPxu.setOnClickListener(this);
        textSxuan.setOnClickListener(this);

        fplfenshu = (TextView) view.findViewById(R.id.fplfenshu);
        fplfenshu.setText(GameXqingActivity.tGame.score + "");

        fpllayout = (LinearLayout) view.findViewById(R.id.fpllayout);
        fpllayout.setOnClickListener(this);
        plfenshu = (PLRatingBarView) view.findViewById(R.id.plfenshu);
        plfenshu.setOnRatingListener(new PLRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int ratingScore) {
                level = ratingScore;
            }
        });

        usernamepl.setText(mSP.getString(mSP.USER_NAME, ""));
        Glide.with(getActivity()).load(mSP.getString(mSP.USER_ICON, "")).priority(Priority.HIGH).into(profile_tabplimage);
        tabplListView = (ListView) view.findViewById(R.id.tabplListView);
        tbXqItemAdapter = new TbXqItemAdapter(getActivity(), GameXqingActivity.tComments, GameXqingActivity.tGame);
        tabplListView.setAdapter(tbXqItemAdapter);
        return view;
    }

    private void showPxu() {
        dialogpxu = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.wanq_gamexq_tabpl_pxu, null);
        dialogpxu.setContentView(inflate);
        btnmr = (Button) inflate.findViewById(R.id.btnmr);
        btnzx = (Button) inflate.findViewById(R.id.btnzx);
        btnrm = (Button) inflate.findViewById(R.id.btnrm);
        btn_cancel = (Button) inflate.findViewById(R.id.btn_cancel);
        btnmr.setOnClickListener(this);
        btnzx.setOnClickListener(this);
        btnrm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels - 80;
        inflate.setLayoutParams(layoutParams);
        Window dialogWindow = dialogpxu.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialogpxu.show();
    }

    private void showSxuan() {
        dialogsxuan = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.wanq_gamexq_tabpl_sxuan, null);
        dialogsxuan.setContentView(inflate);
        quanbupl = (LinearLayout) inflate.findViewById(R.id.quanbupl);
        wxin = (LinearLayout) inflate.findViewById(R.id.wxin);
        sxin = (LinearLayout) inflate.findViewById(R.id.sxin);
        sanxin = (LinearLayout) inflate.findViewById(R.id.sanxin);
        erxin = (LinearLayout) inflate.findViewById(R.id.erxin);
        yixin = (LinearLayout) inflate.findViewById(R.id.yixin);
        quxiao = (LinearLayout) inflate.findViewById(R.id.quxiao);
        quanbupl.setOnClickListener(this);
        wxin.setOnClickListener(this);
        sxin.setOnClickListener(this);
        sanxin.setOnClickListener(this);
        erxin.setOnClickListener(this);
        yixin.setOnClickListener(this);
        quxiao.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels - 80;
        inflate.setLayoutParams(layoutParams);
        Window dialogWindow = dialogsxuan.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialogsxuan.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textSxuan:
                showSxuan();
                break;
            case R.id.quanbupl:
                dialogsxuan.dismiss();
                break;
            case R.id.wxin:
                dialogsxuan.dismiss();
                break;
            case R.id.sxin:
                dialogsxuan.dismiss();
                break;
            case R.id.sanxin:
                dialogsxuan.dismiss();
                break;
            case R.id.erxin:
                dialogsxuan.dismiss();
                break;
            case R.id.yixin:
                dialogsxuan.dismiss();
                break;
            case R.id.quxiao:
                dialogsxuan.dismiss();
                break;
            case R.id.textPxu:
                showPxu();
                break;
            case R.id.btnmr:
                dialogpxu.dismiss();
                break;
            case R.id.btnzx:
                dialogpxu.dismiss();
                break;
            case R.id.btnrm:
                dialogpxu.dismiss();
                break;
            case R.id.btn_cancel:
                dialogpxu.dismiss();
                break;
            case R.id.fpllayout:
                Intent intent = new Intent(getActivity(), FPLActivity.class);
                intent.putExtra("ffenshu", level);
                startActivity(intent);
                break;
            default:
        }
    }
}
