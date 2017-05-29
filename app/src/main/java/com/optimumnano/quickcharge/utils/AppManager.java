package com.optimumnano.quickcharge.utils;

import android.app.Activity;
import android.content.Context;

import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.activity.login.LoginActivity;

import org.xutils.common.util.LogUtil;

import java.util.Stack;

/**
 * Created by ds on 2017/1/12.
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;
    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        synchronized (AppManager.class){
            if (instance == null) {
                instance = new AppManager();
            }
        }
        return instance;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        LogUtil.i("size=="+activityStack.size());
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
                break;
            }
        }
        activityStack.clear();
    }
    /**
     * 结束除登录界面的所有Activity
     */
    public void finishAllActivityExcludeLoginActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size - 1; i++) {
            if (!(activityStack.get(i) instanceof LoginActivity)) {
                finishActivity(activityStack.get(i));
            }
        }
        //activityStack.clear();
    }
    /**
     * 结束除MainActivity以外的所有其他Activity
     */
    public void finishAllActivityExcludeMainActivity() {
        int size = activityStack.size();
        for (int i = 0;i < size-1;  i++) {
            if (!(activityStack.get(i) instanceof MainActivity)) {
                finishActivity(activityStack.get(i));
            }
        }
        //activityStack.clear();
    }
    /**
     * 获取指定的Activity
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }
    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
        // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public void removeActivity(Activity activity){
        if (activity!=null){
            activityStack.remove(activity);
        }
    }

}
