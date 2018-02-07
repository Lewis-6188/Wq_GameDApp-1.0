package com.game.wanq.uu.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.game.wanq.app.R;
import com.game.wanq.uu.model.Config;
import com.game.wanq.uu.url.ParaTran;
import com.game.wanq.uu.url.ReqCallBack;
import com.game.wanq.uu.url.RequestManager;
import com.game.wanq.uu.utils.PermissionUtils;
import com.game.wanq.uu.utils.SPrefUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lewis.Liu on 2018/1/11.
 */

public class GrenXGActivity extends Activity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private LinearLayout xgaifanhui, xgaiRgist;
    private TextView grenRqi, grensex;
    private int mYear, mMonth, mDay;
    private final int DATE_DIALOG = 1;
    private CircleImageView usertouxiang;
    private Dialog tougaiicon;
    private Button xiangce, paizao, quxiao;
    private Bitmap head;//头像Bitmap
    private String path = "/sdcard/wanquHead/", filePtha;//sd路径
    private EditText nic, gphone, gerenjianj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanq_gren_xgai_layout);
        xgaifanhui = (LinearLayout) findViewById(R.id.xgaifanhui);
        xgaiRgist = (LinearLayout) findViewById(R.id.xgaiRgist);

        grenRqi = (TextView) findViewById(R.id.grenRqi);
        grensex = (TextView) findViewById(R.id.grensex);
        nic = (EditText) findViewById(R.id.nic);
        gphone = (EditText) findViewById(R.id.gphone);
        gerenjianj = (EditText) findViewById(R.id.gerenjianj);

        xgaifanhui.setOnClickListener(this);
        xgaiRgist.setOnClickListener(this);
        grenRqi.setOnClickListener(this);
        grensex.setOnClickListener(this);
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        usertouxiang = (CircleImageView) this.findViewById(R.id.usertouxiang);
        usertouxiang.setOnClickListener(this);

        SPrefUtils mSp = SPrefUtils.getInstance(this);
        String touxiang = mSp.getString(mSp.USER_ICON, "");
        if (!TextUtils.isEmpty(touxiang)) {
            Glide.with(this).load(touxiang).priority(Priority.HIGH).into(usertouxiang);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xgaifanhui:
                finish();
                break;
            case R.id.xgaiRgist:
                try {
                    String qiri = grenRqi.getText().toString();
                    String sex = grensex.getText().toString();
                    String niceng = nic.getText().toString();
                    String phone = gphone.getText().toString();
                    String jianjie = gerenjianj.getText().toString();
                    if (!TextUtils.isEmpty(filePtha) || !TextUtils.isEmpty(sex) ||
                            !TextUtils.isEmpty(niceng) || !TextUtils.isEmpty(phone) ||
                            !TextUtils.isEmpty(jianjie)) {
                        int ssex = 1;
                        if (!TextUtils.isEmpty(sex)) {
                            if (sex.equals("男")) {
                                ssex = 1;
                            } else {
                                ssex = 0;
                            }
                        }
                        List<File> files = new ArrayList<>();
                        files.add(new File(filePtha));
                        setUpdate(niceng, qiri, ssex, phone, jianjie, files);
                    } else {
                        Toast.makeText(this, "小主未作任何修改哟~", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.grenRqi:
                showDialog(DATE_DIALOG);
                break;
            case R.id.grensex:
                change_sex();
                break;
            case R.id.usertouxiang:
                writeExternalStorage();
                readPhoneState();
                showCamera();
                break;
            case R.id.xiangce:
                tougaiicon.dismiss();
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);//返回被选中项的URI
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//得到所有图片的URI
                startActivityForResult(intent1, 1);
                break;
            case R.id.paizao:
                tougaiicon.dismiss();
                try {
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//开启相机应用程序获取并返回图片（capture：俘获）
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "head.png")));//指明存储图片或视频的地址URI
                    startActivityForResult(intent2, 2);//采用ForResult打开
                } catch (Exception e) {
                    Toast.makeText(this, "相机无法启动，请先开启相机权限", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.quxiao:
                tougaiicon.dismiss();
                break;
        }
    }

    private void showGHICON() {
        tougaiicon = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.wanq_genghuan_usericon, null);
        tougaiicon.setContentView(inflate);
        xiangce = (Button) inflate.findViewById(R.id.xiangce);
        paizao = (Button) inflate.findViewById(R.id.paizao);
        quxiao = (Button) inflate.findViewById(R.id.quxiao);
        xiangce.setOnClickListener(this);
        paizao.setOnClickListener(this);
        quxiao.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels - 80;
        inflate.setLayoutParams(layoutParams);
        Window dialogWindow = tougaiicon.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        tougaiicon.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1://从相册里面取相片的返回结果
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2://相机拍照后的返回结果
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.png");
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
                }
                break;
            case 3://调用系统裁剪图片后
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        setPicToView(head);//保存在SD卡中
                        usertouxiang.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //调用系统的裁剪
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");//找到指定URI对应的资源图片
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// aspectX aspectY 是宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);//进入系统裁剪图片的界面
    }

//    //把bitmap转换成字符串
//    private String bitmapToString(Bitmap bitmap) {
//        String string = null;
//        ByteArrayOutputStream btString = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btString);
//        byte[] bytes = btString.toByteArray();
//        string = Base64.encodeToString(bytes, Base64.DEFAULT);
//        return string;
//    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd卡是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建以此File对象为名（path）的文件夹
        String fileName = path + "head.png";//图片名字
        try {
            b = new FileOutputStream(fileName);
            // mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件（compress：压缩）
            mBitmap.compress(Bitmap.CompressFormat.PNG, 30, b);// 把数据写入文件（compress：压缩）
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();//关闭流
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            filePtha = fileName;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    public void change_sex() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //定义一个AlertDialog
        String[] strarr = {"男", "女"};
        builder.setItems(strarr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String sex = "2";
                // 自动生成的方法存根
                if (arg1 == 0) {//男
                    grensex.setText("男");
                    sex = "1";
                } else {//女
                    grensex.setText("女");
                    sex = "2";
                }
            }
        });
        builder.show();
    }

    //设置日期 利用StringBuffer追加
    public void display() {
        grenRqi.setText(new StringBuffer().append(mYear).append("年").append(mMonth + 1).append("月").append(mDay).append("日"));
    }


    public void showCamera() {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
    }

    public void readPhoneState() {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_READ_PHONE_STATE, mPermissionGrant);
    }


    public void writeExternalStorage() {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
    }


    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    break;
                case PermissionUtils.CODE_CAMERA:
                    showGHICON();
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(final int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    private void setUpdate(String nicename, String birthday, Integer sex, String phone, String intro, List<File> files) {
        try {
            RequestManager.getInstance(this).httpPostFile(Config.getupdateUserData, ParaTran.getInstance(this).
                    setupdateUserData(nicename, birthday, sex, phone, intro), files, new ReqCallBack() {
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
