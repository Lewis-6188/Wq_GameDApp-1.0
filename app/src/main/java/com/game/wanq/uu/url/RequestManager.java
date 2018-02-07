package com.game.wanq.uu.url;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lewis.Liu on 2017/12/29.
 */

public class RequestManager {
    private static RequestManager manager;

    public static RequestManager getInstance(Context context) {
        synchronized (RequestManager.class) {
            if (manager == null) manager = new RequestManager(context);
            return manager;
        }
    }

    private RequestManager(Context context) {
        this.mContext = context;
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient = new OkHttpClient.Builder()//设置请求超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.okHttpClient = new OkHttpClient.Builder()//设置缓存
                .cache(new Cache(new File(mContext.getCacheDir(), "responses"), 10 * 1024 * 1024)).build();
    }

    private Context mContext;
    private OkHttpClient okHttpClient;


    public String httpGet(String http, HashMap<String, String> params, final ReqCallBack reqCallBack) throws IOException {
        String result = "";
        StringBuilder buf = new StringBuilder();
        buf.append(http).append("?");
        if (params != null && params.size() != 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    String value = entry.getValue();
                    if (!TextUtils.isEmpty(value)) {
                        buf.append(entry.getKey()).append("=").append(URLEncoder.encode(value, "UTF-8"));// 如果请求参数中有中文，需要进行URLEncoder编码 gbk/utf8
                    }
                } catch (UnsupportedEncodingException e) {
                }
                buf.append("&");
            }
            buf.deleteCharAt(buf.length() - 1); // 在指定的索引中删除字符。任何剩余的字符左移。
        }
        Log.i("6188", "-----请求地址----->>" + buf.toString());
        Request request = new Request.Builder().get().url(buf.toString()).build();// 通过Builder辅助类构建一个Request对象
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                reqCallBack.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                reqCallBack.onReqSuccess(response.body().string());
                String headers = response.headers().toString();
            }
        });
//        Response response = okHttpClient.newCall(request).execute();// 通过同步执行获取一个Response对象
//        if (response.isSuccessful()) {  // 判断响应是否成功,如果成功的话,响应的内容会放在response.body()中
//            result = response.body().string(); // 字符串类型
//        }
        return result;
    }

    public String httpPost(String http, HashMap<String, String> params, final ReqCallBack reqCallBack) throws IOException {
        String result = "";
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() != 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    String value = entry.getValue();
                    if (!TextUtils.isEmpty(value)) {
                        String key = entry.getKey();
                        builder.add(key, URLEncoder.encode(value, "UTF-8"));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        Request request = new Request.Builder().url(http).post(builder.build()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                reqCallBack.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                reqCallBack.onReqSuccess(response.body().string());
                String headers = response.headers().toString();
            }
        });
// Response response = okHttpClient.newCall(request).execute(); // 通过同步执行获取一个Response对象
// if (response.isSuccessful()) {// 判断响应是否成功,如果成功的话,响应的内容会放在response.body()中
//            result = response.body().string();// 字符串类型
//        }
        return result;
    }

    public String httpPostFile(String http, HashMap<String, String> params, List<File> files, final ReqCallBack reqCallBack) throws IOException {
        String result = "";
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (params != null) {
            for (String key : params.keySet()) {
                multipartBodyBuilder.addFormDataPart(key, params.get(key));//URLEncoder.encode(params.get(key), "UTF-8")
            }
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (files != null) {
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart("uploadFile", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }
        Request request = new Request.Builder().url(http).post(multipartBodyBuilder.build()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                reqCallBack.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                reqCallBack.onReqSuccess(response.body().string());
                String headers = response.headers().toString();
            }
        });
        // Response response = okHttpClient.newCall(request).execute(); // 通过同步执行获取一个Response对象
        // if (response.isSuccessful()) {// 判断响应是否成功,如果成功的话,响应的内容会放在response.body()中
        // result = response.body().string();// 字符串类型
        // }
        return result;
    }

}
