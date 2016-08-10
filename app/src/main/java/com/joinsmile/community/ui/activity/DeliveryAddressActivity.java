package com.joinsmile.community.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ReceiveProductAddressResp;
import com.joinsmile.community.bean.ReceiveProductAddressVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/8/5.
 */
public class DeliveryAddressActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    @InjectView(R.id.ly_empty_data)
    LinearLayout ivEmptyData;

    private ListViewDataAdapter<ReceiveProductAddressVo> listViewDataAdapter;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    //添加收货地址
    @OnClick(R.id.tv_add_delivery_address)
    public void tvAddDeliveryAddress() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("receiveAddress", null);
        readyGo(AddDeliveryAddressActivity.class, bundle);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.delivery_address_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.delivery_address));
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ReceiveProductAddressVo>() {
            @Override
            public ViewHolderBase<ReceiveProductAddressVo> createViewHolder(int position) {
                return new ViewHolderBase<ReceiveProductAddressVo>() {

                    TextView tv_address;
                    TextView tv_link_phone;
                    TextView tv_address_detail;
                    LinearLayout fl_delivery_address;
                    ImageView iv_edit;
                    CheckBox checkbox;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.delivery_address_item_activity, null);
                        tv_address = ButterKnife.findById(view, R.id.tv_address);
                        tv_link_phone = ButterKnife.findById(view, R.id.tv_link_phone);
                        tv_address_detail = ButterKnife.findById(view, R.id.tv_address_detail);
                        fl_delivery_address = ButterKnife.findById(view, R.id.fl_delivery_address);
                        iv_edit = ButterKnife.findById(view, R.id.iv_edit);
                        checkbox = ButterKnife.findById(view, R.id.checkbox);
                        return view;
                    }

                    @Override
                    public void showData(int position, final ReceiveProductAddressVo itemData) {
                        tv_address.setText("收货地址：" + itemData.getLinkman() + " ");
                        tv_link_phone.setText(" " + itemData.getPhoneNumber());
                        tv_address_detail.setText(itemData.getAddress());
                        if (itemData.getIsDefault() == 1) {
                            checkbox.setChecked(true);
                            checkbox.setVisibility(View.VISIBLE);
                        }
                        iv_edit.setTag(itemData);
                        iv_edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                ReceiveProductAddressVo tag = (ReceiveProductAddressVo) v.getTag();
                                bundle.putSerializable("receiveAddress", tag);
                                readyGo(AddDeliveryAddressActivity.class, bundle);
                            }
                        });
                        fl_delivery_address.setTag(itemData.getAddressID());
                        fl_delivery_address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("addressId", v.getTag().toString());
                                intent.putExtra("addressName", itemData.getAddress());
                                setResult(15, intent);
                                finish();
                            }
                        });
                    }
                };
            }
        });
        lvListView.setAdapter(listViewDataAdapter);
    }

    //获取收货地址
    private void getListAddress() {
        showLoading(getString(R.string.common_loading_message));
        Call<ReceiveProductAddressResp<List<ReceiveProductAddressVo>>> addressRespCall = getApisNew().getUserReceiveProductAddress(AppPreferences.getString("userId")).clone();
        addressRespCall.enqueue(new Callback<ReceiveProductAddressResp<List<ReceiveProductAddressVo>>>() {
            @Override
            public void onResponse(Call<ReceiveProductAddressResp<List<ReceiveProductAddressVo>>> call,
                                   Response<ReceiveProductAddressResp<List<ReceiveProductAddressVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    ReceiveProductAddressResp<List<ReceiveProductAddressVo>> productAddressResp = response.body();
                    List<ReceiveProductAddressVo> productAddressList = productAddressResp.getReceiveProductAddressList();
                    if (productAddressList.size() == 0) {
                        ivEmptyData.setVisibility(View.INVISIBLE);
                    }
                    ArrayList<ReceiveProductAddressVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(productAddressList);
                    listViewDataAdapter.notifyDataSetChanged();
                } else {
                    ivEmptyData.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ReceiveProductAddressResp<List<ReceiveProductAddressVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListAddress();
    }
}
