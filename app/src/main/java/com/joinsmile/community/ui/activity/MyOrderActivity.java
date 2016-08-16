package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.BaseInfoVo;
import com.joinsmile.community.bean.ProductOrderListResp;
import com.joinsmile.community.bean.ProductOrderVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.widgets.ListViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/8/15.
 */
public class MyOrderActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.radio_group)
    RadioGroup radioGroup;
    @InjectView(R.id.lv_list_view)
    ListViewForScrollView lvListView;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    private ListViewDataAdapter<ProductOrderVo> listViewDataAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_order_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("我的订单");
        getOrderByState(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tv_obligation:
                        getOrderByState(0);//待支付
                        break;
                    case R.id.tv_receiving:
                        getOrderByState(1);//待收货
                        break;
                    case R.id.tv_completed:
                        getOrderByState(2);//已完成
                        break;
                    case R.id.tv_refund:
                        getOrderByState(3);//退款
                        break;
                }
            }
        });

        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ProductOrderVo>() {
            @Override
            public ViewHolderBase<ProductOrderVo> createViewHolder(int position) {
                return new ViewHolderBase<ProductOrderVo>() {

                    TextView tv_orderId;
                    TextView tv_order_state;
                    ListViewForScrollView list_view;
                    TextView tv_product_amount;
                    TextView tv_product_price;
                    TextView tv_order_detail;
                    TextView tv_cancel_order;
                    TextView tv_payment;
                    TextView tv_confirm_receiving;
                    TextView tv_confirm_refund;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.my_order_item, null);
                        tv_orderId = ButterKnife.findById(view, R.id.tv_orderId);
                        tv_order_state = ButterKnife.findById(view, R.id.tv_order_state);
                        list_view = ButterKnife.findById(view, R.id.list_view);
                        tv_product_amount = ButterKnife.findById(view, R.id.tv_product_amount);
                        tv_product_price = ButterKnife.findById(view, R.id.tv_product_price);
                        tv_order_detail = ButterKnife.findById(view, R.id.tv_order_detail);
                        tv_cancel_order = ButterKnife.findById(view, R.id.tv_cancel_order);
                        tv_payment = ButterKnife.findById(view, R.id.tv_payment);
                        tv_confirm_receiving = ButterKnife.findById(view, R.id.tv_confirm_receiving);
                        tv_confirm_refund = ButterKnife.findById(view, R.id.tv_confirm_refund);
                        return view;
                    }

                    @Override
                    public void showData(int position, ProductOrderVo itemData) {
                        tv_orderId.setText("订单号：" + itemData.getOrderNumber());
                        tv_order_state.setText(itemData.getOrderStateDescription());
                        tv_product_amount.setText("共" + itemData.getBuyProductList().size() + "件商品，合计");
                        tv_product_price.setText("￥" + itemData.getTotalPrice() + "(含运费)");

                        if ("0".equals(itemData.getOrderSate())) {
                            tv_confirm_receiving.setVisibility(View.GONE);
                            tv_confirm_refund.setVisibility(View.GONE);
                            tv_payment.setVisibility(View.VISIBLE);
                            tv_cancel_order.setVisibility(View.VISIBLE);
                        } else if ("1".equals(itemData.getOrderSate())) {
                            tv_confirm_receiving.setVisibility(View.VISIBLE);
                            tv_cancel_order.setVisibility(View.GONE);
                            tv_confirm_refund.setVisibility(View.GONE);
                            tv_payment.setVisibility(View.GONE);
                        } else if ("2".equals(itemData.getOrderSate())) {
                            tv_confirm_receiving.setVisibility(View.GONE);
                            tv_cancel_order.setVisibility(View.GONE);
                            tv_confirm_refund.setVisibility(View.VISIBLE);
                            tv_payment.setVisibility(View.GONE);
                        } else if ("3".equals(itemData.getOrderSate()) || "4".equals(itemData.getOrderSate())) {
                            tv_confirm_receiving.setVisibility(View.GONE);
                            tv_cancel_order.setVisibility(View.GONE);
                            tv_confirm_refund.setVisibility(View.GONE);
                            tv_payment.setVisibility(View.GONE);
                        }

                        ListViewDataAdapter<ProductOrderVo.BuyProductVo> itemProduct = new ListViewDataAdapter<>(new ViewHolderCreator<ProductOrderVo.BuyProductVo>() {
                            @Override
                            public ViewHolderBase<ProductOrderVo.BuyProductVo> createViewHolder(int position) {

                                return new ViewHolderBase<ProductOrderVo.BuyProductVo>() {
                                    ImageView iv_product_img;
                                    TextView tv_product_title;
                                    TextView tv_product_price;
                                    TextView tv_product_amount;

                                    @Override
                                    public View createView(LayoutInflater layoutInflater) {
                                        View view = layoutInflater.inflate(R.layout.my_order_confirm_item, null);
                                        tv_product_title = ButterKnife.findById(view, R.id.tv_product_title);
                                        tv_product_price = ButterKnife.findById(view, R.id.tv_product_price);
                                        tv_product_amount = ButterKnife.findById(view, R.id.tv_product_amount);
                                        iv_product_img = ButterKnife.findById(view, R.id.iv_product_img);
                                        return view;
                                    }

                                    @Override
                                    public void showData(int position, ProductOrderVo.BuyProductVo itemData) {
                                        tv_product_title.setText(itemData.getProductName());
                                        tv_product_price.setText("￥" + itemData.getUnitPrice());
                                        tv_product_amount.setText("X" + itemData.getAmount());
                                        ImageLoader.getInstance().displayImage(itemData.getProductPicture(), iv_product_img);
                                    }
                                };
                            }
                        });
                        list_view.setAdapter(itemProduct);
                        ArrayList<ProductOrderVo.BuyProductVo> dataList = itemProduct.getDataList();
                        dataList.clear();
                        dataList.addAll(itemData.getBuyProductList());

                        //订单详情页面
                        tv_order_detail.setTag(itemData.getOrderID());
                        tv_order_detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("orderId", v.getTag().toString());
                                readyGo(MyOrderDetailActivity.class, bundle);
                            }
                        });

                        //取消订单
                        tv_cancel_order.setTag(itemData.getOrderID());
                        tv_cancel_order.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Call<BaseInfoVo> infoVoCall = getApisNew().cancelOrder(v.getTag().toString()).clone();
                                infoVoCall.enqueue(new Callback<BaseInfoVo>() {
                                    @Override
                                    public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                                        if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                                            CommonUtils.make(MyOrderActivity.this, "订单取消成功");
                                            listViewDataAdapter.notifyDataSetChanged();
                                        } else {
                                            CommonUtils.make(MyOrderActivity.this, response.body().getErrorMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseInfoVo> call, Throwable t) {

                                    }
                                });
                            }
                        });

                        //去付款
                        tv_payment.setTag(itemData.getOrderID() + "," + itemData.getTotalPrice());
                        tv_payment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String string = v.getTag().toString();
                                String[] split = string.split(",");
                                Bundle bundle = new Bundle();
                                bundle.putString("orderId", split[0]);
                                bundle.putString("money", split[1]);
                                readyGo(OrderPayMentModeActivity.class, bundle);
                            }
                        });
                        //确认收货
                        tv_confirm_receiving.setTag(itemData.getOrderID());
                        tv_confirm_receiving.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Call<BaseInfoVo> infoVoCall = getApisNew().confirmReceived(v.getTag().toString()).clone();
                                infoVoCall.enqueue(new Callback<BaseInfoVo>() {
                                    @Override
                                    public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                                        if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                                            CommonUtils.make(MyOrderActivity.this, "确认收货成功");
                                            listViewDataAdapter.notifyDataSetChanged();
                                        } else {
                                            CommonUtils.make(MyOrderActivity.this, response.body().getErrorMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseInfoVo> call, Throwable t) {

                                    }
                                });
                            }
                        });
                        //退款
                        tv_confirm_refund.setTag(itemData.getOrderID());
                        tv_confirm_refund.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Call<BaseInfoVo> infoVoCall = getApisNew().applyRefund(v.getTag().toString()).clone();
                                infoVoCall.enqueue(new Callback<BaseInfoVo>() {
                                    @Override
                                    public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                                        if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                                            CommonUtils.make(MyOrderActivity.this, "申请退款成功");
                                            listViewDataAdapter.notifyDataSetChanged();
                                        } else {
                                            CommonUtils.make(MyOrderActivity.this, response.body().getErrorMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseInfoVo> call, Throwable t) {

                                    }
                                });
                            }
                        });
                    }
                };
            }
        });
        lvListView.setAdapter(listViewDataAdapter);
    }

    /**
     * 通过订单状态
     *
     * @param orderState
     */
    private void getOrderByState(int orderState) {
        showLoading(getString(R.string.common_loading_message));
        Call<ProductOrderListResp<List<ProductOrderVo>>> listRespCall =
                getApisNew().getUserOrders(AppPreferences.getString("userId"), orderState).clone();
        listRespCall.enqueue(new Callback<ProductOrderListResp<List<ProductOrderVo>>>() {
            @Override
            public void onResponse(Call<ProductOrderListResp<List<ProductOrderVo>>> call,
                                   Response<ProductOrderListResp<List<ProductOrderVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    ProductOrderListResp<List<ProductOrderVo>> listResp = response.body();
                    List<ProductOrderVo> productOrderList = listResp.getProductOrderList();
                    ArrayList<ProductOrderVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(productOrderList);
                }
            }

            @Override
            public void onFailure(Call<ProductOrderListResp<List<ProductOrderVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }
}
