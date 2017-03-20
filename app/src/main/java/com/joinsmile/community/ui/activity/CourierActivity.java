package com.joinsmile.community.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ServiceCompanyDetail;
import com.joinsmile.community.bean.ServiceCompanyVo;
import com.joinsmile.community.bean.ServiceOrderVo;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lgfcxx on 2017/3/15.
 */

public class CourierActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.txt_content)
    TextView txtContent;
    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.edit_noteNumber)
    EditText editNoteNumber;
    @InjectView(R.id.edit_amount)
    EditText editAmount;

    private Bundle bundle;
    private String companyId;

    @OnClick(R.id.btn_back)
    public void btnBack(){
        finish();
    }

    //生成订单
    @OnClick(R.id.tv_payment)
    public void tvPayment(){
        Editable noteNumber = editNoteNumber.getText();
        Editable amount = editAmount.getText();

        if (TextUtils.isEmpty(noteNumber)) {
            CommonUtils.make(mContext, "单号不能为空");
            return;
        }

        if (TextUtils.isEmpty(amount)) {
            CommonUtils.make(mContext, "金额不能为空");
            return;
        }

        Call<ServiceOrderVo<ServiceOrderVo.ServiceOrderInfo>> serviceOrder = getApisNew().createServiceOrder(
                AppPreferences.getString("userId"), companyId, editNoteNumber.toString(), editAmount.toString()
        );

        serviceOrder.enqueue(new Callback<ServiceOrderVo<ServiceOrderVo.ServiceOrderInfo>>() {
            @Override
            public void onResponse(Call<ServiceOrderVo<ServiceOrderVo.ServiceOrderInfo>> call,
                                   Response<ServiceOrderVo<ServiceOrderVo.ServiceOrderInfo>> response) {
                if (response.isSuccessful()) {
                    ServiceOrderVo<ServiceOrderVo.ServiceOrderInfo> body = response.body();
                    if(body.isSuccessfully()){
                        ServiceOrderVo.ServiceOrderInfo orderInfo = body.getOrderInfo();
                        if (orderInfo != null) {
                            String orderId = orderInfo.getOrderId();
                            String totalPrice = orderInfo.getOrderTotalPrice();
                            Bundle b = new Bundle();
                            b.putString("orderId", orderId);
                            b.putString("money", totalPrice);
                            readyGo(ServicePayActivity.class, b);
                        }
                    }else{
                        CommonUtils.make(mContext, body.getErrorMessage());
                    }

                } else {
                    CommonUtils.make(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<ServiceOrderVo<ServiceOrderVo.ServiceOrderInfo>> call, Throwable t) {
                CommonUtils.make(mContext, t.getMessage());
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.courier_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

        companyId = bundle.getString("companyId");
        String companyName = bundle.getString("companyName");
        tvHeaderTitle.setText(companyName + "服务");
        Call<ServiceCompanyDetail> companyDetails = getApisNew().getCompanyDetails(companyId);
        companyDetails.enqueue(new Callback<ServiceCompanyDetail>() {
            @Override
            public void onResponse(Call<ServiceCompanyDetail> call, Response<ServiceCompanyDetail> response) {
                if (response.isSuccessful()) {

                    ServiceCompanyDetail body = response.body();

                    if (body.isSuccessfully() && body != null) {

                        final DisplayImageOptions.Builder builder = getBuilder();

                        ServiceCompanyVo.ServiceCompany serviceCompany = body.getServiceCompany();
                        String description = serviceCompany.getDescription();
                        String companyLogo = serviceCompany.getCompanyLogo();

                        txtContent.setText(description);
                        ImageLoader.getInstance().displayImage(companyLogo, imageView, builder.build());

                    } else {
                        CommonUtils.make(mContext, body.getErrorMessage());
                    }
                } else {
                    CommonUtils.make(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<ServiceCompanyDetail> call, Throwable t) {
                CommonUtils.make(mContext, t.getMessage());
            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        bundle = extras;
    }
}
