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
import com.game.wanq.uu.utils.SPrefUtils;
import com.game.wanq.uu.utils.UtilsTools;
import com.tuo.customview.VerificationCodeView;

import org.json.JSONObject;

/**
 * Created by Lewis.Liu on 2018/1/4.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private ImageView titleLimage, titleRgeisicon, userIcon, clossyzm;
    private TextView titleContText, phoneText, userLoginText, userBZ, logintextJgao, LoginPhone, Loginprd, timeCXfsong, showPhone;
    private LinearLayout phoneLogin, userLogin, userPrdLayout, titleLLayout, titleRLayout, yzmcwu;
    private View phoneView, userLoginView, userZhao, userPrdView;
    private Button LoginBtn;
    private ReqDialog mDialog;
    private boolean isLoginType = false;
    private Dialog yanzmadia;
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
                case 3:
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.wanq_gren_login_layout);
        titleLimage = (ImageView) this.findViewById(R.id.title_grenLogin).findViewById(R.id.titleLimage);
        titleRgeisicon = (ImageView) this.findViewById(R.id.title_grenLogin).findViewById(R.id.titleRgeisicon);
        titleContText = (TextView) this.findViewById(R.id.title_grenLogin).findViewById(R.id.titleContText);
        titleLLayout = (LinearLayout) this.findViewById(R.id.title_grenLogin).findViewById(R.id.titleLLayout);
        titleRLayout = (LinearLayout) this.findViewById(R.id.title_grenLogin).findViewById(R.id.titleRLayout);
        phoneLogin = (LinearLayout) this.findViewById(R.id.phoneLogin);
        userLogin = (LinearLayout) this.findViewById(R.id.userLogin);
        phoneText = (TextView) this.findViewById(R.id.phoneText);
        phoneView = (View) this.findViewById(R.id.phoneView);
        userLoginText = (TextView) this.findViewById(R.id.userLoginText);
        userLoginView = (View) this.findViewById(R.id.userLoginView);
        userIcon = (ImageView) this.findViewById(R.id.userIcon);
        userBZ = (TextView) this.findViewById(R.id.userBZ);
        userPrdLayout = (LinearLayout) this.findViewById(R.id.userPrdLayout);
        userZhao = (View) this.findViewById(R.id.userZhao);
        userPrdView = (View) this.findViewById(R.id.userPrdView);
        logintextJgao = (TextView) this.findViewById(R.id.logintextJgao);
        LoginPhone = (TextView) this.findViewById(R.id.LoginPhone);
        Loginprd = (TextView) this.findViewById(R.id.Loginprd);
        LoginBtn = (Button) this.findViewById(R.id.LoginBtn);
        titleContText.setText("用户登录");
        titleLimage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.fanhui));
        titleRgeisicon.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.shezhi));
        titleLLayout.setOnClickListener(this);
        titleRLayout.setOnClickListener(this);
        phoneLogin.setOnClickListener(this);
        userLogin.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
        LoginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tetx = s.toString();
                if (!TextUtils.isEmpty(tetx) && tetx.length() == 11 && !isLoginType) {
                    LoginBtn.setSelected(true);
                } else {
                    LoginBtn.setSelected(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Loginprd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tetx = s.toString();
                if (!TextUtils.isEmpty(tetx) && isLoginType) {
                    LoginBtn.setSelected(true);
                } else {
                    LoginBtn.setSelected(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleLLayout:
                finish();
                break;
            case R.id.titleRLayout:
                UtilsTools.getInstance(this).startClass(SZHIActivity.class);
                break;
            case R.id.timeCXfsong:
                String text = timeCXfsong.getText().toString();
                if (text.equals("重新发送")) {
                    String phone = LoginPhone.getText().toString();
                    String prd = Loginprd.getText().toString();
                    if (isLoginType) {
                        getLogin(phone, "", prd);
                    } else {
                        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
                            if (!isMobileNO(phone)) {
                                logintextJgao.setVisibility(View.VISIBLE);
                            } else {
                                logintextJgao.setVisibility(View.GONE);
                                mCount = 60;
                                getSend(phone);
                            }
                        }
                    }
                }
                break;
            case R.id.phoneLogin:
                phoneText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                phoneView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                userLoginText.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                userLoginView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                userIcon.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logshouji));
                userBZ.setVisibility(View.VISIBLE);
                userPrdLayout.setVisibility(View.INVISIBLE);
                userPrdView.setVisibility(View.INVISIBLE);
                userZhao.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                LoginBtn.setText("获取验证码");
                LoginPhone.setHint("请输入手机号");
                isLoginType = false;
                LoginPhone.setText("");
                Loginprd.setText("");
                LoginBtn.setSelected(false);
                break;
            case R.id.userLogin:
                phoneText.setTextColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                phoneView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                userLoginText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                userLoginView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                userIcon.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.zhanghao));
                userBZ.setVisibility(View.GONE);
                userPrdLayout.setVisibility(View.VISIBLE);
                userPrdView.setVisibility(View.VISIBLE);
                userPrdView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                userZhao.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTXTHse));
                LoginBtn.setText("登录");
                LoginPhone.setHint("请输入账号");
                isLoginType = true;
                LoginPhone.setText("");
                Loginprd.setText("");
                LoginBtn.setSelected(false);
                break;
            case R.id.LoginBtn:
                String phone = LoginPhone.getText().toString();
                String prd = Loginprd.getText().toString();
                if (isLoginType) {
                    getLogin(phone, "", prd);
                } else {
                    if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
                        if (!isMobileNO(phone)) {
                            logintextJgao.setVisibility(View.VISIBLE);
                        } else {
                            logintextJgao.setVisibility(View.GONE);
                            mCount = 60;
                            getSend(phone);
                        }
                    }
                }
                break;
            case R.id.clossyzm:
                yanzmadia.dismiss();
                if (mDialog != null)
                    mDialog.dismiss();
                break;
        }
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

    private void getSend(final String phone) {
        try {
            mDialog = new ReqDialog(this, R.style._dialog);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            RequestManager.getInstance(this).httpPost(Config.getSend, ParaTran.getInstance(this).setPhone(phone), new ReqCallBack() {
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
                            logintextJgao.setVisibility(View.VISIBLE);
                            logintextJgao.setText(msg);
                            mDialog.dismiss();
                            yanzmadia.dismiss();
                            return;
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
                    yanzmadia.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mDialog.dismiss();
            yanzmadia.dismiss();
        }
    }

    private void showYzma(final String phone) {
        yanzmadia = new Dialog(this, R.style.ActionSheetDialogStyle);
        yanzmadia.setCanceledOnTouchOutside(false);
        View inflate = LayoutInflater.from(this).inflate(R.layout.wanq_identifying_dialog_code, null);
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
                    getLogin(phone, yzm, "");
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

    private void getLogin(String phone, String code, String prd) {
        try {
            mDialog = new ReqDialog(this, R.style._dialog);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            RequestManager.getInstance(this).httpPost(Config.getLogin, ParaTran.getInstance(this).setLogin(phone, code, prd), new ReqCallBack() {
                @Override
                public void onReqSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultType = jsonObject.getInt("result");
                        String msg = jsonObject.getString("msg");
                        Message message = Message.obtain();
                        message.obj = msg;
                        message.what = 3;
                        handler.sendMessage(message);
                        mDialog.dismiss();
                        if (resultType == 1) {
                            return;
                        }
                        yanzmadia.dismiss();
                        JSONObject object = new JSONObject(jsonObject.getString("datas"));
                        String pid = object.getString("pid").trim();
                        String loginName = object.getString("loginName").trim();
                        String nickName = object.getString("nickName").trim();
                        String icon = object.getString("icon").trim();
                        String phone = object.getString("phone").trim();
                        String rigstTime = object.getString("rigstTime").trim();
                        SPrefUtils mSP = SPrefUtils.getInstance(LoginActivity.this);
                        mSP.putString(mSP.USER_ID, pid);
                        mSP.putString(mSP.USER_NAME, loginName);
                        mSP.putString(mSP.USER_NICKNAME, nickName);
                        mSP.putString(mSP.USER_ICON, icon);
                        mSP.putString(mSP.USER_PHONE, phone);
                        mSP.putString(mSP.USER_RIGSTTIME, rigstTime);
                        finish();
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
