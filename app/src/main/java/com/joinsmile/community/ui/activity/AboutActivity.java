package com.joinsmile.community.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.about;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("关于我们");
    }
}
