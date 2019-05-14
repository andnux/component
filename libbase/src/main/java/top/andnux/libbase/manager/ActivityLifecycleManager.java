package top.andnux.libbase.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.*;

public class ActivityLifecycleManager implements Application.ActivityLifecycleCallbacks {

    private static final ActivityLifecycleManager ourInstance = new ActivityLifecycleManager();

    public static ActivityLifecycleManager getInstance() {
        return ourInstance;
    }

    private List<WeakReference<Activity>> mActivityList;

    private ActivityLifecycleManager() {
        this.mActivityList = new Vector<>();
    }

    public Activity getCurrentActivity() {
        return mActivityList.get(mActivityList.size() - 1).get();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityList.add(new WeakReference<>(activity));
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        for (WeakReference<Activity> activityWeakReference : mActivityList) {
            if (activityWeakReference.get().equals(activity)) {
                mActivityList.remove(activityWeakReference);
            }
        }
    }
}
