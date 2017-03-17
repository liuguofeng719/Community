package com.joinsmile.community.ui.base;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.joinsmile.community.R;
import com.joinsmile.community.api.ApisNew;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.RetrofitUtils;
import com.joinsmile.community.view.base.BaseView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import retrofit2.Retrofit;

public abstract class BaseFragment extends BaseLazyFragment implements BaseView {

    protected ApisNew getApisNew(){
        Retrofit retrofit = RetrofitUtils.getRfHttpsInstance(ApisNew.BASE_URI);
        return retrofit.create(ApisNew.class);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        toggleShowLoading(true, null);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }

    //检查登录
    public boolean checkLogin() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            return false;
        }
        return true;
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
}
