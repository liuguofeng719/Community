package com.joinsmile.community;

import android.app.Application;
import android.content.Context;

import com.joinsmile.community.utils.CrashHandler;
import com.joinsmile.community.utils.LocationService;
import com.joinsmile.community.utils.TLog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
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
                    .threadPoolSize(5) // 设置用于显示图片的线程池大小
                    /**
                     * 你可以设置你自己实现的内存缓存
                     */
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                    /**
                     * 为位图最大内存缓存大小(以字节为单位),默认值,可用应用程序内存的1/8
                     * 注意:如果你使用这个方法,那么LruMemoryCache将被用作内存缓存。
                     * 您可以使用memoryCache(MemoryCacheAware)方法来设置自己的MemoryCacheAware的实现。
                     */
                    .memoryCacheSize(2 * 1024 * 1024)
                    /**
                     * 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                     */
                    .denyCacheImageMultipleSizesInMemory()
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
