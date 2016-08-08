package com.joinsmile.community.ui.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.UserApartmentPropertyOrdersResp;
import com.joinsmile.community.bean.UserApartmentPropertyOrdersVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/8/7.
 * 我的缴费
 */
public class MyPropertyPaymentActivity extends BaseActivity {

    @InjectView(R.id.lv_list_view)
    ListView lv_list_view;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    private ListViewDataAdapter<UserApartmentPropertyOrdersVo> listViewDataAdapter;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.myproperty_payment_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_list_view;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("我的缴费");
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<UserApartmentPropertyOrdersVo>() {
            @Override
            public ViewHolderBase<UserApartmentPropertyOrdersVo> createViewHolder(int position) {
                return new ViewHolderBase<UserApartmentPropertyOrdersVo>() {
                    TextView tv_pay_desc;
                    TextView tv_street;
                    TextView tv_pay_type;
                    TextView tv_pay_date;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.myproperty_payment_item, null);
                        tv_pay_desc = ButterKnife.findById(view, R.id.tv_pay_desc);
                        tv_street = ButterKnife.findById(view, R.id.tv_street);
                        tv_pay_type = ButterKnife.findById(view, R.id.tv_pay_type);
                        tv_pay_date = ButterKnife.findById(view, R.id.tv_pay_date);
                        return view;
                    }

                    @Override
                    public void showData(int position, UserApartmentPropertyOrdersVo itemData) {
                        tv_pay_desc.setText(itemData.getOrderDescription());
                        tv_street.setText(itemData.getApartmentFullName());
                        String s = TextUtils.isEmpty(itemData.getPaymentWay()) ? "" : itemData.getPaymentWay();
                        tv_pay_type.setText("支付方式：" + s);
                        if (!TextUtils.isEmpty(itemData.getPayDate())) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                            try {
                                Date parse = simpleDateFormat.parse(itemData.getPayDate());
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
                                tv_pay_date.setText(simpleDateFormat1.format(parse));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });
        lv_list_view.setAdapter(listViewDataAdapter);
        MyPropertyPaymentOrder();
    }

    //我的缴费订单
    private void MyPropertyPaymentOrder() {
        showLoading(getString(R.string.common_loading_message));
        Call<UserApartmentPropertyOrdersResp<List<UserApartmentPropertyOrdersVo>>> propertyOrdersRespCall =
                getApisNew().getUserApartmentPropertyOrders(AppPreferences.getString("userId")).clone();
        propertyOrdersRespCall.enqueue(new Callback<UserApartmentPropertyOrdersResp<List<UserApartmentPropertyOrdersVo>>>() {
            @Override
            public void onResponse(Call<UserApartmentPropertyOrdersResp<List<UserApartmentPropertyOrdersVo>>> call,
                                   Response<UserApartmentPropertyOrdersResp<List<UserApartmentPropertyOrdersVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    List<UserApartmentPropertyOrdersVo> propertyOrderList = response.body().getPropertyOrderList();
                    ArrayList<UserApartmentPropertyOrdersVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(propertyOrderList);
                }
            }

            @Override
            public void onFailure(Call<UserApartmentPropertyOrdersResp<List<UserApartmentPropertyOrdersVo>>> call, Throwable t) {
                hideLoading();
            }
        });

    }
}
