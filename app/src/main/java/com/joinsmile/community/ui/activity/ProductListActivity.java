package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.OnTopProductListResp;
import com.joinsmile.community.bean.PicturesVo;
import com.joinsmile.community.bean.ProductListResp;
import com.joinsmile.community.bean.ProductVo;
import com.joinsmile.community.pulltorefresh.library.PullToRefreshBase;
import com.joinsmile.community.pulltorefresh.library.PullToRefreshGridView;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.widgets.MyRadioGroup;
import com.joinsmile.community.widgets.SlideShowView;
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
 * Created by liuguofeng719 on 2016/7/28.
 * 商品首页
 */
public class ProductListActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.my_rdo_group)
    MyRadioGroup my_rdo_group;
    @InjectView(R.id.pull_refresh_grid)
    PullToRefreshGridView mPullRefreshGridView;
    @InjectView(R.id.slideShowView)
    SlideShowView mSlideShowView;

    private GridView mGridView;
    private Call<ProductListResp<List<ProductVo>>> allProductByType;
    private ListViewDataAdapter<ProductVo> listViewDataAdapter;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.product_list_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return mPullRefreshGridView;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText(getString(R.string.recommend_product_msg));
        listViewDataAdapter = new ListViewDataAdapter<ProductVo>(new ViewHolderCreator<ProductVo>() {
            @Override
            public ViewHolderBase<ProductVo> createViewHolder(int position) {
                return new ViewHolderBase<ProductVo>() {
                    ImageView iv_product_img;
                    TextView tv_product_desc;
                    TextView tv_sales_price;
                    TextView tv_postage;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.product_list_item_activity, null);
                        iv_product_img = ButterKnife.findById(view, R.id.iv_product_img);
                        tv_product_desc = ButterKnife.findById(view, R.id.tv_product_desc);
                        tv_sales_price = ButterKnife.findById(view, R.id.tv_sales_price);
                        tv_postage = ButterKnife.findById(view, R.id.tv_postage);
                        return view;
                    }

                    @Override
                    public void showData(int position, ProductVo itemData) {
                        ImageLoader.getInstance().displayImage(itemData.getDefaultPicture(), iv_product_img);
                        tv_product_desc.setText(itemData.getProductName());
                        tv_product_desc.setTag(itemData.getProductID());
                        tv_sales_price.setText("￥" + itemData.getUnitPrice());
                    }
                };
            }
        });

        mGridView = mPullRefreshGridView.getRefreshableView();
        //下拉刷新
        mPullRefreshGridView.getLoadingLayoutProxy(true, false).setPullLabel("");
        mPullRefreshGridView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        mPullRefreshGridView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开以刷新");

        //上拉刷新
        mPullRefreshGridView.getLoadingLayoutProxy(false, true).setPullLabel("");
        mPullRefreshGridView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshGridView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以后加载");
        mPullRefreshGridView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("上拉加载");
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//                Toast.makeText(ProductListActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show();
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以后加载");
                getByTypeProduct(3, 0, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                getByTypeProduct(3, 0, 1);
            }
        });

        mGridView.setAdapter(listViewDataAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.tv_product_desc);
                Bundle bundle = new Bundle();
                bundle.putString("productId", textView.getTag().toString());
                readyGo(ProductDetailtActivity.class, bundle);
            }
        });
        getPics();
        getByTypeProduct(3, 0, 1);

        //@param sortType 排序类型 1:销量 2:价格 3:上架时间
        //@param isDesc 是否是降序 0:升序 1:降序
        my_rdo_group.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tv_sales_amount:
                        getByTypeProduct(1, 1, 1);
                        break;
                    case R.id.tv_sales_price:
                        getByTypeProduct(2, 0, 1);
                        break;
                    case R.id.tv_sales_new_put_away:
                        getByTypeProduct(3, 0, 1);
                        break;
                }
            }
        });
    }

    //获取轮播图
    private void getPics() {
        Call<OnTopProductListResp<List<PicturesVo>>> callPics = getApisNew().getOnProductPageProducts().clone();
        callPics.enqueue(new Callback<OnTopProductListResp<List<PicturesVo>>>() {
            @Override
            public void onResponse(Call<OnTopProductListResp<List<PicturesVo>>> call, Response<OnTopProductListResp<List<PicturesVo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    if (mSlideShowView != null) {
                        mSlideShowView.clearImages();
                        List<PicturesVo> pics = response.body().getOnTopProductList();
                        if (pics.size() > 0) {
                            mSlideShowView.setImageUrlList(pics);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OnTopProductListResp<List<PicturesVo>>> call, Throwable t) {

            }
        });
    }


    /**
     * 通过商品类型，获取商品
     */
    private void getByTypeProduct(int sortType, int isDesc, int pageIndex) {
        showLoading(getString(R.string.common_loading_message));
        allProductByType = getApisNew().getAllProductByType(sortType, isDesc, pageIndex).clone();
        allProductByType.enqueue(new Callback<ProductListResp<List<ProductVo>>>() {
            @Override
            public void onResponse(Call<ProductListResp<List<ProductVo>>> call,
                                   Response<ProductListResp<List<ProductVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body().isSuccessfully() && response.body().isSuccessfully()) {
                    ProductListResp<List<ProductVo>> productListResp = response.body();
                    //设置适配器
                    List<ProductVo> productList = productListResp.getProductList();
                    ArrayList<ProductVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(productList);
                    listViewDataAdapter.notifyDataSetChanged();
                    //执行完数据调用刷新完成
                    mPullRefreshGridView.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(Call<ProductListResp<List<ProductVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }
}
