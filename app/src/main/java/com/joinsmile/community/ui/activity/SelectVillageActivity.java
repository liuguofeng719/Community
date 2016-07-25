package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ResidentialBuildingVo;
import com.joinsmile.community.bean.ResidentialListResp;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/14.
 *
 * @desc 选择小区
 */
public class SelectVillageActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_select_city)
    TextView tvSelectCity;
    @InjectView(R.id.edit_query)
    EditText editQuery;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    private Bundle bundleData;
    private ListViewDataAdapter<ResidentialBuildingVo> listViewDataAdapter;
    private List<ResidentialBuildingVo> cityList;
    private ConcurrentHashMap<String, List<ResidentialBuildingVo>> rbvo = new ConcurrentHashMap<>();

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.select_village_activity;
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
        tvHeaderTitle.setText(getString(R.string.select_village));
        tvSelectCity.setText(bundleData.getString("cityName"));
        editQuery.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    String string = s.toString();
                    if (cityList != null && cityList.size() > 0) {
                        List<ResidentialBuildingVo> newList = new ArrayList<>();
                        for (ResidentialBuildingVo residentialBuildingVo : cityList) {
                            if (!TextUtils.isEmpty(residentialBuildingVo.getBuildingName())) {
                                if (residentialBuildingVo.getBuildingName().trim().contains(string)) {
                                    newList.add(residentialBuildingVo);
                                }
                            }
                        }
                        if (newList.size() > 0) {
                            ArrayList<ResidentialBuildingVo> dataList = listViewDataAdapter.getDataList();
                            dataList.clear();
                            dataList.addAll(newList);
                            listViewDataAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    ArrayList<ResidentialBuildingVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(rbvo.get("cache"));
                    listViewDataAdapter.notifyDataSetChanged();
                }
            }
        });
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ResidentialBuildingVo>() {
            @Override
            public ViewHolderBase<ResidentialBuildingVo> createViewHolder(int position) {
                return new ViewHolderBase<ResidentialBuildingVo>() {
                    TextView tv_village_name;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.select_village_item_activity, null);
                        tv_village_name = ButterKnife.findById(view, R.id.tv_village_name);
                        return view;
                    }

                    @Override
                    public void showData(int position, ResidentialBuildingVo itemData) {
                        tv_village_name.setText(itemData.getBuildingName());
                        tv_village_name.setTag(itemData.getBuildingID());
                    }
                };
            }
        });
        this.lvListView.setAdapter(listViewDataAdapter);
        this.lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_village_name = (TextView) view.findViewById(R.id.tv_village_name);
                Bundle bundle = new Bundle();
                bundle.putString("selectLocation", bundleData.getString("cityName") + "-" + tv_village_name.getText());
                bundle.putString("buildingId", tv_village_name.getTag().toString());
                readyGo(HousePropertyActivity.class, bundle);
            }
        });
        getVillage(bundleData.getString("cityId"));
    }


    private void getVillage(String cityId) {
        showLoading(getString(R.string.common_loading_message));
        Call<ResidentialListResp<List<ResidentialBuildingVo>>> callVillage = getApisNew().getResidentialBuildings(cityId).clone();
        callVillage.enqueue(new Callback<ResidentialListResp<List<ResidentialBuildingVo>>>() {
            @Override
            public void onResponse(Call<ResidentialListResp<List<ResidentialBuildingVo>>> call, Response<ResidentialListResp<List<ResidentialBuildingVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    cityList = response.body().getBuildingList();
                    if (cityList.size() == 0) {
                        CommonUtils.make(SelectVillageActivity.this, getString(R.string.empty_village));
                    }
                    ArrayList<ResidentialBuildingVo> dataList = listViewDataAdapter.getDataList();
                    if (rbvo.get("cache") == null) {
                        rbvo.put("cache", cityList);
                    }
                    dataList.clear();
                    dataList.addAll(cityList);
                } else {
                    CommonUtils.make(SelectVillageActivity.this, CommonUtils.getCodeToStr(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResidentialListResp<List<ResidentialBuildingVo>>> call, Throwable t) {
                hideLoading();
                if ("timeout".equals(t.getMessage())) {
                    CommonUtils.make(SelectVillageActivity.this, "网络请求超时");
                }
            }
        });
    }
}
