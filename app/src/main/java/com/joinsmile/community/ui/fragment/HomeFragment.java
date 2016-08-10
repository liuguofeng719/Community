package com.joinsmile.community.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.AnnouncementResp;
import com.joinsmile.community.bean.ApartmentNumbersResp;
import com.joinsmile.community.bean.ApartmentNumbersVo;
import com.joinsmile.community.bean.PicturesVo;
import com.joinsmile.community.bean.PicturesVoResp;
import com.joinsmile.community.bean.RecommendProductListResp;
import com.joinsmile.community.bean.RecommendProductVo;
import com.joinsmile.community.ui.activity.InvestigationActivity;
import com.joinsmile.community.ui.activity.LoginActivity;
import com.joinsmile.community.ui.activity.MyVillageActivity;
import com.joinsmile.community.ui.activity.OnlineRepairsActivity;
import com.joinsmile.community.ui.activity.ProductDetailtActivity;
import com.joinsmile.community.ui.activity.ProductListActivity;
import com.joinsmile.community.ui.activity.PropertyMngPaymentActivity;
import com.joinsmile.community.ui.activity.WebViewActivity;
import com.joinsmile.community.ui.base.BaseFragment;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.DensityUtils;
import com.joinsmile.community.utils.TLog;
import com.joinsmile.community.widgets.CircleImageView;
import com.joinsmile.community.widgets.SlideShowView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements SlideShowView.OnImageClickedListener {

    @InjectView(R.id.slideShowView)
    SlideShowView mSlideShowView;
    @InjectView(R.id.tv_location_content)
    TextView tvLocationContent;
    @InjectView(R.id.fl_location)
    FrameLayout flLocation;
    @InjectView(R.id.tv_tips_content)
    TextView tvTipsContent;
    @InjectView(R.id.tv_house_keeper)
    TextView tvHouseKeeper;
    @InjectView(R.id.tv_intelligence)
    TextView tvIntelligence;
    @InjectView(R.id.tv_integral)
    TextView tvIntegral;
    @InjectView(R.id.tv_vote)
    TextView tvVote;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Dialog mDialog;
    private Dialog showDialog;
    private Call<RecommendProductListResp<List<RecommendProductVo>>> listRespCall;
    private Call<AnnouncementResp> respCall;


    @OnClick(R.id.tv_house_keeper)
    public void tvHouseKeeper() {
        mDialog.show();
        final TextView manageService = (TextView) mDialog.findViewById(R.id.tv_manage_service);
        manageService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getString(R.string.online_repairs));
                    bundle.putInt("isRepair", 1);
                    bundle.putString("hintPhone", "备案人或报修人电话号码");
                    readyGo(OnlineRepairsActivity.class, bundle);
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });

        final TextView complaintSuggest = (TextView) mDialog.findViewById(R.id.tv_complaint_suggest);
        complaintSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getString(R.string.online_advice));
                    bundle.putInt("isRepair", 0);
                    bundle.putString("hintPhone", "投诉人电话号码");
                    readyGo(OnlineRepairsActivity.class, bundle);
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });

        final TextView tenementPay = (TextView) mDialog.findViewById(R.id.tv_tenement_pay);
        tenementPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("apartmentNumberID", tvLocationContent.getTag().toString().split(",")[0]);
                    bundle.putString("location", tvLocationContent.getTag().toString().split(",")[2]);
                    readyGo(PropertyMngPaymentActivity.class, bundle);
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });
    }

    //调查问卷
    @OnClick(R.id.tv_vote)
    public void tvVote() {
        Bundle bundle = new Bundle();
        if (tvLocationContent.getTag() != null) {
            bundle.putString("buildingID", tvLocationContent.getTag().toString().split(",")[1]);
        } else {
            bundle.putString("buildingID", "");
        }
        readyGo(InvestigationActivity.class, bundle);
    }

    //选择小区
    @OnClick(R.id.tv_location_content)
    public void tvLocationContent() {
        if (!checkLogin()) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("home", true);
            readyGoForResult(MyVillageActivity.class, 1, bundle);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10) {
            String location = data.getStringExtra("location");
            String buildingID = data.getStringExtra("buildingID");
            String locationDone = data.getStringExtra("locationDone");
            tvLocationContent.setText(location);
            tvLocationContent.setTag(buildingID + "," + locationDone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onFirstUserVisible() {
        TLog.i(TAG_LOG, "onFirstUserVisible");
        getUserApartments();
        getPics();
        getOnMainPageProducts();
    }

    //获取轮播图
    private void getPics() {
        Call<PicturesVoResp<List<PicturesVo>>> callPics = getApisNew().getAdvertisementPictures().clone();
        callPics.enqueue(new Callback<PicturesVoResp<List<PicturesVo>>>() {
            @Override
            public void onResponse(Call<PicturesVoResp<List<PicturesVo>>> call, Response<PicturesVoResp<List<PicturesVo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    if (mSlideShowView != null) {
                        mSlideShowView.clearImages();
                        List<PicturesVo> pics = response.body().getPictures();
                        if (pics.size() > 0) {
                            mSlideShowView.setImageUrlList(pics);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PicturesVoResp<List<PicturesVo>>> call, Throwable t) {

            }
        });
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
    protected View getLoadingTargetView() {
        return recyclerView;
    }

    @Override
    protected void initViewsAndEvents() {
        ShareSDK.initSDK(getActivity());
        mDialog = CommonUtils.createDialog(getActivity());
        mDialog.setContentView(R.layout.community_mng_dialog);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        this.mSlideShowView.setOnImageClickedListener(this);
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private List<RecommendProductVo> data;
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();

        public HomeAdapter(List<RecommendProductVo> data) {
            this.data = data;
            builder.bitmapConfig(Bitmap.Config.RGB_565);
            builder.cacheInMemory(true);
            builder.cacheOnDisk(true);
            builder.considerExifParams(true);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_recommend_product, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                RecommendProductVo recommendProductVo = data.get(position);
                ImageLoader.getInstance().displayImage(recommendProductVo.getPicture(), holder.iv_product_img, builder.build());
                holder.iv_product_img.setTag(recommendProductVo.getProductId());
                holder.iv_product_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", v.getTag().toString());
                        readyGo(ProductDetailtActivity.class, bundle);
                    }
                });
                holder.tv_product_desc.setTag(recommendProductVo.getProductId());
                holder.tv_product_desc.setText(recommendProductVo.getProductName());
                holder.tv_product_desc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", v.getTag().toString());
                        readyGo(ProductDetailtActivity.class, bundle);
                    }
                });
                ArrayList<String> headPicture = recommendProductVo.getHeadPicture();
                holder.ly_face.setTag(recommendProductVo.getProductId());
                holder.ly_face.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", v.getTag().toString());
                        readyGo(ProductDetailtActivity.class, bundle);
                    }
                });
                holder.ly_face.removeAllViews();
                for (String spic : headPicture) {
                    CircleImageView circleImageView = new CircleImageView(getActivity());
                    circleImageView.setBorderColor(Color.parseColor("#b5e0db"));
                    circleImageView.setBorderWidth(4);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dip2px(getActivity(), 48),
                            DensityUtils.dip2px(getActivity(), 48));
                    lp.setMargins(0, 0, 10, 0);
                    circleImageView.setLayoutParams(lp);
                    ImageLoader.getInstance().displayImage(spic, circleImageView, builder.build());
                    holder.ly_face.addView(circleImageView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_product_img;
            TextView tv_product_desc;
            LinearLayout ly_face;

            public MyViewHolder(View view) {
                super(view);
                iv_product_img = ButterKnife.findById(view, R.id.iv_product_img);
                tv_product_desc = ButterKnife.findById(view, R.id.tv_product_desc);
                ly_face = ButterKnife.findById(view, R.id.ly_face);
            }
        }
    }

    /**
     * 我的小区
     */
    private void getUserApartments() {
        if (!checkLogin()) {
            showDialog = CommonUtils.showDialog(getActivity(), getString(R.string.common_loading_message));
            showDialog.show();
            Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> numbersRespCall = getApisNew().getUserApartments(AppPreferences.getString("userId")).clone();
            numbersRespCall.enqueue(new Callback<ApartmentNumbersResp<List<ApartmentNumbersVo>>>() {
                @Override
                public void onResponse(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call,
                                       Response<ApartmentNumbersResp<List<ApartmentNumbersVo>>> response) {
                    CommonUtils.dismiss(showDialog);
                    if (response.isSuccessful() && response.body() != null) {
                        ApartmentNumbersResp<List<ApartmentNumbersVo>> body = response.body();
                        List<ApartmentNumbersVo> apartmentNumberList = body.getApartmentNumberList();
                        ApartmentNumbersVo numbersVo = null;
                        for (int i = 0; i < apartmentNumberList.size(); i++) {
                            if (apartmentNumberList.get(i).isDefault() == 1) {
                                numbersVo = apartmentNumberList.get(i);
                                String building = numbersVo.getBuilding();
                                String numberId = numbersVo.getNumberID();
                                tvLocationContent.setText(building);
                                tvLocationContent.setTag(numberId + "," + numbersVo.getBuildingID() + "," + building + numbersVo.getUnit() + numbersVo.getApartment());
                                break;
                            }
                        }
                        if (numbersVo != null) {
                            getNewAnnouncement(numbersVo.getBuildingID());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call, Throwable t) {
                    CommonUtils.dismiss(showDialog);
                }
            });
        } else {
            getNewAnnouncement("");
        }
    }

    //获取公告
    private void getNewAnnouncement(String buildId) {
        respCall = getApisNew().getNewAnnouncement(buildId).clone();
        respCall.enqueue(new Callback<AnnouncementResp>() {
            @Override
            public void onResponse(Call<AnnouncementResp> call, Response<AnnouncementResp> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    final AnnouncementResp resp = response.body();
                    final String title = resp.getAnnouncement().getTitle();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTipsContent.setText(title);
                            tvTipsContent.setTag(resp);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AnnouncementResp> call, Throwable t) {

            }
        });
    }

    //获取推荐商品
    private void getOnMainPageProducts() {
        showLoading(getString(R.string.common_loading_message));
        listRespCall = getApisNew().getOnMainPageProducts().clone();
        listRespCall.enqueue(new Callback<RecommendProductListResp<List<RecommendProductVo>>>() {
            @Override
            public void onResponse(Call<RecommendProductListResp<List<RecommendProductVo>>> call,
                                   Response<RecommendProductListResp<List<RecommendProductVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    List<RecommendProductVo> productList = response.body().getProductList();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    layoutManager.setSmoothScrollbarEnabled(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new HomeAdapter(productList));
                }
            }

            @Override
            public void onFailure(Call<RecommendProductListResp<List<RecommendProductVo>>> call, Throwable t) {
                hideLoading();
            }
        });
    }

    //积分，现在测试推荐商品
    @OnClick(R.id.tv_query_all)
    public void tv_query_all() {
        readyGo(ProductListActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.index_main;
    }

    @Override
    public void onImageClicked(int position, SlideShowView.ImageViewTag url) {
        Bundle bundle = new Bundle();
        bundle.putString("navUrl", url.getNavUrl());
        readyGo(WebViewActivity.class, bundle);
    }

    @Override
    public void onStop() {
        super.onStop();
        closeDialog();
    }

    private void closeDialog() {
        if (mDialog != null) {
            CommonUtils.dismiss(mDialog);
        }
        if (showDialog != null) {
            CommonUtils.dismiss(showDialog);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSlideShowView != null) {
            mSlideShowView.onDestroy();
        }
        if (listRespCall != null) {
            listRespCall.cancel();
        }
        if (respCall != null) {
            respCall.cancel();
        }
        closeDialog();
    }
}
