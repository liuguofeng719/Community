package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joinsmile.community.R;
import com.joinsmile.community.bean.OrderInfoResp;
import com.joinsmile.community.bean.ShoppingCartVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.TLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
 * 订单确认页
 */
public class OrderConfirmActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_delivery_address)
    TextView tvDeliveryAddress;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    @InjectView(R.id.edit_leave_message)
    EditText editLeaveMessage;
    @InjectView(R.id.tv_product_amount)
    TextView tvProductAmount;
    @InjectView(R.id.tv_product_price)
    TextView tvProductPrice;
    @InjectView(R.id.tv_total_price)
    TextView tvTotalPrice;

    private Bundle extras;
    private ListViewDataAdapter<ShoppingCartVo> listViewDataAdapter;
    private StringBuilder products;
    private Dialog dialog;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    //添加收货地址
    @OnClick(R.id.tv_delivery_address)
    public void tvDeliveryAddress() {
        readyGoForResult(DeliveryAddressActivity.class, 12);
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 15) {
            tvDeliveryAddress.setTag(data.getStringExtra("addressId"));
            tvDeliveryAddress.setText(data.getStringExtra("addressName"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //提交订单
    @OnClick(R.id.tv_confirm_buy)
    public void tvConfirmBuy() {
        dialog = CommonUtils.showDialog(this, "正在提交订单...");
        if (tvDeliveryAddress.getTag() == null) {
            CommonUtils.make(this, "收货地址不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tvDeliveryAddress.getTag().toString())) {
            CommonUtils.make(this, "收货地址不能为空！");
            return;
        }
        if (!TextUtils.isEmpty(products)) {
            dialog.show();
            try {
                String encode = URLEncoder.encode(products.substring(0, products.lastIndexOf("|")), "UTF-8");
                Call<OrderInfoResp> infoRespCall = getApisNew().createOrder(AppPreferences.getString("userId"),
                        encode,
                        editLeaveMessage.getText().toString(),
                        tvDeliveryAddress.getTag().toString()
                ).clone();
                infoRespCall.enqueue(new Callback<OrderInfoResp>() {
                    @Override
                    public void onResponse(Call<OrderInfoResp> call, Response<OrderInfoResp> response) {
                        CommonUtils.dismiss(dialog);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                            OrderInfoResp orderInfoResp = response.body();
                            Bundle bundle = new Bundle();
                            OrderInfoResp.OrderInfo orderInfo = orderInfoResp.getOrderInfo();
                            bundle.putString("orderId", orderInfo.getOrderId());
                            bundle.putString("money", orderInfo.getOrderTotalPrice());
                            readyGo(OrderPayMentModeActivity.class, bundle);
                        } else {
                            CommonUtils.make(OrderConfirmActivity.this, response.body().getErrorMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderInfoResp> call, Throwable t) {
                        CommonUtils.dismiss(dialog);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.order_confirm_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.order_confirm));
        String carts = extras.getString("carts");
        List<ShoppingCartVo> shoppingCartVos = new Gson().fromJson(carts, new TypeToken<List<ShoppingCartVo>>() {
        }.getType());
        TLog.d(TAG_LOG, shoppingCartVos.toString());
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ShoppingCartVo>() {
            @Override
            public ViewHolderBase<ShoppingCartVo> createViewHolder(int position) {
                return new ViewHolderBase<ShoppingCartVo>() {
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
                    public void showData(int position, ShoppingCartVo itemData) {
                        tv_product_title.setText(itemData.getProductName());
                        tv_product_price.setText("￥" + itemData.getProductPrice());
                        tv_product_amount.setText("X" + itemData.getAmount());
                        ImageLoader.getInstance().displayImage(itemData.getProductPicture(), iv_product_img);
                    }
                };
            }
        });

        ArrayList<ShoppingCartVo> dataList = listViewDataAdapter.getDataList();
        dataList.clear();
        dataList.addAll(shoppingCartVos);
        tvProductAmount.setText("共" + dataList.size() + "件商品，合计");
        double totalPrice = 0.0;
        products = new StringBuilder();
        for (ShoppingCartVo shoppingCartVo : dataList) {
            totalPrice += Double.parseDouble(shoppingCartVo.getProductPrice().trim()) * Integer.parseInt(shoppingCartVo.getAmount());
            products.append(shoppingCartVo.getProductID()).append(",").append(shoppingCartVo.getAmount()).append("|");
        }
        TLog.d("products====", products.substring(0, products.lastIndexOf("|")));
        long l = Math.round(totalPrice * 100); // 四舍五入
        double ret = l / 100.0; // 注意：使用 100.0 而不是 100
        tvProductPrice.setText("￥" + ret);
        tvTotalPrice.setText("￥" + ret);
        lvListView.setAdapter(listViewDataAdapter);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            CommonUtils.dismiss(dialog);
        }
        super.onDestroy();
    }
}
