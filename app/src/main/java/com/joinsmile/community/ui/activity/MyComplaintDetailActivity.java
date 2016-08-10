package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.PicturesVo;
import com.joinsmile.community.bean.RepairAndComplaintDetailVo;
import com.joinsmile.community.bean.RepairAndComplaintResp;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.widgets.SlideShowView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/8/8.
 * 我的投诉，我的建议
 */
public class MyComplaintDetailActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.slideShowView)
    SlideShowView mSlideShowView;
    @InjectView(R.id.tv_brief_desc)
    TextView tvBriefDesc;
    @InjectView(R.id.tv_link_phone)
    TextView tvLinkPhone;
    @InjectView(R.id.tv_up_date)
    TextView tvUpDate;
    @InjectView(R.id.tv_address_detail)
    TextView tvAddressDetail;

    private Bundle extras;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_complaint_detail_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(extras.getString("title"));
        Call<RepairAndComplaintResp<RepairAndComplaintDetailVo>> complaintRespCall =
                getApisNew().getRepairAndComplaintsDetails(extras.getString("complaintID")).clone();
        complaintRespCall.enqueue(new Callback<RepairAndComplaintResp<RepairAndComplaintDetailVo>>() {
            @Override
            public void onResponse(Call<RepairAndComplaintResp<RepairAndComplaintDetailVo>> call,
                                   Response<RepairAndComplaintResp<RepairAndComplaintDetailVo>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    RepairAndComplaintDetailVo complaintDetailVo = response.body().getRepairAndComplaint();
                    if (mSlideShowView != null) {
                        mSlideShowView.clearImages();
                        ArrayList<String> pictures = complaintDetailVo.getPictures();
                        List<PicturesVo> pics = new ArrayList<>();
                        for (String picture : pictures) {
                            PicturesVo picturesVo = new PicturesVo();
                            picturesVo.setPictureUrl(picture);
                            pics.add(picturesVo);
                        }
                        if (pics.size() > 0) {
                            mSlideShowView.setImageUrlList(pics);
                        }
                    }
                    tvBriefDesc.setText(complaintDetailVo.getTitle());
                    tvLinkPhone.setText(complaintDetailVo.getLinkmanPhoneNumber());
                    if (!TextUtils.isEmpty(complaintDetailVo.getUploadDate())) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        try {
                            Date parse = simpleDateFormat.parse(complaintDetailVo.getUploadDate());
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd hh:mm");
                            tvUpDate.setText(simpleDateFormat1.format(parse));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    tvAddressDetail.setText(complaintDetailVo.getDescription());
                }
            }

            @Override
            public void onFailure(Call<RepairAndComplaintResp<RepairAndComplaintDetailVo>> call, Throwable t) {

            }
        });
    }
}
