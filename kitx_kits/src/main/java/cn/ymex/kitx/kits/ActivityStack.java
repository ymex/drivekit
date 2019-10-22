package cn.ymex.kitx.kits;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Activity 堆栈管理。
 */
public final class ActivityStack implements Application.ActivityLifecycleCallbacks {

    private static ActivityStack activityStack;
    private Queue<Activity> activitieQuene = new LinkedList<>();
    /**
     * 是否自动管理activity,默认为true ，若设置为false 则需要自己手动处理activity 出入栈。
     */
    private boolean autoManage = true;


    private ActivityStack(boolean auto) {
        this.autoManage = auto;
    }

    private ActivityStack() {
        this(true);
    }

    private static synchronized ActivityStack get(boolean auto) {
        if (activityStack == null) {
            activityStack = new ActivityStack(auto);
        }
        return activityStack;
    }

    public static ActivityStack instance() {
        return get(true);
    }


    public void registerActivityLifecycleCallbacks(Application context) {
            context.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 设置是否自动管理activity
     *
     * @param auto
     */
    public ActivityStack autoManage(boolean auto) {
        autoManage = auto;
        return this;
    }

    public Queue<Activity> getQuene() {
        return activitieQuene;
    }

    /**
     * 线束当前Activity
     */
    public void finishCurrentActivity() {
        if (activitieQuene.isEmpty()) {
            return;
        }
        finishActivity(activitieQuene.poll());
    }

    /**
     * 获取当前activity
     *
     * @return Activity , when activity quene is empty return null.
     */
    public Activity getCurrentActivity() {
        if (activitieQuene.isEmpty()) {
            return null;
        }
        return activitieQuene.peek();
    }

    /**
     * 通过类名获取指定activitys
     *
     * @param name activity class name
     * @return 所有同名activity list
     */
    public List<Activity> getActivities(String name) {
        List<Activity> acts = new ArrayList<>();
        for (Activity act : activitieQuene) {
            if (act.getClass().getName().equals(name)) {
                acts.add(act);
            }
        }
        return acts;
    }

    /**
     * 获取最后一个指定类名activity
     *
     * @param name activity class name
     * @return null or last activity
     */
    public Activity getLastActivity(String name) {
        List<Activity> acts = getActivities(name);
        if (acts.isEmpty()) {
            return null;
        }
        return acts.get(acts.size() - 1);
    }

    /**
     * 获取第一个指定类名activity
     *
     * @param name activity class name
     * @return null or first activity
     */
    public Activity getFirstActivity(String name) {
        List<Activity> acts = getActivities(name);
        if (acts.isEmpty()) {
            return null;
        }
        return acts.get(0);
    }


    /**
     * 结束指定类名的activity
     *
     * @param name activity class name
     */
    public void finishActivity(String name) {
        List<Activity> acts = getActivities(name);
        if (acts.isEmpty()) {
            return;
        }
        for (Activity act : acts) {
            finishActivity(act);
        }
    }

    /**
     * 结束 指定 activity
     *
     * @param activity 入参activity
     */
    public void finishActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }


    /**
     * 入栈activity
     *
     * @param activity
     */
    public void put(Activity activity) {
        if (!activitieQuene.contains(activity)) {
            activitieQuene.add(activity);
        }
    }

    /**
     * 清空
     */
    public ActivityStack clear() {
        activitieQuene.clear();
        return this;
    }

    /**
     * 删除
     *
     * @param activity
     */
    public void remove(Activity activity) {
        activitieQuene.remove(activity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (autoManage) {
            put(activity);
        }

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (autoManage) {
            remove(activity);
        }
    }
}
