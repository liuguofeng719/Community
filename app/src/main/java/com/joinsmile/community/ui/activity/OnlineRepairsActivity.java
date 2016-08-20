package com.joinsmile.community.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.joinsmile.community.R;
import com.joinsmile.community.bean.RepairAndComplaintsResp;
import com.joinsmile.community.bean.RepairAndComplaintsVo;
import com.joinsmile.community.crop.CropFileUtils;
import com.joinsmile.community.crop.PhotoActionHelper;
import com.joinsmile.community.ui.base.BaseActivity;
import com.joinsmile.community.utils.AppPreferences;
import com.joinsmile.community.utils.CommonUtils;
import com.joinsmile.community.utils.Const;
import com.joinsmile.community.utils.DensityUtils;
import com.joinsmile.community.utils.TLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuguofeng719 on 2016/7/11.
 */
public class OnlineRepairsActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_submit)
    TextView tvSubmit;
    @InjectView(R.id.tv_house_number)
    TextView tvHouseNumber;
    @InjectView(R.id.tv_phone)
    TextView tv_phone;
    @InjectView(R.id.edit_question_title)
    EditText edit_question_title;
    @InjectView(R.id.edit_description)
    EditText edit_description;

    @InjectView(R.id.ly_img_container)
    LinearLayout lycontainer;
    @InjectView(R.id.iv_plus)
    FrameLayout iv_plus;

//    @InjectView(R.id.recyclerView)
//    RecyclerView recyclerView;

    private Bundle extras;
    private List<Bitmap> imageBytes = new CopyOnWriteArrayList<>();
    private Call<RepairAndComplaintsResp<RepairAndComplaintsVo>> repairAndComplaintsVoCall;
    private HomeAdapter homeAdapter;
    private Dialog dialog;
    private String mOutputPath;

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private Dialog showDialog;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //拍照和选择本地照片
    @OnClick(R.id.iv_plus)
    public void ivplus() {
        dialog = CommonUtils.createDialog(this);
        dialog.setContentView(R.layout.select_pic_activity);
        RadioGroup radio_group = (RadioGroup) dialog.findViewById(R.id.radio_group);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                CommonUtils.dismiss(dialog);
                switch (checkedId) {
                    case R.id.tv_take_photo:
                        PhotoActionHelper.takePhoto(OnlineRepairsActivity.this).output(mOutputPath).maxOutputWidth(800).requestCode(Const.REQUEST_TAKE_PHOTO).start();
                        break;
                    case R.id.tv_location_photo:
                        try {
                            //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
                            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, CODE_GALLERY_REQUEST);
                        } catch (ActivityNotFoundException e) {
                        }
                        break;

                }
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    //选择报修的房子
    @OnClick(R.id.tv_house_number)
    public void tvHouseNumber() {
        readyGoForResult(SelectHouseNumberActivity.class, 2);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    //提交报修
    @OnClick(R.id.tv_submit)
    public void tvSubmit() {
        //validate
        if (validate()) {
            showDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, RequestBody> map = new HashMap<>();
                    int index = 0;
                    for (Bitmap imageByte : imageBytes) {
                        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), CommonUtils.Bitmap2Bytes(imageByte));
                        map.put("image\"; filename=\"icon" + index + ".png", requestBody);
                        index++;
                    }
                    map.put("userID", CommonUtils.toRequestBody(AppPreferences.getString("userId")));
                    map.put("apartmentNumberID", CommonUtils.toRequestBody(tvHouseNumber.getTag().toString()));
                    map.put("title", CommonUtils.toRequestBody(edit_question_title.getText().toString()));
                    map.put("description", CommonUtils.toRequestBody(edit_description.getText().toString()));
                    map.put("linkmanPhoneNumber", CommonUtils.toRequestBody(tv_phone.getText().toString()));
                    map.put("isRepair", CommonUtils.toRequestBody("" + extras.getInt("isRepair")));
                    repairAndComplaintsVoCall = getApisNew().addRepairAndComplaints(map);
                    repairAndComplaintsVoCall.enqueue(new Callback<RepairAndComplaintsResp<RepairAndComplaintsVo>>() {
                        @Override
                        public void onResponse(Call<RepairAndComplaintsResp<RepairAndComplaintsVo>> call,
                                               Response<RepairAndComplaintsResp<RepairAndComplaintsVo>> response) {
                            CommonUtils.dismiss(showDialog);
                            TLog.d(TAG_LOG, response.body().toString());
                            if (response.isSuccessful() && response.body().isSuccessfully()) {
                                CommonUtils.make(OnlineRepairsActivity.this, "上报成功");
                                readyGoThenKill(IndexActivity.class);
                            }
                        }

                        @Override
                        public void onFailure(Call<RepairAndComplaintsResp<RepairAndComplaintsVo>> call, Throwable t) {
                            CommonUtils.dismiss(showDialog);
                        }
                    });
                }
            }).start();
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(tvHouseNumber.getTag().toString())) {
            CommonUtils.make(this, getString(R.string.house_not_empty));
            return false;
        }
        if (TextUtils.isEmpty(tv_phone.getText().toString())) {
            CommonUtils.make(this, getString(R.string.phone_not_empty));
            return false;
        }
        if (TextUtils.isEmpty(edit_question_title.getText().toString())) {
            CommonUtils.make(this, getString(R.string.title_not_empty));
            return false;
        }
        if (TextUtils.isEmpty(edit_description.getText().toString())) {
            CommonUtils.make(this, getString(R.string.content_not_empty));
            return false;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String numberId = intent.getStringExtra("numberId");
        String doneLoction = intent.getStringExtra("doneLoction");
        tvHouseNumber.setText(doneLoction);
        tvHouseNumber.setTag(numberId);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.online_repairs_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText(extras.getString("title"));
        tv_phone.setHint(extras.getString("hintPhone"));
        mOutputPath = new File(getExternalCacheDir(), "face.jpg").getPath();
        showDialog = CommonUtils.showDialog(this, getString(R.string.common_loading_message));
    }

    class HomeAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<Bitmap> data = new ArrayList<>();

        public HomeAdapter() {
        }

        public List<Bitmap> getData() {
            return data;
        }

        public void setData(List<Bitmap> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TLog.d("onCreateViewHolder", "BusinessAdapter -> onCreateViewHolder()");
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            TLog.d("onBindViewHolder", "BusinessAdapter -> onBindViewHolder()");
            Bitmap imageVo = data.get(position);
            holder.iv_photo.setImageBitmap(imageVo);
            holder.iv_del.setTag(position);
            holder.iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = Integer.parseInt(v.getTag().toString());
                    imageBytes.remove(index);
                    homeAdapter.getData().remove(index);
                    homeAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_photo;
        ImageView iv_del;

        public MyViewHolder(View view) {
            super(view);
            iv_photo = ButterKnife.findById(view, R.id.iv_photo);
            iv_del = ButterKnife.findById(view, R.id.iv_del);
        }
    }

    class ImageVo {

        private Bitmap bitmap;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

    @Override
    protected void onPause() {
        if (repairAndComplaintsVoCall != null) {
            repairAndComplaintsVoCall.clone();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (repairAndComplaintsVoCall != null) {
            repairAndComplaintsVoCall.clone();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == Const.REQUEST_CLIP_IMAGE || requestCode == Const.REQUEST_TAKE_PHOTO)) {
            String path = PhotoActionHelper.getOutputPath(data);
            if (path != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                setImage(bitmap);
            }
            return;
        } else if (requestCode == CODE_GALLERY_REQUEST) {//选择本地图片
            if (data.getData() != null) {
                Uri uri = data.getData();
                if (uri == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                        saveImage(photo, mOutputPath);
                        PhotoActionHelper.clipImage(this).input(mOutputPath).output(mOutputPath).requestCode(Const.REQUEST_CLIP_IMAGE).start();
                    }
                } else {
                    PhotoActionHelper.clipImage(this).input(CropFileUtils.getPath(this, uri)).output(mOutputPath).requestCode(Const.REQUEST_CLIP_IMAGE).start();
                }
            }
        } else if (resultCode == 4) {
            String numberName = data.getStringExtra("numberName");
            String numberId = data.getStringExtra("numberId");
            tvHouseNumber.setText(numberName);
            tvHouseNumber.setTag(numberId);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImage(final Intent data) {
        final ContentResolver contentResolver = this.getContentResolver();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (data != null) {
                    //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                    Uri mImageCaptureUri = data.getData();
                    //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {
                        Bitmap image;
                        try {
                            //这个方法是根据Uri获取Bitmap图片的静态方法
                            image = MediaStore.Images.Media.getBitmap(contentResolver, mImageCaptureUri);
                            OnlineRepairsActivity.this.setImage(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                            Bitmap image = extras.getParcelable("data");
                            setImage(image);
                        }
                    }
                }
            }
        });
    }


    private void setImage(Bitmap image) {
        if (imageBytes.size() == 4) {
            imageBytes.add(image);
            iv_plus.setVisibility(View.GONE);
        } else {
            imageBytes.add(image);
        }
        addImage(image);
    }

    private void addImage(Bitmap image) {
        if (image != null) {
            Bitmap compressImage = CommonUtils.compressImage(image);
            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(compressImage);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(DensityUtils.dip2px(this, 60), DensityUtils.dip2px(this, 60));
            lp.setMargins(0, 0, DensityUtils.dip2px(this, 8), 0);
            imageView.setLayoutParams(lp);

            ImageView delImage = new ImageView(this);
            delImage.setImageResource(R.drawable.icon_del);
            delImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            FrameLayout.LayoutParams delImagelp = new FrameLayout.LayoutParams(DensityUtils.dip2px(this, 20), DensityUtils.dip2px(this, 20));
            delImagelp.setMargins(0, 5, 30, 0);
            delImagelp.gravity = Gravity.RIGHT;//此处相当于布局文件中的android:layout_gravity属性
            delImage.setLayoutParams(delImagelp);
            delImage.setTag(imageBytes.size() - 1);
            delImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageBytes.remove(Integer.parseInt(v.getTag().toString()));
                    lycontainer.removeAllViews();
                    for (Bitmap image : imageBytes) {
                        addImage(image);
                    }
                }
            });
            frameLayout.addView(imageView);
            frameLayout.addView(delImage);
            lycontainer.addView(frameLayout);
        }
    }
}
