package com.optimumnano.quickcharge.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.optimumnano.quickcharge.fragment.LazyFrag;
import com.optimumnano.quickcharge.http.TaskDispatcher;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ds on 2017/1/17.
 */
public abstract class BaseFragment extends LazyFrag {
    protected Context mContext;
    protected TaskDispatcher mTaskDispatcher;

    @Subscribe
    public void onData(Object object) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public boolean deAlive() {
        if (isAdded() && !isDetached()) {
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
        mTaskDispatcher = TaskDispatcher.getInstance(mContext);
    }
}
