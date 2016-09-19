package com.syhd.myweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syhd.myweather.App;

import butterknife.ButterKnife;


/**
 * fragment基类
 * Created by XRB on 2015/12/9.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 进度条
     */
    public Context context;
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = App.application;

    }

    //填充UI
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = initView(inflater,container,savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    //填充数据
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);

    }


    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void initData(Bundle savedInstanceState);
}
