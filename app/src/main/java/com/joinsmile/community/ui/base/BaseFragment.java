package com.joinsmile.community.ui.base;

import com.joinsmile.community.api.ApisNew;
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
}
