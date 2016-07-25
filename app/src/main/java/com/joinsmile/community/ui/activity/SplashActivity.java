package com.joinsmile.community.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.CommonUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void initViewsAndEvents() {
        final ImageView iv_bg = (ImageView) findViewById(R.id.splash_image);

//        Call<VersionVo> versionVoCall = getApisNew().getVersion().clone();
//        versionVoCall.enqueue(new Callback<VersionVo>() {
//            @Override
//            public void onResponse(Call<VersionVo> call, Response<VersionVo> response) {
//                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
//                    TLog.d("version", "" + getVersionCode()+" =serverCode= "+response.body().getVersionCode());
//                    if (response.body().getVersionCode() > getVersionCode()) {
//                        new UpdateManager(SplashActivity.this,
//                                response.body().getAppUrl(),
//                                new IncomingHandler()).showDownloadDialog();
//                    } else {
//                        iv_bg.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent(SplashActivity.this, IndexActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }, 3000);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<VersionVo> call, Throwable t) {
//
//            }
//        });
        iv_bg.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, IndexActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }

    class IncomingHandler extends Handler {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 12) {
                CommonUtils.make(SplashActivity.this, "不能打开升级地址");
            } else if (msg.what == 13) {
                CommonUtils.make(SplashActivity.this, "找不到升级地址");
            } else if (msg.what == 14) {
                CommonUtils.make(SplashActivity.this, "下载升级包失败，请检查磁盘空间或者是否关闭了权限");
            }
        }
    }


    private int getVersionCode() {
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
