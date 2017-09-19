package com.joinsmile.community.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.ProgressWebView;

import butterknife.InjectView;

public class WebViewFileUploadActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @InjectView(R.id.iv_preview)
    ProgressWebView iv_preview;
    Bundle bundle;
    static final String TAG = "WebViewFileUploadActivity";
    @Override
    protected void getBundleExtras(Bundle extras) {
        this.bundle = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.web_view_file;
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
        iv_preview.loadUrl(bundle.getString("navUrl"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && iv_preview.canGoBack()) {
            iv_preview.goBack();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TLog.d(TAG, "requestCode=" + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == ProgressWebView.TYPE_CAMERA) { // 相册选择
                iv_preview.onActivityCallBack(true, null);
            } else if (requestCode == ProgressWebView.TYPE_GALLERY) {// 相册选择
                if (data != null) {
                    Uri uri = data.getData();
                    TLog.d(TAG, "uri=" + uri);
                    if (uri != null) {
                        iv_preview.onActivityCallBack(false, uri);
                    } else {
                        CommonUtils.make(WebViewFileUploadActivity.this, "获取数据为空");
                    }
                }
            }
        }
    }

    // 权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ProgressWebView.TYPE_REQUEST_PERMISSION) {
            iv_preview.toCamera();// 到相机
        }
    }
}
