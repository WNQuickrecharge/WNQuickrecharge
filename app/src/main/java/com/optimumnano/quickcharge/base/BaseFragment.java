package com.optimumnano.quickcharge.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.optimumnano.quickcharge.fragment.LazyFrag;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ds on 2017/1/17.
 */
public abstract class BaseFragment extends LazyFrag {


    @Subscribe
    public void onData(Object object){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
