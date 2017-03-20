package com.joinsmile.community.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.CataloguesVo;
import com.joinsmile.community.bean.SubCatalogues;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lgfcxx on 2017/3/17.
 * 商品分类
 */

public class CatalogueActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.list_categories_parent)
    ListView listCategoriesParent;
    @InjectView(R.id.list_categories_children)
    GridView listCategoriesChildren;
    @InjectView(R.id.ly_content)
    LinearLayout lyContent;
    private ListViewDataAdapter<CataloguesVo.Catalogue> catalogueParent;
    private ListViewDataAdapter<SubCatalogues.SubCatalogue> catalogueChildren;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.catalogue_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lyContent;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("商品分类");
        getCategoriesParent();
        catalogueChildren = new ListViewDataAdapter<>(new ViewHolderCreator<SubCatalogues.SubCatalogue>() {
            @Override
            public ViewHolderBase<SubCatalogues.SubCatalogue> createViewHolder(int position) {
                final DisplayImageOptions.Builder builder = getBuilder();
                return new ViewHolderBase<SubCatalogues.SubCatalogue>() {
                    ImageView image;
                    TextView tv_text;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.service_mng_item, null);
                        image = (ImageView) view.findViewById(R.id.image);
                        tv_text = (TextView) view.findViewById(R.id.tv_text);
                        return view;
                    }

                    @Override
                    public void showData(int position, SubCatalogues.SubCatalogue itemData) {
                        ImageLoader.getInstance().displayImage(itemData.getSubCataloguePicture(), image, builder.build());
                        tv_text.setText(itemData.getSubCatalogueName());
                        tv_text.setTag(itemData.getSubCatalogueId());
                    }
                };
            }
        });
        listCategoriesChildren.setAdapter(catalogueChildren);
        listCategoriesChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String subCatalogueId = view.findViewById(R.id.tv_text).getTag().toString();
                Intent intentData = new Intent();
                intentData.putExtra("subCatalogueId",subCatalogueId);
                setResult(30,intentData);
                finish();
            }
        });
    }

    //获取父分类
    private void getCategoriesParent() {

        showLoading(getString(R.string.common_loading_message));

        catalogueParent = new ListViewDataAdapter<>(new ViewHolderCreator<CataloguesVo.Catalogue>() {
            @Override
            public ViewHolderBase<CataloguesVo.Catalogue> createViewHolder(int position) {
                return new ViewHolderBase<CataloguesVo.Catalogue>() {
                    TextView tv_catalogue_name;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View inflate = layoutInflater.inflate(R.layout.catalogue_activity_parent_item, null);
                        tv_catalogue_name = (TextView) inflate.findViewById(R.id.tv_catalogue_name);
                        return inflate;
                    }

                    @Override
                    public void showData(int position, CataloguesVo.Catalogue itemData) {
                        tv_catalogue_name.setText(itemData.getCataloguesName());
                        tv_catalogue_name.setTag(itemData.getCatalogueId());
                    }
                };
            }
        });

        listCategoriesParent.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listCategoriesParent.setAdapter(catalogueParent);
        listCategoriesParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("test", "test=======" + view);
                getCatalogueChildren(view.findViewById(R.id.tv_catalogue_name).getTag().toString());
            }
        });

        Call<CataloguesVo<List<CataloguesVo.Catalogue>>> catalogues = getApisNew().getPrimaryCatalogues();
        catalogues.enqueue(new Callback<CataloguesVo<List<CataloguesVo.Catalogue>>>() {
            @Override
            public void onResponse(Call<CataloguesVo<List<CataloguesVo.Catalogue>>> call,
                                   Response<CataloguesVo<List<CataloguesVo.Catalogue>>> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    CataloguesVo<List<CataloguesVo.Catalogue>> listCataloguesVo = response.body();
                    if (listCataloguesVo.isSuccessfully()) {

                        List<CataloguesVo.Catalogue> primaryCatalogues = listCataloguesVo.getPrimaryCatalogues();
                        ArrayList<CataloguesVo.Catalogue> dataList = catalogueParent.getDataList();
                        dataList.clear();
                        dataList.addAll(primaryCatalogues);
                        catalogueParent.notifyDataSetChanged();
                        listCategoriesParent.setItemChecked(0, true);
                        getCatalogueChildren(dataList.get(0).getCatalogueId());
                    } else {
                        CommonUtils.make(mContext, listCataloguesVo.getErrorMessage());
                    }

                } else {
                    CommonUtils.make(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<CataloguesVo<List<CataloguesVo.Catalogue>>> call, Throwable t) {
                hideLoading();
                CommonUtils.make(mContext, t.getMessage());
            }
        });
    }
    //获取子分类
    private void getCatalogueChildren(String catalogueParentId) {
        Log.d("test", "catalogueParentId=======" + catalogueParentId);
        Call<SubCatalogues<List<SubCatalogues.SubCatalogue>>> subCatalogues = getApisNew().getSubCatalogues(catalogueParentId);
        subCatalogues.enqueue(new Callback<SubCatalogues<List<SubCatalogues.SubCatalogue>>>() {
            @Override
            public void onResponse(Call<SubCatalogues<List<SubCatalogues.SubCatalogue>>> call,
                                   Response<SubCatalogues<List<SubCatalogues.SubCatalogue>>> response) {
                if (response.isSuccessful()) {

                    SubCatalogues<List<SubCatalogues.SubCatalogue>> listSubCatalogues = response.body();
                    if (listSubCatalogues.isSuccessfully()) {

                        List<SubCatalogues.SubCatalogue> catalogues = listSubCatalogues.getSubCatalogues();
                        ArrayList<SubCatalogues.SubCatalogue> dataList = catalogueChildren.getDataList();
                        dataList.clear();
                        dataList.addAll(catalogues);
                        catalogueChildren.notifyDataSetChanged();
                    } else {
                        CommonUtils.make(mContext, listSubCatalogues.getErrorMessage());
                    }

                } else {
                    CommonUtils.make(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<SubCatalogues<List<SubCatalogues.SubCatalogue>>> call, Throwable t) {
                CommonUtils.make(mContext, t.getMessage());
            }
        });
    }
}
