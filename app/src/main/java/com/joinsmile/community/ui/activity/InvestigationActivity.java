package com.joinsmile.community.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.InvestigationVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/7/25.
 * 调查主题列表
 */
public class InvestigationActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    private ListViewDataAdapter listViewDataAdapter;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.investigation_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.investigation_msg));
        listViewDataAdapter = new ListViewDataAdapter<InvestigationVo>(new ViewHolderCreator<InvestigationVo>() {
            @Override
            public ViewHolderBase<InvestigationVo> createViewHolder(int position) {
                return new ViewHolderBase<InvestigationVo>() {
                    TextView tv_subject;
                    ImageView iv_state;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.investigation_item_activity, null);
                        tv_subject = ButterKnife.findById(view, R.id.tv_subject);
                        iv_state = ButterKnife.findById(view, R.id.iv_state);
                        return view;
                    }

                    @Override
                    public void showData(int position, InvestigationVo itemData) {
                        tv_subject.setText(itemData.getSubject());

                    }
                };
            }
        });
    }
}
