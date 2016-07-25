package com.joinsmile.community.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.ApartmentNumbersResp;
import com.joinsmile.community.bean.ApartmentNumbersVo;
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
 * @desc 选择门牌号
 */
public class ApartmentNumbersActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_select_location)
    TextView tvSelectLocation;
    @InjectView(R.id.lv_list_view)
    ListView lvListView;
    private Bundle bundleData;
    private ListViewDataAdapter<ApartmentNumbersVo> listViewDataAdapter;

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
        tvHeaderTitle.setText(getString(R.string.select_house_number));
        tvSelectLocation.setText(bundleData.getString("endLocation"));
        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<ApartmentNumbersVo>() {
            @Override
            public ViewHolderBase<ApartmentNumbersVo> createViewHolder(int position) {
                return new ViewHolderBase<ApartmentNumbersVo>() {
                    TextView tv_house_property;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.house_property_item_activity, null);
                        tv_house_property = ButterKnife.findById(view, R.id.tv_house_property);
                        return view;
                    }

                    @Override
                    public void showData(int position, ApartmentNumbersVo itemData) {
                        tv_house_property.setText(itemData.getNumberName());
                        tv_house_property.setTag(itemData.getNumberID());
                    }
                };
            }
        });
        this.lvListView.setAdapter(listViewDataAdapter);
        this.lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView houseProperty = (TextView) view.findViewById(R.id.tv_house_property);
                Bundle bundle = new Bundle();
                bundle.putString("numberId", houseProperty.getTag().toString());
                bundle.putString("doneLocation", bundleData.getString("endLocation") + "—" + houseProperty.getText());
                readyGo(ApartmentOwnerPhoneNumberActivity.class, bundle);
            }
        });
        getVillage(bundleData.getString("unitId"));
    }


    private void getVillage(String unitId) {
        showLoading(getString(R.string.common_loading_message));
        Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> callVillage = getApisNew().getApartmentNumbers(unitId).clone();
        callVillage.enqueue(new Callback<ApartmentNumbersResp<List<ApartmentNumbersVo>>>() {
            @Override
            public void onResponse(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call,
                                   Response<ApartmentNumbersResp<List<ApartmentNumbersVo>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    List<ApartmentNumbersVo> apartmentNumbersVos = response.body().getApartmentNumberList();
                    ArrayList<ApartmentNumbersVo> dataList = listViewDataAdapter.getDataList();
                    dataList.clear();
                    dataList.addAll(apartmentNumbersVos);
                } else {
                    CommonUtils.make(ApartmentNumbersActivity.this, CommonUtils.getCodeToStr(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> call, Throwable t) {
                hideLoading();
                if ("timeout".equals(t.getMessage())) {
                    CommonUtils.make(ApartmentNumbersActivity.this, "网络请求超时");
                }
            }
        });
    }
}
