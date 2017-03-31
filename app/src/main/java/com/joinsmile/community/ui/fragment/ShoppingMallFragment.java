package com.joinsmile.community.ui.fragment;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.OnTopProductListResp;
import com.joinsmile.community.bean.PicturesVo;
import com.joinsmile.community.bean.ProductListResp;
import com.joinsmile.community.bean.ProductPageCatalogues;
import com.joinsmile.community.bean.ProductVo;
import com.joinsmile.community.pulltorefresh.library.PullToRefreshBase;
import com.joinsmile.community.pulltorefresh.library.PullToRefreshGridView;
import com.joinsmile.community.pulltorefresh.library.PullToRefreshScrollView;
import com.joinsmile.community.ui.activity.CatalogueActivity;
import com.joinsmile.community.ui.activity.ProductDetailtActivity;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseFragment;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.GridViewForScrollView;
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
import retrofit2.http.Query;

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
    PullToRefreshScrollView pullToRefreshScrollView;

    @InjectView(R.id.pull_refresh_grid_view)
    GridViewForScrollView mPullRefreshGridView;

    @InjectView(R.id.slideShowView)
    SlideShowView mSlideShowView;
    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.grid_category)
    GridView gridViewCategory;

    private String primaryCatalogueID;//一级分类
    private String subCatalogueID;//子分类

    private Call<ProductListResp<List<ProductVo>>> allProductByType;
    private ListViewDataAdapter<ProductVo> listViewDataAdapter;

    boolean isMore = true;
    private int currPage = 1;
    private int sortType = 3;
    private int desc = 0;
    private ListViewDataAdapter<ProductPageCatalogues.ProductPageCatalogue> dataAdapter;

    @Override
    protected void onFirstUserVisible() {
        TLog.i(TAG_LOG, "onFirstUserVisible");
        getPics();
        getByTypeProduct(primaryCatalogueID,subCatalogueID,sortType, desc, 1);
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
                final DisplayImageOptions.Builder builder = getBuilder();

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
        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        //下拉刷新
        pullToRefreshScrollView.getLoadingLayoutProxy(true, false).setPullLabel("");
        pullToRefreshScrollView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        pullToRefreshScrollView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开以刷新");

        //上拉刷新
        pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setPullLabel("");
        pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以后加载");
        pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("上拉加载");
        // Set a listener to be invoked when the list should be refreshed.
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                Toast.makeText(ProductListActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show();
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以后加载");
                isMore = true;
                currPage = 1;
                getByTypeProduct(primaryCatalogueID,subCatalogueID,sortType, desc, currPage);
            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<ScrollView> refreshView) {
                if (!isMore) {
                    refreshView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshView.onRefreshComplete();
                        }
                    }, 100);
                } else {
                    getByTypeProduct(primaryCatalogueID,subCatalogueID,sortType, desc, currPage);
                }
            }
        });

        mPullRefreshGridView.setAdapter(listViewDataAdapter);
        mPullRefreshGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                primaryCatalogueID="";
                subCatalogueID="";
                switch (checkedId) {
                    case R.id.tv_sales_amount:
                        sortType = 1;
                        desc = 1;
                        getByTypeProduct(primaryCatalogueID,subCatalogueID,sortType, desc, 1);
                        break;
                    case R.id.tv_sales_price:
                        sortType = 2;
                        desc = 0;
                        getByTypeProduct(primaryCatalogueID,subCatalogueID,sortType, desc, 1);
                        break;
                    case R.id.tv_sales_new_put_away:
                        sortType = 3;
                        desc = 0;
                        getByTypeProduct(primaryCatalogueID,subCatalogueID,sortType, desc, 1);
                        break;
                }
                currPage = 1;
                listViewDataAdapter.getDataList().clear();
            }
        });
        btn_back.setVisibility(View.GONE);

        //获取商品分类
        getFirstCategory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 30) {
            String subCatalogueId = data.getStringExtra("subCatalogueId");
            primaryCatalogueID="";
            getByTypeProduct(primaryCatalogueID,subCatalogueId,sortType,desc,1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //获取一级分类
    private void getFirstCategory(){

        dataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ProductPageCatalogues.ProductPageCatalogue>() {
            @Override
            public ViewHolderBase<ProductPageCatalogues.ProductPageCatalogue> createViewHolder(int position) {
                return new ViewHolderBase<ProductPageCatalogues.ProductPageCatalogue>() {
                    final DisplayImageOptions.Builder builder = getBuilder();
                    ImageView image;
                    TextView tv_text;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.catalogue_item_activity, null);
                        image = (ImageView) view.findViewById(R.id.image);
                        tv_text = (TextView) view.findViewById(R.id.tv_text);
                        return view;
                    }

                    @Override
                    public void showData(int position, ProductPageCatalogues.ProductPageCatalogue itemData) {
                        if (TextUtils.isEmpty(itemData.getCatalogueID())) {
                            image.setImageResource(R.drawable.all);
                            tv_text.setText("全部分类");
                            tv_text.setTag("all");
                        } else {
                            ImageLoader.getInstance().displayImage(itemData.getCatalogueIcon(), image, builder.build());
                            tv_text.setText(itemData.getCatalogueName());
                            tv_text.setTag(itemData.getCatalogueID());
                        }
                    }
                };
            }
        });

        gridViewCategory.setAdapter(dataAdapter);

        gridViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                primaryCatalogueID = view.findViewById(R.id.tv_text).getTag().toString();
                if ("all".equalsIgnoreCase(primaryCatalogueID)) {
                    readyGoForResult(CatalogueActivity.class,1);
                } else {
                    subCatalogueID="";
                    getByTypeProduct(primaryCatalogueID, subCatalogueID, sortType, desc, 1);
                }
            }
        });

        Call<ProductPageCatalogues<List<ProductPageCatalogues.ProductPageCatalogue>>> productPageCatalogues = getApisNew().getProductPageCatalogues();

        productPageCatalogues.enqueue(new Callback<ProductPageCatalogues<List<ProductPageCatalogues.ProductPageCatalogue>>>() {
            @Override
            public void onResponse(Call<ProductPageCatalogues<List<ProductPageCatalogues.ProductPageCatalogue>>> call,
                                   Response<ProductPageCatalogues<List<ProductPageCatalogues.ProductPageCatalogue>>> response) {

                if (response.isSuccessful()) {

                    ProductPageCatalogues<List<ProductPageCatalogues.ProductPageCatalogue>> listProductPageCatalogues = response.body();

                    if (listProductPageCatalogues.isSuccessfully()) {

                        List<ProductPageCatalogues.ProductPageCatalogue> catalogues = listProductPageCatalogues.getProductPageCatalogues();
                        ProductPageCatalogues.ProductPageCatalogue pageCatalogueLast = new ProductPageCatalogues.ProductPageCatalogue();
                        pageCatalogueLast.setCatalogueID("");
                        pageCatalogueLast.setCatalogueName("全部分类");
                        catalogues.add(pageCatalogueLast);

                        ArrayList<ProductPageCatalogues.ProductPageCatalogue> dataList = dataAdapter.getDataList();
                        dataList.clear();
                        dataList.addAll(catalogues);
                        dataAdapter.notifyDataSetChanged();

                    } else {

                        CommonUtils.make(mContext, listProductPageCatalogues.getErrorMessage());
                    }
                } else {

                    CommonUtils.make(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductPageCatalogues<List<ProductPageCatalogues.ProductPageCatalogue>>> call, Throwable t) {
                CommonUtils.make(mContext, t.getMessage());
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
    private void getByTypeProduct(String primaryCatalogueID,String subCatalogueID,int sortType, int isDesc, int pageIndex) {
        showLoading(getString(R.string.common_loading_message));
        allProductByType = getApisNew().getAllProductByType(primaryCatalogueID,subCatalogueID,sortType, isDesc, pageIndex).clone();
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
                    dataList.clear();
                    if (currPage == totalPage) {
                        isMore = false;
                        pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setReleaseLabel("没有更多数据了");
                    }
                    if (currPage < totalPage) {
                        currPage++;
                    }
                    if (productList.isEmpty()) {
                        isMore = false;
                        listViewDataAdapter.notifyDataSetChanged();
                        CommonUtils.make(mContext, "暂无数据");
                    } else {
                        dataList.addAll(productList);
                        listViewDataAdapter.notifyDataSetChanged();
                    }
                    pullToRefreshScrollView.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(Call<ProductListResp<List<ProductVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }
}
