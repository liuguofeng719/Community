package com.joinsmile.community.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.joinsmile.community.ui.activity.MyVillageActivity;
import com.joinsmile.community.ui.activity.OnlineRepairsActivity;
import com.joinsmile.community.ui.activity.WebViewActivity;
import com.joinsmile.community.ui.base.BaseFragment;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.DensityUtils;
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

    @OnClick(R.id.tv_house_keeper)
    public void tvHouseKeeper() {
        mDialog.show();
        final TextView manageService = (TextView) mDialog.findViewById(R.id.tv_manage_service);
        manageService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.online_repairs));
                bundle.putInt("isRepair", 1);
                bundle.putString("hintPhone", "备案人或报修人电话号码");
                readyGo(OnlineRepairsActivity.class, bundle);
            }
        });

        final TextView complaintSuggest = (TextView) mDialog.findViewById(R.id.tv_complaint_suggest);
        complaintSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.online_advice));
                bundle.putInt("isRepair", 0);
                bundle.putString("hintPhone", "投诉人电话号码");
                readyGo(OnlineRepairsActivity.class, bundle);
            }
        });

        final TextView tenementPay = (TextView) mDialog.findViewById(R.id.tv_tenement_pay);
        tenementPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //调查问卷
    @OnClick(R.id.tv_vote)
    public void tvVote() {
        Bundle bundle = new Bundle();
        if (tvLocationContent.getTag() != null) {
            bundle.putString("buildingID", tvLocationContent.getTag().toString());
        } else {
            bundle.putString("buildingID", "");
        }
        readyGo(InvestigationActivity.class, bundle);
    }

    //检查登录
    public boolean checkLogin() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            return false;
        }
        return true;
    }

    //选择小区
    @OnClick(R.id.tv_location_content)
    public void tvLocationContent() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("home", true);
        readyGoForResult(MyVillageActivity.class, 1, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10) {
            String location = data.getStringExtra("location");
            String buildingID = data.getStringExtra("buildingID");
            tvLocationContent.setText(location);
            tvLocationContent.setTag(buildingID);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onFirstUserVisible() {
        getPics();
        getOnMainPageProducts();
        getUserApartments();
    }

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

    }

    @Override
    protected void onUserInvisible() {

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

        public HomeAdapter(List<RecommendProductVo> data) {
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.home_recommend_product, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                RecommendProductVo recommendProductVo = data.get(position);
                DisplayImageOptions.Builder b = new DisplayImageOptions.Builder();
                b.showImageForEmptyUri(R.drawable.screen_portrait);
                b.showImageOnFail(R.drawable.screen_portrait);
                b.showImageOnLoading(R.drawable.screen_portrait);
                ImageLoader.getInstance().displayImage(recommendProductVo.getPicture(), holder.iv_product_img, b.build());//
                holder.tv_product_desc.setText(recommendProductVo.getProductName());//
                ArrayList<String> headPicture = recommendProductVo.getHeadPicture();
                holder.ly_face.removeAllViews();
                for (String spic : headPicture) {
                    CircleImageView circleImageView = new CircleImageView(getActivity());
                    circleImageView.setBorderColor(Color.parseColor("#b5e0db"));
                    circleImageView.setBorderWidth(4);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dip2px(getActivity(), 48),
                            DensityUtils.dip2px(getActivity(), 48));
                    lp.setMargins(0, 0, 10, 0);
                    circleImageView.setLayoutParams(lp);
                    DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    builder.showImageForEmptyUri(R.drawable.zuozhu);
                    builder.showImageOnFail(R.drawable.zuozhu);
                    builder.showImageOnLoading(R.drawable.zuozhu);
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
        Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> numbersRespCall = getApisNew().getUserApartments(AppPreferences.getString("userId")).clone();
        numbersRespCall.enqueue(new Callback<ApartmentNumbersResp<List<ApartmentNumbersVo>>>() {
            @Override
            public void onResponse(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call,
                                   Response<ApartmentNumbersResp<List<ApartmentNumbersVo>>> response) {
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
                            tvLocationContent.setTag(numberId);
                            break;
                        }
                    }
                    if (numbersVo != null) {
                        getNewAnnouncement(numbersVo.getNumberID());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call, Throwable t) {
            }
        });
    }

    //获取公告
    private void getNewAnnouncement(String buildId) {
        Call<AnnouncementResp> respCall = getApisNew().getNewAnnouncement(buildId).clone();
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
        showLoading(getString(R.string.common_load_message));
        Call<RecommendProductListResp<List<RecommendProductVo>>> listRespCall = getApisNew().getOnMainPageProducts().clone();
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
        if (mDialog != null) {
            CommonUtils.dismiss(mDialog);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSlideShowView != null) {
            mSlideShowView.onDestroy();
        }
        if (mDialog != null) {
            CommonUtils.dismiss(mDialog);
        }
    }
}
