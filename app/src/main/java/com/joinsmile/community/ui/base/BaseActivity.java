package com.joinsmile.community.ui.base;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.joinsmile.community.CommunityApplication;
import com.joinsmile.community.R;
import com.joinsmile.community.api.ApisNew;
import com.joinsmile.community.netstatus.NetUtils;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.RetrofitUtils;
import com.joinsmile.community.view.base.BaseView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import retrofit2.Retrofit;

public abstract class BaseActivity extends BaseFragmentActivity implements BaseView {

    protected ApisNew getApisNew(){
        Retrofit retrofit = RetrofitUtils.getRfHttpsInstance(ApisNew.BASE_URI);
        return retrofit.create(ApisNew.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected CommunityApplication getBaseApplication() {
        return (CommunityApplication) getApplication();
    }

    @Override
    public void showError(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showNetError() {
        toggleNetworkError(true, null);
    }

    @Override
    public void showLoading(String msg) {
        toggleShowLoading(true, msg);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }

    @NonNull
    protected DisplayImageOptions.Builder getBuilder() {
        final DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.considerExifParams(true);
        builder.showImageForEmptyUri(R.drawable.no_banner);
        builder.showImageOnFail(R.drawable.no_banner);
        builder.showImageOnLoading(R.drawable.no_banner);
        return builder;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    //检查登录
    public boolean checkLogin() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            return false;
        }
        return true;
    }
}
