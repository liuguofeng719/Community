package com.joinsmile.community.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 双击点击事件抽象类
 * 
 * @author wanglin
 */
public abstract class DoubleClick {
    
    protected Context mContext;
    
    /**
     * 开始任务的时间。
     */
    private long mStartTime;
    
    /**
     * 构造方法，初始化Context对象及开始的任务时间。
     * 
     * @param context
     */
    public DoubleClick(Context context) {
        mContext = context;
        mStartTime = -1;
    }
    
    /**
     * 当某个动作要双击才执行时，调用此方法。
     * 
     * @param delayTime
     *            判断双击的时间。
     * @param msg
     *            当第一次点击时，弹出的提示信息。如果为null，则不作提示。
     */
    public void doDoubleClick(int delayTime, String msg) {
        if (!doInDelayTime(delayTime)) {
            Toast.makeText(mContext, msg, delayTime).show();
        }
    }
    
    /**
     * 如果是在在指定的时间内则执行doOnDoubleClick，否则返回false。
     * 
     * @param delayTime
     *            指定的延迟时间。
     * @return 当且仅当执行了doOnDoubleClick时返回true,否则返回false。
     */
    protected boolean doInDelayTime(int delayTime) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mStartTime <= delayTime) {
            afterDoubleClick();
            mStartTime = -1;
            return true;
        }
        mStartTime = nowTime;
        return false;
    }
    
    /**
     * 当某个动作要双击才执行时，调用此方法。
     * 
     * @param delayTime
     *            判断双击的时间。
     * @param msgResid
     *            当第一次点击时，弹出的提示信息的资源ID。
     */
    public void doDoubleClick(int delayTime, int msgResid) {
        if (!doInDelayTime(delayTime)) {
            Toast.makeText(mContext, msgResid, delayTime).show();
        }
    }
    
    /**
     * 在双击之后执行的事情。
     */
    abstract protected void afterDoubleClick();
}
