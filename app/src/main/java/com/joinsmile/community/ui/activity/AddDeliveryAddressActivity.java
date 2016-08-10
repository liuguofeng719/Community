package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.BaseInfoVo;
import com.joinsmile.community.bean.ReceiveProductAddressVo;
import com.joinsmile.community.bean.ReceiverAddressVo;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;

import java.io.Serializable;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/8/5.
 */
public class AddDeliveryAddressActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.edit_delivery_name)
    EditText editDeliveryName;
    @InjectView(R.id.edit_phone)
    EditText editPhone;
    @InjectView(R.id.edit_area)
    TextView editArea;
    @InjectView(R.id.edit_details_address)
    EditText editDetailsAddress;
    @InjectView(R.id.checkbox_default)
    CheckBox checkboxDefault;
    private Bundle extras;
    private Dialog mDialog;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    //选择省市级
    @OnClick(R.id.edit_area)
    public void editArea() {
        readyGo(ProvinceActivity.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        String cityId = extras.getString("cityId");
        String cityName = extras.getString("cityName");
        editArea.setText(cityName);
        editArea.setTag(cityId);
    }

    //保存提货地址
    @OnClick(R.id.tv_save)
    public void tvSave() {
        if (TextUtils.isEmpty(editDeliveryName.getText())) {
            CommonUtils.make(this, "请输入收货人的名字");
            return;
        }
        if (TextUtils.isEmpty(editPhone.getText())) {
            CommonUtils.make(this, "请输入电话号码");
            return;
        }
        if (TextUtils.isEmpty(editArea.getText())) {
            CommonUtils.make(this, "请选择省市区");
            return;
        }
        if (TextUtils.isEmpty(editDetailsAddress.getText())) {
            CommonUtils.make(this, "请输入详细地址");
            return;
        }

        mDialog = CommonUtils.showDialog(this);
        mDialog.show();
        final ReceiverAddressVo receiver = new ReceiverAddressVo();
        receiver.setUserID(AppPreferences.getString("userId"));
        receiver.setCountyID(editArea.getTag().toString());
        receiver.setIsDefault(checkboxDefault.isChecked() ? 1 : 0);
        receiver.setLinkman(editDeliveryName.getText().toString());
        receiver.setPhoneNumber(editPhone.getText().toString());
        receiver.setStreet(editDetailsAddress.getText().toString());
        Call<BaseInfoVo> infoVoCall = getApisNew().addUserReceiveProductAddress(receiver).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                CommonUtils.dismiss(mDialog);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    CommonUtils.make(AddDeliveryAddressActivity.this, "修改地址成功");
                }
            }

            @Override
            public void onFailure(Call<BaseInfoVo> call, Throwable t) {
                CommonUtils.dismiss(mDialog);
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.add_delivery_address_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.edit_delivery_address));
        Serializable serializable = extras.getSerializable("receiveAddress");
        if (serializable != null) {
            ReceiveProductAddressVo receiveAddress = (ReceiveProductAddressVo) serializable;
            editDeliveryName.setText(receiveAddress.getLinkman());
            editPhone.setText(receiveAddress.getPhoneNumber());
            editDetailsAddress.setText(receiveAddress.getAddress());
            editArea.setText(receiveAddress.getCityName());
            editArea.setTag(receiveAddress.getCityId());
            if (receiveAddress.getIsDefault() == 1) {
                checkboxDefault.setChecked(true);
            } else {
                checkboxDefault.setChecked(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            CommonUtils.dismiss(mDialog);
        }
        super.onDestroy();
    }
}
