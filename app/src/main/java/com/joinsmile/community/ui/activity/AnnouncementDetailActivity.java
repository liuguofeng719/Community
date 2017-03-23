package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.AnnouncementResp;
import com.joinsmile.community.bean.AnnouncementVo;
import com.joinsmile.community.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AnnouncementDetailActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_publish_date)
    TextView tvPublishDate;
    @InjectView(R.id.tv_content)
    TextView tvContent;
    private Bundle extras;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.announcement_deatil;
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
        AnnouncementResp announcement = (AnnouncementResp) extras.getSerializable("announcement");
        AnnouncementVo announcementVo = announcement.getAnnouncement();
        tv_header_title.setText(announcementVo.getBuildingName() + "公告");
        tvTitle.setText(announcementVo.getTitle());
        tvPublishDate.setText("发布日期:"+announcementVo.getPublishDate());
        tvContent.setText(announcementVo.getContent());

    }
}
