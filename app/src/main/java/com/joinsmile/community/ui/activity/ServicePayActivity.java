package com.joinsmile.community.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.joinsmile.community.R;
import com.joinsmile.community.bean.AlipayVo;
import com.joinsmile.community.bean.PayTypeVo;
import com.joinsmile.community.bean.WXPayVo;
import com.joinsmile.community.common.Constants;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.utils.alipay.PayResult;
import com.joinsmile.community.utils.wxpay.MD5;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicePayActivity extends BaseActivity {

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_CHECK_FLAG = 2;
    public static final String ALIPAY = "alipay";
    public static final String WENXIN = "wenxin";
    public static final String BALANCE = "balance";

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.lv_pay_type)
    ListView lv_pay_type;
    @InjectView(R.id.tv_total_price)
    TextView tv_total_price;
    @InjectView(R.id.tv_pay_submit)
    TextView tv_pay_submit;

    private IWXAPI api;
    private Dialog mDialog;
    private PayTypeVo payTypeVo;
    private String partner;
    private Bundle extras;
    private ListViewDataAdapter listViewDataPay;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.pay_ment_mode;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    Bundle bundle = new Bundle();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        bundle.putString("status", "9000");
                        bundle.putString("msg", "支付成功");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            bundle.putString("status", "8000");
                            bundle.putString("msg", "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            bundle.putString("status", "0");
                            bundle.putString("msg", "支付失败");
                        }
                    }
                    readyGo(PaySuccessActivity.class, bundle);
                    break;
                case SDK_CHECK_FLAG:
                    Toast.makeText(ServicePayActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void initViewsAndEvents() {

        tv_header_title.setText(getString(R.string.pay_mode_title));
        tv_total_price.setText("￥" + extras.getString("money") + "元");

        final List<PayTypeVo> payTypeVoList = new ArrayList<>();
        payTypeVoList.add(new PayTypeVo(R.drawable.paybao, "支付宝", ALIPAY, Boolean.FALSE));
        payTypeVoList.add(new PayTypeVo(R.drawable.weixin, "微信钱包", WENXIN, Boolean.TRUE));
        payTypeVo = payTypeVoList.get(1);
        listViewDataPay = new ListViewDataAdapter<PayTypeVo>(new ViewHolderCreator<PayTypeVo>() {
            @Override
            public ViewHolderBase<PayTypeVo> createViewHolder(int position) {
                return new ViewHolderBase<PayTypeVo>() {
                    RelativeLayout rl_pay_type;
                    ImageView iv_pay_icon;
                    TextView tv_pay_text;
                    CheckBox cbo_box;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.pay_list_item, null);
                        rl_pay_type = ButterKnife.findById(view, R.id.rl_pay_type);
                        iv_pay_icon = ButterKnife.findById(view, R.id.iv_pay_icon);
                        tv_pay_text = ButterKnife.findById(view, R.id.tv_pay_text);
                        cbo_box = ButterKnife.findById(view, R.id.cbo_box);
                        return view;
                    }

                    @Override
                    public void showData(final int position, PayTypeVo itemData) {
                        iv_pay_icon.setImageResource(itemData.getResId());
                        tv_pay_text.setText(itemData.getPayName());
                        cbo_box.setChecked(itemData.isSelected());
                        rl_pay_type.setTag(itemData);
                        rl_pay_type.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<PayTypeVo> dataList = listViewDataPay.getDataList();
                                for (PayTypeVo ptv : dataList) {
                                    ptv.setSelected(false);
                                }
                                PayTypeVo itemData = (PayTypeVo) listViewDataPay.getDataList().get(position);
                                itemData.setSelected(Boolean.TRUE);
                                listViewDataPay.notifyDataSetChanged();
                                payTypeVo = ((PayTypeVo) v.getTag());
                            }
                        });
                    }
                };
            }
        });
        listViewDataPay.getDataList().addAll(payTypeVoList);
        lv_pay_type.setAdapter(listViewDataPay);
        this.signAlipay();

    }

    /**
     * 微信支付
     */
    private void wenXinSubmit() {
        mDialog = CommonUtils.showDialog(ServicePayActivity.this, "正在加载微信支付");
        mDialog.show();
        api = WXAPIFactory.createWXAPI(ServicePayActivity.this, Constants.wxpay.APPID);
        api.registerApp(Constants.wxpay.APPID);

        Call<WXPayVo> callWX = getApisNew().payServiceOrderByWeixin(extras.getString("orderId")).clone();
        callWX.enqueue(new Callback<WXPayVo>() {
            @Override
            public void onResponse(Call<WXPayVo> call, Response<WXPayVo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final WXPayVo wxPayVo = response.body();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TLog.d(TAG_LOG, wxPayVo.toString());
                                PayReq req = new PayReq();
                                req.appId = wxPayVo.getAppId();
                                req.partnerId = wxPayVo.getPartnerId();
                                req.prepayId = wxPayVo.getPrepayId();
                                req.nonceStr = wxPayVo.getNonceStr();
                                req.timeStamp = wxPayVo.getTimeStamp();
                                req.packageValue = "Sign=WXPay";

                                List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                                signParams.add(new BasicNameValuePair("appid", req.appId));
                                signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                                signParams.add(new BasicNameValuePair("package", req.packageValue));
                                signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                                signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                                signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                                req.sign = genAppSign(signParams);
                                Message message = handler.obtainMessage();
                                if (api.sendReq(req)) {
                                    message.what = 1;
                                    handler.sendMessage(message);
                                } else {
                                    message.what = 0;
                                    handler.sendMessage(message);
                                }
                                TLog.d(TAG_LOG, api.sendReq(req) + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    if (response.body() != null) {
                        WXPayVo body = response.body();
                        CommonUtils.make(ServicePayActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(ServicePayActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<WXPayVo> call, Throwable t) {

            }
        });
    }

    /**
     * 支付宝支付
     */
    private void alipaySubmit() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ServicePayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(partner);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @OnClick(R.id.tv_pay_submit)
    public void paySubmit() {
        String type = payTypeVo.getType();
        if (WENXIN.equals(type)) {
            wenXinSubmit();
        } else if (ALIPAY.equals(type)) {
            alipaySubmit();
        }
    }

    /**
     * 获取支付宝签名
     */
    private void signAlipay() {
        Call<AlipayVo> siginCall = getApisNew().payServiceOrderByAlipay(extras.getString("orderId")).clone();
        siginCall.enqueue(new Callback<AlipayVo>() {
            @Override
            public void onResponse(Call<AlipayVo> call, Response<AlipayVo> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    partner = response.body().getSignedContent();
                } else {
                    if (response.body() != null) {
                        AlipayVo body = response.body();
                        CommonUtils.make(ServicePayActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(ServicePayActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<AlipayVo> call, Throwable t) {

            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            CommonUtils.dismiss(mDialog);
            switch (msg.what) {
                case 1:
                    break;
                case 0:
                    Bundle bundle = new Bundle();
                    CommonUtils.dismiss(mDialog);
                    bundle.putString("status", "0");
                    bundle.putString("msg", "支付失败");
                    readyGo(PaySuccessActivity.class, bundle);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.wxpay.API_KEY);
        TLog.d(TAG_LOG, sb.toString());
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        TLog.d(TAG_LOG, appSign);
        return appSign;
    }
}
