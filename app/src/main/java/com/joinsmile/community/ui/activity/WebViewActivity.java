package com.joinsmile.community.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.base.BaseActivity;

import butterknife.InjectView;

public class WebViewActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @InjectView(R.id.iv_preview)
    WebView iv_preview;
    Bundle bundle;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.bundle = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.web_view;
    }

    @Override
    protected View getLoadingTargetView() {
        return iv_preview;
    }

    @Override
    protected void initViewsAndEvents() {

        tv_header_title.setText(bundle.getString("title"));
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_preview.getSettings().setJavaScriptEnabled(true);
        iv_preview.getSettings().setDefaultTextEncodingName("gbk");
        iv_preview.getSettings().setSupportZoom(false);
        iv_preview.getSettings().setLoadWithOverviewMode(true);
        iv_preview.loadUrl(bundle.getString("navUrl"));
        iv_preview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        iv_preview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(bundle.getString("navUrl"));
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading(getString(R.string.common_loading_message));
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideLoading();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && iv_preview.canGoBack()) {
            iv_preview.goBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
