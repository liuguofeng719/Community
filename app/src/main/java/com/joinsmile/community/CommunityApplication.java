package com.joinsmile.community;

import android.app.Application;
import android.content.Context;

import com.joinsmile.community.utils.CrashHandler;
import com.joinsmile.community.utils.LocationService;
import com.joinsmile.community.utils.TLog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class CommunityApplication extends Application {

    public static CommunityApplication ticketsApplication;
    public LocationService locationService;

    public static CommunityApplication getTicketApplication() {
        return ticketsApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ticketsApplication = this;
        initImageLoader(this);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
        //TODO 禁用和启用Log
        TLog.enableLog();
//        TLog.disableLog();
        //百度地图定位
//        SDKInitializer.initialize(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
//                    .writeDebugLogs() // Remove for release app
                    .build();
            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
