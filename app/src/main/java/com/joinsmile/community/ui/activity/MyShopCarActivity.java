package com.joinsmile.community.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joinsmile.community.R;
import com.joinsmile.community.bean.ShoppingCartResp;
import com.joinsmile.community.bean.ShoppingCartVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.SparseBooleanUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/8/11.
 * 我的购物车
 */
public class MyShopCarActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    @InjectView(R.id.checkbox_all)
    CheckBox checkboxAll;
    @InjectView(R.id.tv_total_price)
    TextView tvTotalPrice;

    private ListViewDataAdapter<ShoppingCartVo> listViewDataAdapter;
    private List<ShoppingCartVo> cartProductList;

    @OnCheckedChanged(R.id.checkbox_all)
    public void checkboxAll() {
        if (checkboxAll.isChecked()) {
            SparseBooleanUtils.checkedAll(true);
        } else {
            SparseBooleanUtils.resetBoolean();
        }
        listViewDataAdapter.notifyDataSetChanged();
        calculate();
    }

    /**
     * 去支付
     */
    @OnClick(R.id.tv_go_payment)
    public void tvGoPayment() {
        SparseBooleanArray sparseBooleanArray = SparseBooleanUtils.getSparseBooleanArray();
        int size = sparseBooleanArray.size();
        List<ShoppingCartVo> selectedCarts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (sparseBooleanArray.get(i)) {
                ShoppingCartVo shoppingCartVo = cartProductList.get(i);
                selectedCarts.add(shoppingCartVo);
            }
        }
        if (selectedCarts.size() == 0) {
            CommonUtils.make(this, "请选择需要的商品!");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("carts", new Gson().toJson(selectedCarts));
        readyGo(OrderConfirmActivity.class, bundle);
    }

    /**
     * 删除订单
     */
    @OnClick(R.id.tv_del)
    public void tvDel() {
        StringBuilder str = new StringBuilder();
        SparseBooleanArray sparseBooleanArray = SparseBooleanUtils.getSparseBooleanArray();
        int size = sparseBooleanArray.size();

        for (int i = 0; i < size; i++) {
            if (sparseBooleanArray.get(i)) {
                str.append(cartProductList.get(i).getShoppingCartID() + ",");
            }
        }
        if (str.length() == 0) {
            CommonUtils.make(this, "请选择商品项!");
            return;
        }
        Call<ShoppingCartResp<List<ShoppingCartVo>>> cartRespCall =
                getApisNew().removeShoppingCartProduct(AppPreferences.getString("userId"),
                        str.substring(0, str.lastIndexOf(","))).clone();
        cartRespCall.enqueue(new Callback<ShoppingCartResp<List<ShoppingCartVo>>>() {
            @Override
            public void onResponse(Call<ShoppingCartResp<List<ShoppingCartVo>>> call,
                                   Response<ShoppingCartResp<List<ShoppingCartVo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    ShoppingCartResp<List<ShoppingCartVo>> shoppingCartResp = response.body();
                    cartProductList = shoppingCartResp.getShoppingCartProductList();
                    if (cartProductList != null && cartProductList.size() > 0) {
                        ArrayList<ShoppingCartVo> dataList = listViewDataAdapter.getDataList();
                        dataList.clear();
                        dataList.addAll(cartProductList);
                        listViewDataAdapter.notifyDataSetChanged();
                        tvTotalPrice.setText("￥" + shoppingCartResp.getTotalPrice());
                    }
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartResp<List<ShoppingCartVo>>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_shop_car_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("购物车");
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ShoppingCartVo>() {
            @Override
            public ViewHolderBase<ShoppingCartVo> createViewHolder(int position) {

                final DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                builder.bitmapConfig(Bitmap.Config.RGB_565);
                builder.cacheInMemory(true);
                builder.cacheOnDisk(true);
                builder.considerExifParams(true);
                builder.showImageForEmptyUri(R.drawable.no_banner);
                builder.showImageOnFail(R.drawable.no_banner);
                builder.showImageOnLoading(R.drawable.no_banner);

                return new ViewHolderBase<ShoppingCartVo>() {

                    CheckBox checkbox_selected;
                    ImageView iv_product_img;
                    ImageView iv_reduce;
                    ImageView iv_plus;
                    TextView tv_product_title;
                    TextView tv_product_price;
                    EditText edit_product_number;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.my_shop_car_item, null);
                        checkbox_selected = ButterKnife.findById(view, R.id.checkbox_selected);
                        iv_product_img = ButterKnife.findById(view, R.id.iv_product_img);
                        iv_reduce = ButterKnife.findById(view, R.id.iv_reduce);
                        iv_plus = ButterKnife.findById(view, R.id.iv_plus);
                        tv_product_title = ButterKnife.findById(view, R.id.tv_product_title);
                        tv_product_price = ButterKnife.findById(view, R.id.tv_product_price);
                        edit_product_number = ButterKnife.findById(view, R.id.edit_product_number);
                        return view;
                    }

                    @Override
                    public void showData(final int position, ShoppingCartVo itemData) {
                        tv_product_title.setText(itemData.getProductName());
                        tv_product_price.setText("￥" + itemData.getProductPrice());
                        edit_product_number.setText("" + itemData.getAmount());
                        ImageLoader.getInstance().displayImage(itemData.getProductPicture(), iv_product_img);
                        edit_product_number.setTag(itemData.getShoppingCartID());
                        edit_product_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            public boolean onEditorAction(TextView v, int actionId,
                                                          KeyEvent event) {
                                final String searchStr = edit_product_number.getText().toString().trim();
//                                EditorInfo.IME_ACTION_DONE只有对android:singleLine="true"的EditText有效
                                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    if (searchStr == null || "".equals(searchStr) || "0".equals(searchStr)) {
                                        edit_product_number.setText("1");
                                        CommonUtils.make(MyShopCarActivity.this, "商品数量不能为空或者为零!");
                                        return true;
                                    } else {
                                        int amount = Integer.parseInt(searchStr.toString());
                                        modifyShopCart(amount, edit_product_number.getTag().toString());
                                    }
                                    return true;
                                }
                                return false;
                            }
                        });

                        iv_reduce.setTag(itemData.getShoppingCartID());
                        iv_reduce.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(edit_product_number.getText()) || edit_product_number.getText().toString().equals("0")) {
                                    return;
                                }
                                int count = Integer.parseInt(edit_product_number.getText().toString());
                                if (count == 1) {
                                    edit_product_number.setText("" + count);
                                    modifyShopCart(1, v.getTag().toString());
                                } else {
                                    int tmp = count - 1;
                                    edit_product_number.setText("" + tmp);
                                    modifyShopCart(tmp, v.getTag().toString());
                                }
                            }
                        });
                        iv_plus.setTag(itemData.getShoppingCartID());
                        iv_plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(edit_product_number.getText())) {
                                    edit_product_number.setText("" + 1);
                                    modifyShopCart(1, v.getTag().toString());
                                } else {
                                    int count = Integer.parseInt(edit_product_number.getText().toString());
                                    int tmp = count + 1;
                                    edit_product_number.setText("" + tmp);
                                    modifyShopCart(tmp, v.getTag().toString());
                                }
                            }
                        });
                        checkbox_selected.setOnCheckedChangeListener(null);
                        checkbox_selected.setChecked(SparseBooleanUtils.getSparseBooleanArray().get(position));
                        checkbox_selected.setTag(position);
                        checkbox_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                int parseInt = Integer.parseInt(buttonView.getTag().toString());
                                if (isChecked) {
                                    SparseBooleanUtils.putBoolean(parseInt, true);
                                } else {
                                    SparseBooleanUtils.putBoolean(parseInt, false);
                                }
                                calculate();
                            }
                        });
                    }
                };
            }
        });
        lvListView.setAdapter(listViewDataAdapter);
        getShopCart();
    }

    //计算总价
    public void calculate() {
        SparseBooleanArray booleanArray = SparseBooleanUtils.getSparseBooleanArray();
        double totalPrice = 0.0;
        for (int i = 0; i < booleanArray.size(); i++) {
            if (booleanArray.get(i)) {
                ShoppingCartVo cartVo = cartProductList.get(i);
                int amount = Integer.parseInt(cartVo.getAmount().trim());
                double itemPrice = Double.parseDouble(cartVo.getProductPrice().toString().trim());
                totalPrice += amount * itemPrice;
            }
        }
        long l = Math.round(totalPrice * 100); // 四舍五入
        double ret = l / 100.0; // 注意：使用 100.0 而不是 100
        tvTotalPrice.setText("￥" + ret);
    }

    //获取我的购物车
    private void getShopCart() {
        showLoading(getString(R.string.common_loading_message));
        Call<ShoppingCartResp<List<ShoppingCartVo>>> cartRespCall =
                getApisNew().getShoppingCartProducts(AppPreferences.getString("userId")).clone();
        cartRespCall.enqueue(new Callback<ShoppingCartResp<List<ShoppingCartVo>>>() {
            @Override
            public void onResponse(Call<ShoppingCartResp<List<ShoppingCartVo>>> call,
                                   Response<ShoppingCartResp<List<ShoppingCartVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    ShoppingCartResp<List<ShoppingCartVo>> shoppingCartResp = response.body();
                    cartProductList = shoppingCartResp.getShoppingCartProductList();
                    if (cartProductList != null && cartProductList.size() > 0) {
                        ArrayList<ShoppingCartVo> dataList = listViewDataAdapter.getDataList();
                        dataList.clear();
                        dataList.addAll(cartProductList);
                        SparseBooleanUtils.setData(dataList.size(), true);
                        tvTotalPrice.setText("￥" + shoppingCartResp.getTotalPrice());
                    }
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartResp<List<ShoppingCartVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }

    //修改购物车
    private void modifyShopCart(int amount, String shopCartID) {
        Call<ShoppingCartResp<List<ShoppingCartVo>>> cartRespCall =
                getApisNew().editShoppingCartProduct(AppPreferences.getString("userId"), amount, shopCartID).clone();
        cartRespCall.enqueue(new Callback<ShoppingCartResp<List<ShoppingCartVo>>>() {
            @Override
            public void onResponse(Call<ShoppingCartResp<List<ShoppingCartVo>>> call,
                                   Response<ShoppingCartResp<List<ShoppingCartVo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    ShoppingCartResp<List<ShoppingCartVo>> shoppingCartResp = response.body();
                    cartProductList = shoppingCartResp.getShoppingCartProductList();
                    if (cartProductList != null && cartProductList.size() > 0) {
                        ArrayList<ShoppingCartVo> dataList = listViewDataAdapter.getDataList();
                        dataList.clear();
                        dataList.addAll(cartProductList);
                        listViewDataAdapter.notifyDataSetChanged();
                        tvTotalPrice.setText("￥" + shoppingCartResp.getTotalPrice());
                    }
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartResp<List<ShoppingCartVo>>> call, Throwable t) {

            }
        });
    }
}
