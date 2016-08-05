package com.joinsmile.community.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.AuthticationVo;
import com.joinsmile.community.ui.activity.LoginActivity;
import com.joinsmile.community.ui.activity.MyVillageActivity;
import com.joinsmile.community.ui.activity.UserInfoActivity;
import com.joinsmile.community.ui.base.BaseFragment;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.widgets.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.InjectView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {

    @InjectView(R.id.iv_face)
    CircleImageView iv_face;
    @InjectView(R.id.tv_login_title)
    TextView tv_login_title;
    @InjectView(R.id.tv_not_login_title)
    TextView tv_not_login_title;

    //我的小区
    @OnClick(R.id.tv_my_community)
    public void tvMyCommunity() {
        readyGo(MyVillageActivity.class);
    }

    @OnClick(R.id.rl_head_face)
    public void login() {
        if (isLogin()) {
            readyGo(UserInfoActivity.class);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    //我的投票
    @OnClick(R.id.tv_my_vote)
    public void tvMyVote() {

    }

    //我的订单
    @OnClick(R.id.tv_my_order)
    public void tvMyOrder() {

    }

    //我的缴费
    @OnClick(R.id.tv_my_payment)
    public void tvMyPayment() {

    }

    //我的收藏
    @OnClick(R.id.tv_my_favorites)
    public void tvMyFavorites() {

    }

    //帮助中心
    @OnClick(R.id.tv_help)
    public void tvHelp() {

    }

    //关于我们
    @OnClick(R.id.tv_about)
    public void tvabout() {

    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin();
    }

    //检查用户是否登录
    private boolean isLogin() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            tv_login_title.setVisibility(View.GONE);
            tv_not_login_title.setVisibility(View.VISIBLE);
            return false;
        } else {
            AuthticationVo authticationVo = AppPreferences.getObject(AuthticationVo.class);
            tv_login_title.setVisibility(View.VISIBLE);
            tv_login_title.setText(authticationVo.getPhoneNumber());
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
            builder.showImageForEmptyUri(R.mipmap.logo);
            builder.showImageOnFail(R.mipmap.logo);
            builder.showImageOnLoading(R.mipmap.logo);
            ImageLoader.getInstance().displayImage(authticationVo.getHeadPicture(), iv_face, builder.build());
            tv_not_login_title.setVisibility(View.GONE);

            return true;
        }
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {
    }

    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_info;
    }

}
