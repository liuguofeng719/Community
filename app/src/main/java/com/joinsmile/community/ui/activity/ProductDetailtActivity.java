package com.joinsmile.community.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ProductResp;
import com.joinsmile.community.bean.ProductVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.widgets.ListViewForScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/28.
 * 商品详情
 */
public class ProductDetailtActivity extends BaseActivity {

    @InjectView(R.id.iv_product_img)
    ImageView ivProductImg;
    @InjectView(R.id.tv_product_title)
    TextView tvProductTitle;
    @InjectView(R.id.tv_product_price)
    TextView tvProductPrice;
    @InjectView(R.id.tv_inventory_total)
    TextView tvInventoryTotal;
    @InjectView(R.id.edit_product_number)
    EditText editProductNumber;
    @InjectView(R.id.radio_group)
    RadioGroup radioGroup;
    @InjectView(R.id.tv_pic_detail)
    RadioButton tvPicDetail;
    @InjectView(R.id.tv_appraise)
    RadioButton tvAppraise;
    @InjectView(R.id.lv_list_view)
    ListViewForScrollView lvListView;

    private Bundle extras;
    private ListViewDataAdapter<String> listViewDataAdapter;

    //加入收藏
    @OnClick(R.id.tv_collect)
    public void tvCollect() {

    }

    //减
    @OnClick(R.id.iv_reduce)
    public void ivReduce() {
        if (TextUtils.isEmpty(editProductNumber.getText()) || editProductNumber.getText().toString().equals("0")) {
            return;
        }
        int count = Integer.parseInt(editProductNumber.getText().toString());
        int tmp = count - 1;
        editProductNumber.setText("" + tmp);
    }

    //加
    @OnClick(R.id.iv_plus)
    public void ivPlus() {
        if (TextUtils.isEmpty(editProductNumber.getText())) {
            editProductNumber.setText("" + 1);
        } else {
            int count = Integer.parseInt(editProductNumber.getText().toString());
            int tmp = count + 1;
            editProductNumber.setText("" + tmp);
        }
    }

    //打电话
    @OnClick(R.id.iv_call_phone)
    public void ivCallPhone() {

    }

    //去购物车页面
    @OnClick(R.id.iv_shop_car)
    public void ivShopCar() {

    }

    //加入购物车
    @OnClick(R.id.tv_join_car)
    public void tvJoinCar() {

    }

    //去订单确认页
    @OnClick(R.id.tv_buy)
    public void tvBuy() {

    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.product_detail_activity;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<String>() {

            @Override
            public ViewHolderBase<String> createViewHolder(int position) {

                final DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                builder.bitmapConfig(Bitmap.Config.RGB_565);
                builder.cacheInMemory(true);
                builder.cacheOnDisk(true);
                builder.considerExifParams(true);

                return new ViewHolderBase<String>() {
                    ImageView imageView;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.product_detail_pic_item, null);
                        imageView = ButterKnife.findById(view, R.id.iv_product_detail_pic);
                        return view;
                    }

                    @Override
                    public void showData(int position, String itemData) {
                        ImageLoader.getInstance().displayImage(itemData, imageView, builder.build());
                    }
                };
            }
        });
        lvListView.setAdapter(listViewDataAdapter);
        getProductById();
    }

    //通过商品编码获取商品
    private void getProductById() {
        Call<ProductResp<ProductVo>> productRespCall = getApisNew().getProduct(extras.getString("productId")).clone();
        productRespCall.enqueue(new Callback<ProductResp<ProductVo>>() {

            @Override
            public void onResponse(Call<ProductResp<ProductVo>> call,
                                   Response<ProductResp<ProductVo>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    ProductVo productVo = response.body().getProduct();
                    final DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    builder.bitmapConfig(Bitmap.Config.RGB_565);
                    builder.cacheInMemory(true);
                    builder.cacheOnDisk(true);
                    builder.considerExifParams(true);
                    ImageLoader.getInstance().displayImage(productVo.getDefaultPicture(), ivProductImg, builder.build());
                    tvProductTitle.setText(productVo.getProductName());
                    tvProductPrice.setText("￥" + productVo.getUnitPrice());
                    tvInventoryTotal.setText("" + productVo.getSalesVolume());
                    ArrayList<String> pictures = productVo.getPictures();
                    ArrayList<String> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(pictures);
                }
            }

            @Override
            public void onFailure(Call<ProductResp<ProductVo>> call, Throwable t) {

            }
        });
    }
}
