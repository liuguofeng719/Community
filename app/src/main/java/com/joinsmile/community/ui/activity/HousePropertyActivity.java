package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.BuildingUnitsResp;
import com.joinsmile.community.bean.BuildingUnitsVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/14.
 *
 * @desc 选择单元
 */
public class HousePropertyActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_select_location)
    TextView tvSelectLocation;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    private Bundle bundleData;
    private ListViewDataAdapter<BuildingUnitsVo> listViewDataAdapter;
    private List<BuildingUnitsVo> unitsVoList;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.house_property_activity;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected View getLoadingTargetView() {
        return lvListView;
    }


    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(getString(R.string.select_house));
        tvSelectLocation.setText(bundleData.getString("selectLocation"));
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<BuildingUnitsVo>() {
            @Override
            public ViewHolderBase<BuildingUnitsVo> createViewHolder(int position) {
                return new ViewHolderBase<BuildingUnitsVo>() {
                    TextView tv_house_property;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.house_property_item_activity, null);
                        tv_house_property = ButterKnife.findById(view, R.id.tv_house_property);
                        return view;
                    }

                    @Override
                    public void showData(int position, BuildingUnitsVo itemData) {
                        tv_house_property.setText(itemData.getUnitName());
                        tv_house_property.setTag(itemData.getUnitID());
                    }
                };
            }
        });
        this.lvListView.setAdapter(listViewDataAdapter);
        this.lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_house_property = (TextView) view.findViewById(R.id.tv_house_property);
                Bundle bundle = new Bundle();
                bundle.putString("endLocation", bundleData.getString("selectLocation") + "-" + tv_house_property.getText());
                bundle.putString("unitId", tv_house_property.getTag().toString());
                readyGo(ApartmentNumbersActivity.class, bundle);
            }
        });
        getVillage(bundleData.getString("buildingId"));
    }


    private void getVillage(String cityId) {
        showLoading(getString(R.string.common_loading_message));
        Call<BuildingUnitsResp<List<BuildingUnitsVo>>> callVillage = getApisNew().getBuildingUnits(cityId).clone();
        callVillage.enqueue(new Callback<BuildingUnitsResp<List<BuildingUnitsVo>>>() {
            @Override
            public void onResponse(Call<BuildingUnitsResp<List<BuildingUnitsVo>>> call, Response<BuildingUnitsResp<List<BuildingUnitsVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    unitsVoList = response.body().getBuildingUnitList();
                    ArrayList<BuildingUnitsVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(unitsVoList);
                } else {
                    CommonUtils.make(HousePropertyActivity.this, CommonUtils.getCodeToStr(response.code()));
                }
            }

            @Override
            public void onFailure(Call<BuildingUnitsResp<List<BuildingUnitsVo>>> call, Throwable t) {
                hideLoading();
                if ("timeout".equals(t.getMessage())) {
                    CommonUtils.make(HousePropertyActivity.this, "网络请求超时");
                }
            }
        });
    }
}
