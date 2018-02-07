package com.game.wanq.uu.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.ReqDialog;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.UtilsTools;
import com.tuo.customview.VerificationCodeView;

import org.json.JSONObject;

public class GrenActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {
    private ImageView titleLimage, titleRgeisicon, clossyzm;
    private EditText regtextPhone;
    private TextView titleContText, textJgao, textZhangh, showPhone, timeCXfsong;
    private Button regBtn;
    private LinearLayout yzmcwu, titleRLayout;
    private Dialog yanzmadia;
    private ReqDialog mDialog;
    private int mCount = 60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mDialog.dismiss();
                    showYzma(msg.obj.toString());
                    break;
                case 2:
                    Toast.makeText(GrenActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    private boolean isYanzmaGet = false;
    private VerificationCodeView icv;
    private Runnable mNumberRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCount > 0 && handler != null) {
                isYanzmaGet = true;
                mCount--;
                String str = "<font color=\"red\">" + mCount + "</font><font color=\"gray\">" + "秒后可从新发送" + "</font>";
                timeCXfsong.setText(Html.fromHtml(str));
                handler.postDelayed(mNumberRunnable, 1000);
            } else {
                isYanzmaGet = false;
                timeCXfsong.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                timeCXfsong.setText("重新发送");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_gren_layout);
        titleLimage = (ImageView) this.findViewById(R.id.title_gren).findViewById(R.id.titleLimage);
        titleRgeisicon = (ImageView) this.findViewById(R.id.title_gren).findViewById(R.id.titleRgeisicon);
        titleContText = (TextView) this.findViewById(R.id.title_gren).findViewById(R.id.titleContText);
        titleRLayout = (LinearLayout) this.findViewById(R.id.title_gren).findViewById(R.id.titleRLayout);
        regtextPhone = (EditText) this.findViewById(R.id.regtextPhone);
        textJgao = (TextView) this.findViewById(R.id.textJgao);
        textZhangh = (TextView) this.findViewById(R.id.textZhangh);
        regBtn = (Button) this.findViewById(R.id.regBtn);
        titleContText.setText("新用户注册");
        titleLimage.setVisibility(View.GONE);
        titleRgeisicon.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.shezhi));
        regtextPhone.requestFocus();
        regtextPhone.addTextChangedListener(this);
        regtextPhone.setOnFocusChangeListener(this);
        regBtn.setOnClickListener(this);
        textZhangh.setOnClickListener(this);
        titleRLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regBtn:
                String phone = regtextPhone.getText().toString().trim();
                if (!isMobileNO(phone)) {
                    textJgao.setVisibility(View.VISIBLE);
                } else {
                    textJgao.setVisibility(View.GONE);
                    mCount = 60;
                    getSend(phone);
                }
                break;
            case R.id.titleRLayout:
                UtilsTools.getInstance(GrenActivity.this).startClass(SZHIActivity.class);
                break;
            case R.id.textZhangh:
                UtilsTools.getInstance(GrenActivity.this).startClass(LoginActivity.class);
                break;
            case R.id.clossyzm:
                yanzmadia.dismiss();
                if (mDialog != null)
                    mDialog.dismiss();
                break;
            case R.id.timeCXfsong:
                if (isYanzmaGet) {
                    return;
                }
                if (mCount == 0 && timeCXfsong.getText().equals("重新发送")) {
                    mCount = 60;
                }
                yanzmadia.dismiss();
                showYzma(regtextPhone.getText().toString().trim());
                getSend(regtextPhone.getText().toString().trim());
                break;
            default:
        }
    }

    @Override // EditText焦点的监听
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override // 对EditText内容的实时监听第二个执行
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override// 第一个执行
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s))
            regBtn.setSelected(true);
    }

    @Override// 第三个执行
    public void afterTextChanged(Editable s) {

    }

    //    验证手机格式
    public boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    ------------------------------------------------
    13(老)号段：130、131、132、133、134、135、136、137、138、139
    14(新)号段：145、147
    15(新)号段：150、151、152、153、154、155、156、157、158、159
    17(新)号段：170、171、173、175、176、177、178
    18(3G)号段：180、181、182、183、184、185、186、187、188、189
    */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    private void showYzma(final String phone) {
        if (yanzmadia != null) yanzmadia.dismiss();
        yanzmadia = new Dialog(GrenActivity.this, R.style.ActionSheetDialogStyle);
        yanzmadia.setCanceledOnTouchOutside(false);
        View inflate = LayoutInflater.from(GrenActivity.this).inflate(R.layout.wanq_identifying_dialog_code, null);
        yanzmadia.setContentView(inflate);
        clossyzm = (ImageView) inflate.findViewById(R.id.clossyzm);
        showPhone = (TextView) inflate.findViewById(R.id.showPhone);
        timeCXfsong = (TextView) inflate.findViewById(R.id.timeCXfsong);
        yzmcwu = (LinearLayout) inflate.findViewById(R.id.yzmcwu);
        icv = (VerificationCodeView) inflate.findViewById(R.id.icv);
        icv.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String yzm = icv.getInputContent();
                if (yzm.trim().length() == 6) {
                    getRegister(phone, yzm);
                }
            }

            @Override
            public void deleteContent() {
            }
        });
        String str = "<font color=\"red\">" + mCount + "</font><font color=\"gray\">" + "秒后可从新发送" + "</font>";
        timeCXfsong.setText(Html.fromHtml(str));
        handler.postDelayed(mNumberRunnable, 1000);
        showPhone.setText("+86" + phone);
        timeCXfsong.setOnClickListener(this);
        clossyzm.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels - 80;
        inflate.setLayoutParams(layoutParams);
        Window dialogWindow = yanzmadia.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        yanzmadia.show();
    }

    private void getSend(final String phone) {
        try {
            mDialog = new ReqDialog(GrenActivity.this, R.style._dialog);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            RequestManager.getInstance(GrenActivity.this).httpPost(Config.getSend, ParaTran.getInstance(GrenActivity.this).setPhone(phone), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("验证码错误")) {
                                yzmcwu.setVisibility(View.VISIBLE);
                            }
                            textJgao.setVisibility(View.VISIBLE);
                            textJgao.setText(msg);
                            mDialog.dismiss();
                            yanzmadia.dismiss();
                        }
                        Message message = Message.obtain();
                        message.obj = phone;
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String result) {
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mDialog.dismiss();
        }
    }

    private void getRegister(String phone, String yzm) {
        try {
            mDialog = new ReqDialog(GrenActivity.this, R.style._dialog);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            RequestManager.getInstance(GrenActivity.this).httpPost(Config.getRegister, ParaTran.getInstance(GrenActivity.this).setRgister(phone, yzm), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        if (resultType == 1) {
                            mDialog.dismiss();
                            return;
                        }
                        Message message = Message.obtain();
                        message.obj = "注册成功，默认密码:手机号+123456";
                        message.what = 2;
                        handler.sendMessage(message);
                        yanzmadia.dismiss();
                        UtilsTools.getInstance(GrenActivity.this).startClass(MainActivity.class);
                        mDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String result) {
                    mDialog.dismiss();
                    yanzmadia.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mDialog.dismiss();
            yanzmadia.dismiss();
        }
    }
}
