package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.BaseInfoVo;
import com.joinsmile.community.bean.MessageInviteVo;
import com.joinsmile.community.bean.MessageVo;
import com.joinsmile.community.common.Constants;
import com.joinsmile.community.netstatus.NetUtils;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/15.
 * 邀请成员
 */
public class InviteMemberActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.edit_phone_number)
    EditText edit_phone_number;
    @InjectView(R.id.ed_verifyCode)
    EditText ed_verifyCode;
    @InjectView(R.id.tv_verify_code)
    TextView tv_verify_code;
    CountTimer countTimer;
    String verifyCode;
    private Bundle extras;
    private Dialog dialog;
    private String userId;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    //邀请
    @OnClick(R.id.tv_done_text)
    public void tvDoneText() {
        if (validateUserInfo()) {
            return;
        }
        if (TextUtils.isEmpty(ed_verifyCode.getText())) {
            CommonUtils.make(this, getString(R.string.login_reg_code_empty));
            return;
        }
        if (!verifyCode.equals(ed_verifyCode.getText().toString())) {
            CommonUtils.make(this, getString(R.string.login_reg_code_isequals));
            return;
        }
        bindHouse();
    }

    /**
     * 邀请成员默认为家庭成员
     */
    private void bindHouse() {
        dialog = CommonUtils.createDialog(this);
        Call<BaseInfoVo> infoVoCall = getApisNew().bindingHouse(
                userId,
                extras.getString("numberID"),
                3).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                CommonUtils.dismiss(dialog);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    CommonUtils.make(InviteMemberActivity.this, getString(R.string.invite_member_msg));
                    readyGoThenKill(MyVillageActivity.class);
                } else {
                    CommonUtils.make(InviteMemberActivity.this, response.body().getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseInfoVo> call, Throwable t) {
                CommonUtils.dismiss(dialog);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            CommonUtils.dismiss(dialog);
        }
        super.onDestroy();
    }

    @OnClick(R.id.tv_verify_code)
    public void verifyCode() {
        if (NetUtils.isNetworkAvailable(this)) {
            if (validateUserInfo()) return;
            countTimer.start();//开启验证码
            Call<MessageInviteVo> callMsg = getApisNew().sendInvitedCode(edit_phone_number.getText().toString()).clone();
            callMsg.enqueue(new Callback<MessageInviteVo>() {
                @Override
                public void onResponse(Call<MessageInviteVo> call, Response<MessageInviteVo> response) {
                    MessageInviteVo body = response.body();
                    if (response.isSuccessful() && body != null && body.isSuccessfully()) {
                        verifyCode = body.getVerifyCode();
                        userId = body.getUserId();
                    } else {
                        CommonUtils.make(InviteMemberActivity.this, body.getErrorMessage());
                    }
                }

                @Override
                public void onFailure(Call<MessageInviteVo> call, Throwable t) {
                    CommonUtils.make(InviteMemberActivity.this, t.getMessage());
                }
            });
        } else {
            CommonUtils.make(this, getString(R.string.no_network));
        }
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
            tv_verify_code.setText(millisUntilFinished / 1000 + "秒后发送");
            tv_verify_code.setClickable(false);
        }

        // 间隔时间内执行的操作
        @Override
        public void onFinish() {
            tv_verify_code.setText(getString(R.string.login_reg_code));
            tv_verify_code.setClickable(true);
        }
    }


    private boolean validateUserInfo() {
        if (TextUtils.isEmpty(edit_phone_number.getText())) {
            CommonUtils.make(this, getString(R.string.login_phone_empty));
            return true;
        }
        if (!CommonUtils.isMobile(edit_phone_number.getText().toString())) {
            CommonUtils.make(this, getString(R.string.login_phone_ok));
            return true;
        }
        return false;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.invite_member_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText(getString(R.string.invite_member));
        countTimer = new CountTimer(Constants.reg.millisInFuture, Constants.reg.countDownInterval);
    }
}
