package com.joinsmile.community.ui.base;

import android.text.TextUtils;

import com.joinsmile.community.api.ApisNew;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.RetrofitUtils;
import com.joinsmile.community.view.base.BaseView;

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
}
