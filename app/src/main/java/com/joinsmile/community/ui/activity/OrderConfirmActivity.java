package com.joinsmile.community.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

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

    //添加收货地址
    @OnClick(R.id.tv_delivery_address)
    public void tvDeliveryAddress() {
        readyGoForResult(DeliveryAddressActivity.class, 123);
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    //提交订单
    @OnClick(R.id.tv_confirm_buy)
    public void tvConfirmBuy() {
        if (checkLogin()) {

        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.order_confirm_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.order_confirm));
    }
}
