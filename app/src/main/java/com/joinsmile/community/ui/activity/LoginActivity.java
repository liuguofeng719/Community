package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.AuthticationVo;
import com.joinsmile.community.netstatus.NetUtils;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.TLog;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.edit_phone)
    EditText editPhone;
    @InjectView(R.id.edit_pwd)
    EditText editPwd;
    private Dialog mDialog;
    private Bundle extras;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //注册
    @OnClick(R.id.tv_register)
    public void tvRegister() {
        readyGo(RegisterActivity.class);
    }

    //找回密码
    @OnClick(R.id.tv_forgot_pwd)
    public void tvForgotPwd() {
        Bundle bundle = new Bundle();
        bundle.putString("title",getString(R.string.login_button_rest));
        readyGo(RestAndForgotActivity.class,bundle);
    }

    //登录
    @OnClick(R.id.tv_login_in)
    public void tvLoginIn() {
        mDialog = CommonUtils.showDialog(this, "正在登录...");
        mDialog.show();
        if (NetUtils.isNetworkAvailable(LoginActivity.this)) {
            if (validate()) {
                Call<AuthticationVo> userCall = getApisNew().authentication(editPhone.getText().toString(), editPwd.getText().toString()).clone();
                userCall.enqueue(new Callback<AuthticationVo>() {
                    @Override
                    public void onResponse(Call<AuthticationVo> call, Response<AuthticationVo> response) {
                        CommonUtils.dismiss(mDialog);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                            AuthticationVo userVo = response.body();
                            TLog.d(TAG_LOG, userVo.toString());
                            AppPreferences.putString("userId", userVo.getUserID());
                            AppPreferences.putString("phoneNumber", userVo.getPhoneNumber());
                            AppPreferences.putObject(userVo);
                            finish();
                        } else {
                            if (response.body() != null) {
                                AuthticationVo body = response.body();
                                CommonUtils.make(LoginActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                            } else {
                                CommonUtils.make(LoginActivity.this, CommonUtils.getCodeToStr(response.code()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthticationVo> call, Throwable t) {
                        CommonUtils.dismiss(mDialog);
                        if ("timeout".equals(t.getMessage())) {
                            CommonUtils.make(LoginActivity.this, "网络请求超时");
                        }
                    }
                });
            }
        } else {
            CommonUtils.dismiss(mDialog);
            CommonUtils.make(LoginActivity.this, getString(R.string.no_network));
        }
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            CommonUtils.dismiss(mDialog);
        }
        super.onDestroy();
    }

    private boolean validate() {
        if (TextUtils.isEmpty(editPhone.getText())) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_phone_empty));
            return false;
        }
        if (!CommonUtils.isMobile(editPhone.getText().toString())) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_phone_ok));
            return false;
        }
        if (TextUtils.isEmpty(editPwd.getText())) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_pwd_empty));
            return false;
        }
        if (editPwd.getText().length() < 6) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_pwd_length));
            return false;
        }
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.login_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.login_in));
    }

}
