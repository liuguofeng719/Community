package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ApartmentOwnerPhoneNumberVo;
import com.joinsmile.community.bean.BaseInfoVo;
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
 * Created by liuguofeng719 on 2016/7/14.
 *
 * @desc 绑定业主手机
 */
public class ApartmentOwnerPhoneNumberActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_select_location)
    TextView tvSelectLocation;
    @InjectView(R.id.tv_phone_start)
    TextView tv_phone_start;
    @InjectView(R.id.ed_verifyCode)
    EditText ed_verifyCode;
    @InjectView(R.id.tv_phone_end)
    EditText tv_phone_end;
    @InjectView(R.id.btn_verify_code)
    TextView btn_verify_code;
    @InjectView(R.id.tv_proxy_role)
    TextView tv_proxy_role;

    CountTimer countTimer;
    String verifyCode;

    private Bundle extras;
    private String phoneNumber;
    private String lastFour;
    private String numberId;
    private Dialog dialog;
    private int stringExtra = 0;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    /**
     * 选择代理角色
     */
    @OnClick(R.id.tv_proxy_role)
    public void choiceProxyRole() {
        Bundle bundle = new Bundle();
        bundle.putInt("role", stringExtra);
        readyGoForResult(WheelPickerActivity.class, 10, bundle);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        stringExtra = extras.getInt("role");
        if (stringExtra == 0) {
            tv_proxy_role.setText("业主本人");
        } else if (stringExtra == 1) {
            tv_proxy_role.setText("代理业主");
        } else if (stringExtra == 2) {
            tv_proxy_role.setText("家庭成员");
        }
    }

    @OnClick(R.id.btn_verify_code)
    public void verifyCode() {
        if (NetUtils.isNetworkAvailable(this)) {
            if (validateUserInfo()) return;
            countTimer.start();//开启验证码
            Call<MessageVo> callMsg = getApisNew().sendInvitedCode(phoneNumber).clone();
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
            CommonUtils.make(this, getString(R.string.no_network));
        }
    }

    private boolean validateUserInfo() {
        if (TextUtils.isEmpty(tv_phone_end.getText())) {
            CommonUtils.make(this, getString(R.string.phone_end_empty));
            return true;
        }
        if (tv_phone_end.getText().toString().trim().length() < 4) {
            CommonUtils.make(this, getString(R.string.phone_last_four));
            return true;
        }
        if (!tv_phone_end.getText().toString().trim().equals(lastFour)) {
            CommonUtils.make(this, getString(R.string.phone_last_four));
            return true;
        }
        return false;
    }

    @OnClick(R.id.tv_done_text)
    public void doneText() {
        //validate
        if (TextUtils.isEmpty(stringExtra + "")) {
            CommonUtils.make(this, "请选择角色");
            return;
        }

        if (TextUtils.isEmpty(btn_verify_code.getText())) {
            CommonUtils.make(this, "验证码不能为空");
            return;
        }
        if (!verifyCode.equals(ed_verifyCode.getText().toString())) {
            CommonUtils.make(this, "验证码不正确");
            return;
        }
        //submit
        dialog = CommonUtils.createDialog(this);
        dialog.show();

        Call<BaseInfoVo> infoVoCall = getApisNew().bindingHouse(
                AppPreferences.getString("userId"),
                numberId,
                stringExtra + 1).clone();

        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                CommonUtils.dismiss(dialog);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    CommonUtils.make(ApartmentOwnerPhoneNumberActivity.this, getString(R.string.bind_house_msg));
                    readyGoThenKill_CLEAR_TOP(MyVillageActivity.class);
                }
            }

            @Override
            public void onFailure(Call<BaseInfoVo> call, Throwable t) {
                CommonUtils.dismiss(dialog);
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.apartment_owner_phone_number_activity;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.select_house_number));
        tvSelectLocation.setText(extras.getString("doneLocation"));
        //获取门牌号
        numberId = extras.getString("numberId");
        countTimer = new CountTimer(Constants.reg.millisInFuture, Constants.reg.countDownInterval);
        getVillage(numberId);
    }


    private void getVillage(String numberID) {
        Call<ApartmentOwnerPhoneNumberVo> callVillage = getApisNew().getApartmentOwnerPhoneNumber(numberID).clone();
        callVillage.enqueue(new Callback<ApartmentOwnerPhoneNumberVo>() {
            @Override
            public void onResponse(Call<ApartmentOwnerPhoneNumberVo> call, Response<ApartmentOwnerPhoneNumberVo> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    phoneNumber = response.body().getPhoneNumber();
                    lastFour = phoneNumber.substring(7, 11);//最后4位
                    tv_phone_start.setText(phoneNumber.substring(0, 3) + "****");//获取前面三位
                } else {
                    CommonUtils.make(ApartmentOwnerPhoneNumberActivity.this, CommonUtils.getCodeToStr(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ApartmentOwnerPhoneNumberVo> call, Throwable t) {
                if ("timeout".equals(t.getMessage())) {
                    CommonUtils.make(ApartmentOwnerPhoneNumberActivity.this, "网络请求超时");
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

    @Override
    protected void onDestroy() {
        if (countTimer != null) {
            countTimer.cancel();
            countTimer = null;
        }
        if (dialog != null) {
            CommonUtils.dismiss(dialog);
        }
        super.onDestroy();
    }
}
