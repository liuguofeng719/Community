package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ProductOrderVo;
import com.joinsmile.community.bean.ProductOrderVoResp;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
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
public class MyOrderDetailActivity extends BaseActivity {


    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.lv_list_view)
    ListViewForScrollView lvListView;
    @InjectView(R.id.tv_consignee)
    TextView tvConsignee;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_freight)
    TextView tvFreight;
    @InjectView(R.id.tv_leave_message)
    TextView tvLeaveMessage;
    @InjectView(R.id.tv_product_amount)
    TextView tvProductAmount;
    @InjectView(R.id.tv_product_price)
    TextView tvProductPrice;
    @InjectView(R.id.tv_express_company)
    TextView tvExpressCompany;
    @InjectView(R.id.tv_express_company_order)
    TextView tvExpressCompanyOrder;
    @InjectView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @InjectView(R.id.tv_confirm_buy)
    TextView tvConfirmBuy;
    private Bundle extras;
    private Dialog dialog;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_order_detail_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("订单详情");
        dialog = CommonUtils.showDialog(this, getString(R.string.common_loading_message));
        getOrderDetail();

    }

    //获取订单详情
    private void getOrderDetail() {
        dialog.show();
        Call<ProductOrderVoResp<ProductOrderVo>> voRespCall = getApisNew().getOrderDetails(extras.getString("orderId")).clone();
        voRespCall.enqueue(new Callback<ProductOrderVoResp<ProductOrderVo>>() {
            @Override
            public void onResponse(Call<ProductOrderVoResp<ProductOrderVo>> call,
                                   Response<ProductOrderVoResp<ProductOrderVo>> response) {
                CommonUtils.dismiss(dialog);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    ProductOrderVo productOrder = response.body().getProductOrder();
                    List<ProductOrderVo.BuyProductVo> buyProductList = productOrder.getBuyProductList();
                    tvConsignee.setText("收货人：" + productOrder.getBuyersName());
                    tvPhone.setText(productOrder.getBuyersPhoneNumber());
                    tvAddress.setText(productOrder.getBuyersAddress());
                    tvLeaveMessage.setText(productOrder.getRemark());
                    tvProductPrice.setText("￥" + productOrder.getTotalPrice() + "(含运费)");
                    tvProductAmount.setText("共" + buyProductList.size() + "件商品，合计");
                    tvTotalPrice.setText("￥" + productOrder.getTotalPrice());
                    tvExpressCompany.setText(productOrder.getLogisticsCompany());
                    tvExpressCompanyOrder.setText(productOrder.getLogisticsNumber());

                    ListViewDataAdapter<ProductOrderVo.BuyProductVo> listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ProductOrderVo.BuyProductVo>() {
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
                    lvListView.setAdapter(listViewDataAdapter);
                    ArrayList<ProductOrderVo.BuyProductVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(buyProductList);
                    if ("0".equals(productOrder.getOrderSate())) {
                        tvConfirmBuy.setTag(productOrder.getOrderID() + "," + productOrder.getTotalPrice());
                        tvConfirmBuy.setOnClickListener(new View.OnClickListener() {
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
                    } else {
                        tvConfirmBuy.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductOrderVoResp<ProductOrderVo>> call, Throwable t) {
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
}
