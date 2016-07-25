package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.SparseBooleanUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/7/20.
 */
public class WheelPickerActivity extends BaseActivity {

    @InjectView(R.id.lv_list_view)
    ListView lv_list_view;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;

    private ListViewDataAdapter listViewDataAdapter;
    private Bundle bundle;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @OnClick(R.id.tv_done)
    public void done() {
        int role = 0;
        SparseBooleanArray booleanArray = SparseBooleanUtils.getSparseBooleanArray();
        for (int i = 0; i < booleanArray.size(); i++) {
            if (booleanArray.get(i)) {
                role = i;
                break;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt("role", role);
        readyGoThenKill_CLEAR_TOP_SINGLE_TOP(ApartmentOwnerPhoneNumberActivity.class, bundle);
        finish();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        bundle = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.wheelpicker_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("请选择角色");
        List data = getList();
        initData();
        SparseBooleanUtils.putBoolean(bundle.getInt("role"), true);
        listViewDataAdapter = new ListViewDataAdapter<String>(new ViewHolderCreator() {
            @Override
            public ViewHolderBase createViewHolder(int position) {
                return new ViewHolderBase<String>() {
                    TextView tv_title;
                    FrameLayout fl_proxy_role;
                    CheckBox checkBox;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.wheelpicker_item_activity, null);
                        tv_title = ButterKnife.findById(view, R.id.tv_title);
                        fl_proxy_role = ButterKnife.findById(view, R.id.fl_proxy_role);
                        checkBox = ButterKnife.findById(view, R.id.checkbox);
                        return view;
                    }

                    @Override
                    public void showData(int position, String itemData) {
                        if (SparseBooleanUtils.getSparseBooleanArray().get(position)) {
                            checkBox.setChecked(true);
                            checkBox.setVisibility(View.VISIBLE);
                        } else {
                            checkBox.setChecked(false);
                            checkBox.setVisibility(View.INVISIBLE);
                        }
                        tv_title.setText(itemData);
                        fl_proxy_role.setTag(position);
                        fl_proxy_role.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SparseBooleanUtils.resetBoolean();
                                SparseBooleanUtils.putBoolean(Integer.parseInt(v.getTag().toString()), true);
                                listViewDataAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
            }
        });
        listViewDataAdapter.getDataList().clear();
        listViewDataAdapter.getDataList().addAll(data);
        lv_list_view.setAdapter(listViewDataAdapter);
    }

    private void initData() {
        SparseBooleanUtils.putBoolean(0, false);
        SparseBooleanUtils.putBoolean(1, false);
        SparseBooleanUtils.putBoolean(2, false);
    }

    @NonNull
    private List getList() {
        List data = new ArrayList<String>();
        data.add("业主本人");
        data.add("代理业主");
        data.add("家庭成员");
        return data;
    }
}
