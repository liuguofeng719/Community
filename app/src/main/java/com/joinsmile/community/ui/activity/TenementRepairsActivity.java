package com.joinsmile.community.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/7/11.
 *
 * @desc 物业报修
 */
public class TenementRepairsActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_header)
    FrameLayout tvHeader;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    @InjectView(R.id.tv_phone_repairs)
    TextView tvPhoneRepairs;
    @InjectView(R.id.tv_online_repairs)
    TextView tvOnlineRepairs;
    private ListViewDataAdapter listViewDataAdapter;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }
    @OnClick(R.id.tv_online_repairs)
    public void OnlineRepairs(){
        readyGo(OnlineRepairsActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.tenement_repairs_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.tenement_repairs));

        listViewDataAdapter = new ListViewDataAdapter(new ViewHolderCreator() {
            @Override
            public ViewHolderBase createViewHolder(int position) {

                return new ViewHolderBase() {
                    TextView tv_service_title;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.tenement_repairs_item_activity, null);
                        tv_service_title = ButterKnife.findById(view, R.id.tv_service_title);
                        return view;
                    }

                    @Override
                    public void showData(int position, Object itemData) {

                    }
                };
            }
        });
    }
}
