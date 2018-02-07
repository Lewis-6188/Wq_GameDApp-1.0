package com.game.wanq.uu.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.game.wanq.app.R;
import com.game.wanq.uu.utils.SPrefUtils;
import com.game.wanq.uu.utils.UtilsTools;

/**
 * Created by Lewis.Liu on 2018/1/23.
 */

public class WebActivity extends Activity {
    private WebView mWebView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SPrefUtils mSP = SPrefUtils.getInstance(this);
        setContentView(R.layout.wanq_web_layout);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.requestFocus();
        mWebView.getSettings().setJavaScriptEnabled(true);//启用支持JavaScript
        mWebView.getSettings().setDomStorageEnabled(true);//启用支持DOM Storage
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        //mWebView.addJavascriptInterface(new AndroidObject(), "AndroidObj");//16以前
        mWebView.addJavascriptInterface(this, "AndroidObj");//16之后
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                final String msg = mSP.getString(mSP.USER_ID, "");
                if (!TextUtils.isEmpty(msg)) {
                    int version = Build.VERSION.SDK_INT;
                    if (version < 18) {
                        mWebView.loadUrl("javascript:setUserId('" + msg + "')");
                    } else {
                        mWebView.evaluateJavascript("javascript:setUserId('" + msg + "')", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //此处为 js 返回的结果
                            }
                        });
                    }
                } else {
                    UtilsTools.getInstance(WebActivity.this).startClass(LoginActivity.class);
                }
            }
        });
        mWebView.loadUrl("http://121.40.138.84:9999/white/static/app/html/game_FqLe/message.html");   //加载web资源
    }

    @JavascriptInterface
    public void ViewBack() {
        finish();
    }
}
