package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.joinsmile.community.R;
import com.joinsmile.community.bean.ApartmentPropertyCostResp;
import com.joinsmile.community.bean.OrderInfoResp;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.TLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/28.
 * 物业缴费
 */
public class PropertyMngPaymentActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_property_price)
    TextView tv_property_price;
    @InjectView(R.id.tv_property_manager)
    TextView tv_property_manager;
    @InjectView(R.id.edit_start_year)
    TextView edit_start_year;
    @InjectView(R.id.edit_end_year)
    TextView edit_end_year;
    @InjectView(R.id.tv_submit)
    TextView tv_submit;
    @InjectView(R.id.edit_monthly)
    TextView edit_monthly;
    @InjectView(R.id.tv_total_price)
    TextView tv_total_price;

    @InjectView(R.id.ly_content)
    LinearLayout ly_content;
    private Bundle extras;
    private String apartmentNumberID;
    private Call<OrderInfoResp> infoRespCall;
    private Dialog mdialog;
    private String propertyCostStandard;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.property_mngpayment_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return ly_content;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText(getString(R.string.payment_property_costs));
        tv_property_manager.setText(extras.getString("location"));
        getPropertyMng();
        //选择出发日期
        final TimePickerView pvTimeStart = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimeStart.setTime(new Date());
        pvTimeStart.setCyclic(false);
        pvTimeStart.setCancelable(true);
        //时间选择后回调
        pvTimeStart.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(date);
                edit_start_year.setText(format);
            }
        });
        edit_start_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTimeStart.show();
            }
        });

        final TimePickerView pvTimeEnd = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimeEnd.setTime(new Date());
        pvTimeEnd.setCyclic(false);
        pvTimeEnd.setCancelable(true);
        //时间选择后回调
        pvTimeEnd.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(date);
                edit_end_year.setText(format);
                String startYear = edit_start_year.getText().toString();
                String endYear = edit_end_year.getText().toString();
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();

                try {
                    c1.setTime(sdf.parse(startYear));
                    c2.setTime(sdf.parse(endYear));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int monthSpace = CommonUtils.getMonths(c1.getTime(), c2.getTime());
                edit_monthly.setText(""+monthSpace);
                int totalPrice =  (int) (Double.parseDouble(propertyCostStandard) * monthSpace);
                tv_total_price.setText(""+ totalPrice);
            }
        });
        edit_end_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTimeEnd.show();
            }
        });
    }

    //获取收费标准
    private void getPropertyMng() {
        showLoading(getString(R.string.common_loading_message));
        apartmentNumberID = extras.getString("apartmentNumberID");
        Call<ApartmentPropertyCostResp> propertyCostRespCall = getApisNew().getApartmentPropertyCost(apartmentNumberID).clone();
        propertyCostRespCall.enqueue(new Callback<ApartmentPropertyCostResp>() {
            @Override
            public void onResponse(Call<ApartmentPropertyCostResp> call,
                                   Response<ApartmentPropertyCostResp> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    propertyCostStandard = response.body().getPropertyCostStandard();
                    tv_property_price.setText("收费标准：" + propertyCostStandard + "/月");
                    TLog.d(TAG_LOG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ApartmentPropertyCostResp> call, Throwable t) {
                hideLoading();
            }
        });
    }

    //提交缴费订单
    @OnClick(R.id.tv_submit)
    public void tvSubmit() {
        mdialog = CommonUtils.showDialog(this);
        mdialog.show();
        infoRespCall = getApisNew().createPropertyCostsOrder(
                AppPreferences.getString("userId"),
                apartmentNumberID,
                edit_monthly.getText().toString(),
                edit_start_year.getText().toString(),
                edit_end_year.getText().toString()
        ).clone();
        infoRespCall.enqueue(new Callback<OrderInfoResp>() {
            @Override
            public void onResponse(Call<OrderInfoResp> call, Response<OrderInfoResp> response) {
                CommonUtils.dismiss(mdialog);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    OrderInfoResp orderInfoResp = response.body();
                    OrderInfoResp.OrderInfo orderInfo = orderInfoResp.getOrderInfo();
                    Bundle bundle = new Bundle();
                    String orderId = orderInfo.getOrderId();
                    bundle.putString("orderId", orderId);
                    bundle.putString("money", orderInfo.getOrderTotalPrice());
                    readyGo(PayMentModeActivity.class, bundle);
                }
            }

            @Override
            public void onFailure(Call<OrderInfoResp> call, Throwable t) {
                CommonUtils.dismiss(mdialog);
            }
        });
    }
}
