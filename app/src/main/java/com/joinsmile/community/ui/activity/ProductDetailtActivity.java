package com.joinsmile.community.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.joinsmile.community.R;
import com.joinsmile.community.bean.BaseInfoVo;
import com.joinsmile.community.bean.ProductResp;
import com.joinsmile.community.bean.ProductVo;
import com.joinsmile.community.bean.ShoppingCartVo;
import com.joinsmile.community.ui.adpater.base.ListViewDataAdapter;
import com.joinsmile.community.ui.adpater.base.ViewHolderBase;
import com.joinsmile.community.ui.adpater.base.ViewHolderCreator;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.widgets.ListViewForScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/28.
 * 商品详情
 */
public class ProductDetailtActivity extends BaseActivity {

    @InjectView(R.id.iv_product_img)
    ImageView ivProductImg;
    @InjectView(R.id.tv_product_title)
    TextView tvProductTitle;
    @InjectView(R.id.tv_product_price)
    TextView tvProductPrice;
    @InjectView(R.id.tv_inventory_total)
    TextView tvInventoryTotal;
    @InjectView(R.id.edit_product_number)
    EditText editProductNumber;
    @InjectView(R.id.radio_group)
    RadioGroup radioGroup;
    @InjectView(R.id.tv_pic_detail)
    RadioButton tvPicDetail;
    @InjectView(R.id.tv_appraise)
    RadioButton tvAppraise;
//    @InjectView(R.id.lv_list_view)
//    ListViewForScrollView lvListView;
    @InjectView(R.id.webView)
    WebView webView;

    private Bundle extras;
    private ListViewDataAdapter<String> listViewDataAdapter;
    private ProductVo productVo;
    private WebSettings webSettings;

    //加入收藏
    @OnClick(R.id.tv_collect)
    public void tvCollect() {

    }

    //减
    @OnClick(R.id.iv_reduce)
    public void ivReduce() {
        if (TextUtils.isEmpty(editProductNumber.getText()) || editProductNumber.getText().toString().equals("0")) {
            editProductNumber.setText("1");
            return;
        }
        int count = Integer.parseInt(editProductNumber.getText().toString());
        int tmp = count - 1;
        editProductNumber.setText("" + tmp);
    }

    //加
    @OnClick(R.id.iv_plus)
    public void ivPlus() {
        if (TextUtils.isEmpty(editProductNumber.getText())) {
            editProductNumber.setText("" + 1);
        } else {
            int count = Integer.parseInt(editProductNumber.getText().toString());
            int tmp = count + 1;
            editProductNumber.setText("" + tmp);
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    //打电话
    @OnClick(R.id.iv_call_phone)
    public void ivCallPhone() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            callPhone();
        }
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "0873-7183123");
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                // Permission Denied
                Toast.makeText(ProductDetailtActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //去购物车页面
    @OnClick(R.id.iv_shop_car)
    public void ivShopCar() {
        if (checkLogin()) {
            readyGo(MyShopCarActivity.class);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    //加入购物车
    @OnClick(R.id.tv_join_car)
    public void tvJoinCar() {
        if (!checkLogin()) {
            readyGo(LoginActivity.class);
        }
        if (TextUtils.isEmpty(editProductNumber.getText())) {
            CommonUtils.make(this, "商品数量不能为空!");
            return;
        }
        if ("0".equals(editProductNumber.getText())) {
            CommonUtils.make(this, "商品数量不能为0!");
            return;
        }

        Call<BaseInfoVo> infoVoCall = getApisNew().AddProductToShoppingCart(AppPreferences.getString("userId"),
                tvProductTitle.getTag().toString(),
                Integer.parseInt(editProductNumber.getText().toString())).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Call<BaseInfoVo> call, Response<BaseInfoVo> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    CommonUtils.make(ProductDetailtActivity.this, "添加购物车成功");
                } else {
                    CommonUtils.make(ProductDetailtActivity.this, response.body().getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseInfoVo> call, Throwable t) {

            }
        });
    }

    //去订单确认页
    @OnClick(R.id.tv_buy)
    public void tvBuy() {
        if (checkLogin()) {
            List<ShoppingCartVo> list = new ArrayList<>();
            ShoppingCartVo shoppingCartVo = new ShoppingCartVo();
            shoppingCartVo.setAmount(editProductNumber.getText().toString());
            shoppingCartVo.setProductName(productVo.getProductName());
            shoppingCartVo.setProductID(productVo.getProductID());
            shoppingCartVo.setProductPicture(productVo.getDefaultPicture());
            shoppingCartVo.setProductPrice(productVo.getUnitPrice());
            list.add(shoppingCartVo);
            Bundle bundle = new Bundle();
            bundle.putString("carts", new Gson().toJson(list));
            readyGo(OrderConfirmActivity.class, bundle);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.product_detail_activity;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
//        listViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<String>() {
//
//            @Override
//            public ViewHolderBase<String> createViewHolder(int position) {
//                final DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//                builder.bitmapConfig(Bitmap.Config.RGB_565);
//                builder.cacheInMemory(true);
//                builder.cacheOnDisk(true);
//                builder.considerExifParams(true);
//                builder.showImageForEmptyUri(R.drawable.no_banner);
//                builder.showImageOnFail(R.drawable.no_banner);
//                builder.showImageOnLoading(R.drawable.no_banner);
//
//                return new ViewHolderBase<String>() {
//                    ImageView imageView;
//
//                    @Override
//                    public View createView(LayoutInflater layoutInflater) {
//                        View view = layoutInflater.inflate(R.layout.product_detail_pic_item, null);
//                        imageView = ButterKnife.findById(view, R.id.iv_product_detail_pic);
//                        return view;
//                    }
//
//                    @Override
//                    public void showData(int position, String itemData) {
//                        Glide.with(ProductDetailtActivity.this).load(itemData).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                if (imageView == null) {
//                                    return false;
//                                }
//                                if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
//                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                                }
//                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
//                                int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
//                                float scale = (float) vw / (float) resource.getIntrinsicWidth();
//                                int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                                params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
//                                imageView.setLayoutParams(params);
//                                return false;
//                            }
//                        })
//                                .placeholder(R.drawable.no_banner)
//                                .into(imageView);
////                        ImageLoader.getInstance().displayImage(itemData, imageView, builder.build());
//                    }
//                };
//            }
//        });
//        lvListView.setAdapter(listViewDataAdapter);
        getProductById();
    }

    //通过商品编码获取商品
    private void getProductById() {
        Call<ProductResp<ProductVo>> productRespCall = getApisNew().getProduct(extras.getString("productId")).clone();
        productRespCall.enqueue(new Callback<ProductResp<ProductVo>>() {

            @Override
            public void onResponse(Call<ProductResp<ProductVo>> call,
                                   Response<ProductResp<ProductVo>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessfully()) {
                    productVo = response.body().getProduct();
                    final DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    builder.bitmapConfig(Bitmap.Config.RGB_565);
                    builder.cacheInMemory(true);
                    builder.cacheOnDisk(true);
                    builder.considerExifParams(true);
                    builder.showImageForEmptyUri(R.drawable.no_banner);
                    builder.showImageOnFail(R.drawable.no_banner);
                    builder.showImageOnLoading(R.drawable.no_banner);
                    ImageLoader.getInstance().displayImage(productVo.getDefaultPicture(), ivProductImg, builder.build());
                    tvProductTitle.setTag(productVo.getProductID());
                    tvProductTitle.setText(productVo.getProductName());
                    tvProductPrice.setText("￥" + productVo.getUnitPrice());
                    tvInventoryTotal.setText("" + productVo.getSalesVolume());
                    ArrayList<String> pictures = productVo.getPictures();
//                    ArrayList<String> dataList = listViewDataAdapter.getDataList();
//                    dataList.clear();
//                    dataList.addAll(pictures);
//                    listViewDataAdapter.notifyDataSetChanged();
                    initWebView(pictures);
                }
            }

            @Override
            public void onFailure(Call<ProductResp<ProductVo>> call, Throwable t) {

            }
        });
    }

    public void initWebView(List<String> strings) {
        webView.setFocusable(false);
        webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBlockNetworkImage(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new GoodsDetailWebViewClient());
        load(webView,strings);
    }

    public void load(WebView webView,List<String> strings){
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"; charset=\" />\n" +
                "        <meta name=\"copyright\" content=\"\" />\n" +
                "        <meta name=\"keywords\" content=\"\" />\n" +
                "        <meta name=\"description\" content=\"\" />\n" +
                "        <meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\" />\n" +
                "        <title></title>\n" +
                "        <style type=\"text/css\">\n" +
                "          html{min-height:100%;background:#FFF;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%}body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,textarea,p,blockquote,th,td{margin:0;padding:0;-webkit-tap-highlight-color:rgba(0,0,0,0)}body{min-width:320px;max-width:640px;min-height:100%;line-height:1.4;margin:0 auto;font:14px/1.4\"Helvetica Neue\", Helvetica, STHeiTi, sans-serif, tahoma, arial, \\5b8b\\4f53;color:#444}article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section{display:block;margin:0;padding:0}audio,canvas,video{display:inline-block;*display:inline;*zoom:1}input:focus,a:focus{outline:none}fieldset,img{border:0}address,caption,cite,code,dfn,em,strong,th,var,i{font-style:normal;font-weight:normal}ol,ul{list-style:none}caption,th{text-align:left}h1,h2,h3,h4,h5,h6{font-size:100%;font-weight:normal}q:before,q:after{content:''}abbr,acronym{border:0;font-variant:normal}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}:root sub,:root sup{vertical-align:baseline}sup{top:-0.5em}sub{bottom:-0.25em}button,input,textarea,select{font-family:tahoma, arial, simsun, sans-serif;font-size:inherit;font-weight:inherit;line-height:1.5;vertical-align:middle}button,input,textarea,select{*font-size:100%}textarea{resize:none}table{border-collapse:collapse;border-spacing:0}th{text-align:inherit}a{vertical-align:baseline;color:#444;}a:hover{text-decoration:none}ins,a{text-decoration:none}del{text-decoration:line-through}.clear{display:block;float:none;clear:both;overflow:hidden;visibility:hidden;width:0;height:0;background:none;border:0;font-size:0}.cfix:before,.cfix:after{content:\"\";display:table}.cfix:after{clear:both}.cfix{*zoom:1}.fl{float:left;display:inline}.fr{float:right;display:inline}.dib-wrap{font-size:0;letter-spacing:-.31em;*letter-spacing:normal}.dib-wrap .dib{font-size:12px;letter-spacing:normal;word-spacing:normal;vertical-align:top}.dib{display:inline-block;*display:inline;*zoom:1}.hide{display:none}\n" +
                "          a img {vertical-align: middle;}\n" +
                "\n" +
                "          /*图文详情*/\n" +
                "          .des_cnt {padding:10px 5px; -webkit-box-sizing:border-box; box-sizing:border-box;}\n" +
                "          .des_cnt .des_item h2 { font:bold 12px/18px; padding-bottom:3px; border-bottom:3px solid #000;}\n" +
                "          .des_cnt .des_img img {width:100% !important; height:auto !important;}\n" +
                "          .des_cnt .des_item  p{ line-height:1.7em; text-indent:15px;}\n" +
                "          .des_cnt .des_item  .dl_cnt{ font-weight:bold;}\n" +
                "        </style>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div class=\"mobile_sp_body\">\n" +
                "          <div class=\"des_cnt cfix\">\n" +
                "              <div class=\"des_item cfix\">\n" +
                "                  <div class=\"des_img\">\n" +
                "                    <div style=\"text-align:center;\">");
        for (String string : strings) {
            sb.append("<img src='"+string+"' />");
        }
        sb.append("                   </div>\n" +
                "                  </div>\n" +
                "              </div>\n" +
                "          </div>\n" +
                "      </div>\n" +
                "    </body>\n" +
                "</html>");
        webView.loadDataWithBaseURL(null,sb.toString(), "text/html", "utf-8", null);
    }

    private class GoodsDetailWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webSettings.setBlockNetworkImage(false);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }
    }

}
