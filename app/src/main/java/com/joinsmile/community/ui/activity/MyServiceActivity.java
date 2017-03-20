package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ServiceOrderListVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lgfcxx on 2017/3/18.
 */

public class MyServiceActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.list_view)
    ListView listView;
    private ListViewDataAdapter<ServiceOrderListVo.ServiceOrder> listViewDataAdapter;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.myservice_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return listView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("我的服务");
        showLoading(getString(R.string.common_loading_message));
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ServiceOrderListVo.ServiceOrder>() {
            @Override
            public ViewHolderBase<ServiceOrderListVo.ServiceOrder> createViewHolder(int position) {
                return new ViewHolderBase<ServiceOrderListVo.ServiceOrder>() {
                    TextView tv_content;
                    TextView tv_service_money;
                    TextView tv_noteNumber;
                    TextView tv_pay_type;
                    TextView tv_pay_date;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.myservice_item_activity, null);
                        tv_content = (TextView) view.findViewById(R.id.tv_content);
                        tv_service_money = (TextView) view.findViewById(R.id.tv_service_money);
                        tv_noteNumber = (TextView) view.findViewById(R.id.tv_noteNumber);
                        tv_pay_type = (TextView) view.findViewById(R.id.tv_pay_type);
                        tv_pay_date = (TextView) view.findViewById(R.id.tv_pay_date);
                        return view;
                    }

                    @Override
                    public void showData(int position, ServiceOrderListVo.ServiceOrder itemData) {
                        tv_content.setText(itemData.getServiceContent());
                        tv_service_money.setText("" + itemData.getTotalPrice() + "元");
                        tv_noteNumber.setText("" + itemData.getServiceNoteNumber());
                        tv_pay_type.setText(itemData.getPaymentWay());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            String format =sdf.format(sdf.parse(itemData.getPaymentDateTime()));
                            tv_pay_date.setText(format);
                        } catch (ParseException e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                };
            }
        });
        listView.setAdapter(listViewDataAdapter);
        getServiceOrders();
    }

    //获取服务订单
    private void getServiceOrders() {
        Call<ServiceOrderListVo<List<ServiceOrderListVo.ServiceOrder>>> userServiceOrders = getApisNew().getUserServiceOrders(AppPreferences.getString("userId"));
        userServiceOrders.enqueue(new Callback<ServiceOrderListVo<List<ServiceOrderListVo.ServiceOrder>>>() {
            @Override
            public void onResponse(Call<ServiceOrderListVo<List<ServiceOrderListVo.ServiceOrder>>> call,
                                   Response<ServiceOrderListVo<List<ServiceOrderListVo.ServiceOrder>>> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    ServiceOrderListVo<List<ServiceOrderListVo.ServiceOrder>> serviceOrderListVo = response.body();
                    if (serviceOrderListVo.isSuccessfully()) {
                        List<ServiceOrderListVo.ServiceOrder> serviceOrderList = serviceOrderListVo.getServiceOrderList();
                        ArrayList<ServiceOrderListVo.ServiceOrder> dataList = listViewDataAdapter.getDataList();
                        dataList.clear();
                        dataList.addAll(serviceOrderList);
                        listViewDataAdapter.notifyDataSetChanged();
                    } else {
                        CommonUtils.make(mContext, serviceOrderListVo.getErrorMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceOrderListVo<List<ServiceOrderListVo.ServiceOrder>>> call, Throwable t) {
                hideLoading();
                CommonUtils.make(mContext, t.getMessage());
            }
        });
    }
}
