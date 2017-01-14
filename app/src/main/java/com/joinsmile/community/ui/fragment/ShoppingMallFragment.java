package com.joinsmile.community.ui.fragment;

import android.graphics.Bitmap;
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
import com.joinsmile.community.ui.activity.ProductDetailtActivity;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseFragment;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.MyRadioGroup;
import com.joinsmile.community.widgets.SlideShowView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/18.
 * 商城
 */
public class ShoppingMallFragment extends BaseFragment {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.my_rdo_group)
    MyRadioGroup my_rdo_group;
    @InjectView(R.id.pull_refresh_grid)
    PullToRefreshGridView mPullRefreshGridView;
    @InjectView(R.id.slideShowView)
    SlideShowView mSlideShowView;
    @InjectView(R.id.btn_back)
    ImageView btn_back;

    private GridView mGridView;
    private Call<ProductListResp<List<ProductVo>>> allProductByType;
    private ListViewDataAdapter<ProductVo> listViewDataAdapter;

    boolean isMore = true;
    private int currPage = 1;
    private int sortType = 3;
    private int desc = 0;

    @Override
    protected void onFirstUserVisible() {
        TLog.i(TAG_LOG, "onFirstUserVisible");
        getPics();
        getByTypeProduct(3, 0, 1);
    }

    @Override
    protected void onUserVisible() {
        TLog.i(TAG_LOG, "onUserVisible");
    }

    @Override
    protected void onUserInvisible() {
        TLog.i(TAG_LOG, "onUserInvisible");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.shopping_mall_fragment;
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
                final DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                builder.bitmapConfig(Bitmap.Config.RGB_565);
                builder.cacheInMemory(true);
                builder.cacheOnDisk(true);
                builder.considerExifParams(true);
                builder.showImageForEmptyUri(R.drawable.no_banner);
                builder.showImageOnFail(R.drawable.no_banner);
                builder.showImageOnLoading(R.drawable.no_banner);

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
                        ImageLoader.getInstance().displayImage(itemData.getDefaultPicture(), iv_product_img, builder.build());
                        tv_product_desc.setText(itemData.getProductName());
                        tv_product_desc.setTag(itemData.getProductID());
                        tv_sales_price.setText("￥" + itemData.getUnitPrice());
                        tv_postage.setText("");
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
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以后加载");
                isMore = true;
                currPage = 1;
                getByTypeProduct(sortType, desc, currPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (!isMore) {
                    refreshView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshGridView.onRefreshComplete();
                        }
                    }, 100);
                } else {
                    getByTypeProduct(sortType, desc, currPage);
                }
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

        //@param sortType 排序类型 1:销量 2:价格 3:上架时间
        //@param isDesc 是否是降序 0:升序 1:降序
        my_rdo_group.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tv_sales_amount:
                        sortType = 1;
                        desc = 1;
                        getByTypeProduct(1, 1, 1);
                        break;
                    case R.id.tv_sales_price:
                        sortType = 2;
                        desc = 0;
                        getByTypeProduct(2, 0, 1);
                        break;
                    case R.id.tv_sales_new_put_away:
                        sortType = 3;
                        desc = 0;
                        getByTypeProduct(3, 0, 1);
                        break;
                }
                currPage = 1;
                listViewDataAdapter.getDataList().clear();
            }
        });
        btn_back.setVisibility(View.GONE);
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
                    List<ProductVo> dataList = listViewDataAdapter.getDataList();
                    int totalPage = productListResp.getPageCount();
                    if (currPage == 1) {
                        dataList.clear();
                    }
                    if (currPage == totalPage) {
                        isMore = false;
                        mPullRefreshGridView.getLoadingLayoutProxy(false, true).setReleaseLabel("没有更多数据了");
                    }
                    if (currPage < totalPage) {
                        currPage++;
                    }
                    if (productList.isEmpty()) {
                        isMore = false;
                    } else {
                        dataList.addAll(productList);
                        listViewDataAdapter.notifyDataSetChanged();
                    }
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
