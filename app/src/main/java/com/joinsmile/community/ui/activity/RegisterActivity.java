package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.AuthticationVo;
import com.joinsmile.community.bean.BaseInfoVo;
import com.joinsmile.community.bean.MessageVo;
import com.joinsmile.community.common.Constants;
import com.joinsmile.community.netstatus.NetUtils;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    @InjectView(R.id.ed_user_phone)
    EditText ed_user_phone;
    @InjectView(R.id.ed_user_pwd)
    EditText ed_user_pwd;
    @InjectView(R.id.ed_verifyCode)
    EditText ed_verifyCode;
    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.btn_submit)
    TextView btn_submit;
    @InjectView(R.id.btn_verify_code)
    TextView btn_verify_code;
    CountTimer countTimer;
    String verifyCode;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.register_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        countTimer = new CountTimer(Constants.reg.millisInFuture, Constants.reg.countDownInterval);
        this.tv_header_title.setText(getString(R.string.login_reg));
        this.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimer.cancel();
                finish();
            }
        });
        this.btn_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isNetworkAvailable(RegisterActivity.this)) {
                    if (validateUserInfo()) return;
                    countTimer.start();//开启验证码
                    Call<MessageVo> callMsg = getApisNew().getVerifyCode(ed_user_phone.getText().toString()).clone();
                    callMsg.enqueue(new Callback<MessageVo>() {
                        @Override
                        public void onResponse(Call<MessageVo> call, Response<MessageVo> response) {
                            verifyCode = response.body().getVerifyCode();
                        }

                        @Override
                        public void onFailure(Call<MessageVo> call, Throwable t) {

                        }
                    });
                } else {
                    CommonUtils.make(RegisterActivity.this, getString(R.string.no_network));
                }
            }
        });

        this.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isNetworkAvailable(RegisterActivity.this)) {
                    if (validateSubmit()) {
                        final Dialog mDialog = CommonUtils.showDialog(RegisterActivity.this, "正在注册并登录中");
                        mDialog.show();
                        Call<BaseInfoVo> callRegister = getApisNew().register(
                                ed_user_phone.getText().toString(),
                                ed_user_pwd.getText().toString(),
                                ed_verifyCode.getText().toString()).clone();
                        callRegister.enqueue(new Callback<BaseInfoVo>() {
                            @Override
                            public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                                countTimer.cancel();
                                if (response.isSuccessful() && response.body().isSuccessfully()) {

                                    //注册成功并且登陆
                                    Call<AuthticationVo> callLogin = getApisNew().authentication(ed_user_phone.getText().toString(), ed_user_pwd.getText().toString()).clone();
                                    callLogin.enqueue(new Callback<AuthticationVo>() {
                                        @Override
                                        public void onResponse(Call<AuthticationVo> call, Response<AuthticationVo> response) {
                                            CommonUtils.dismiss(mDialog);
                                            if (response.isSuccessful() && response.body().isSuccessfully()) {
                                                AppPreferences.putString("userId", response.body().getUserID());
                                                AppPreferences.putString("userPhone", ed_user_phone.getText().toString());
                                                AppPreferences.putObject(response.body());
                                                readyGoThenKill(IndexActivity.class);
                                            } else {
                                                CommonUtils.make(RegisterActivity.this, response.body().getErrorMessage());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<AuthticationVo> call, Throwable t) {
                                            CommonUtils.dismiss(mDialog);
                                        }
                                    });
                                } else {
                                    CommonUtils.dismiss(mDialog);
                                    if (response.body() != null) {
                                        BaseInfoVo body = response.body();
                                        CommonUtils.make(RegisterActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                                    } else {
                                        CommonUtils.make(RegisterActivity.this, CommonUtils.getCodeToStr(response.code()));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseInfoVo> call, Throwable t) {
                                countTimer.cancel();
                                CommonUtils.dismiss(mDialog);
                            }
                        });
                    }
                } else {
                    CommonUtils.make(RegisterActivity.this, getString(R.string.no_network));
                }
            }
        });
    }

    class CountTimer extends CountDownTimer {
        /**
         * @param millisInFuture    时间间隔是多长的时间
         * @param countDownInterval 回调onTick方法，没隔多久执行一次
         */
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // 间隔时间结束的时候调用的方法
        @Override
        public void onTick(long millisUntilFinished) {
            // 更新页面的组件
            btn_verify_code.setText(millisUntilFinished / 1000 + "秒后发送");
            btn_verify_code.setClickable(false);
        }

        // 间隔时间内执行的操作
        @Override
        public void onFinish() {
            btn_verify_code.setText(getString(R.string.login_reg_code));
            btn_verify_code.setClickable(true);
        }
    }


    private boolean validateUserInfo() {
        if (TextUtils.isEmpty(ed_user_phone.getText())) {
            CommonUtils.make(RegisterActivity.this, getString(R.string.login_phone_empty));
            return true;
        }
        if (!CommonUtils.isMobile(ed_user_phone.getText().toString())) {
            CommonUtils.make(RegisterActivity.this, getString(R.string.login_phone_ok));
            return true;
        }
        return false;
    }

    private boolean validateSubmit() {
        if (validateUserInfo()) return false;
        if (TextUtils.isEmpty(ed_user_pwd.getText())) {
            CommonUtils.make(RegisterActivity.this, getString(R.string.login_pwd_empty));
            return false;
        }
        if (ed_user_pwd.getText().length() < 6) {
            CommonUtils.make(RegisterActivity.this, getString(R.string.login_pwd_length));
            return false;
        }
        if (TextUtils.isEmpty(ed_verifyCode.getText())) {
            CommonUtils.make(RegisterActivity.this, getString(R.string.login_reg_code_empty));
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (countTimer != null) {
            countTimer.cancel();
            countTimer = null;
        }
        super.onDestroy();
    }
}
