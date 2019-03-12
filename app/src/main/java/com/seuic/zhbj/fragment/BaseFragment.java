package com.seuic.zhbj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by someone on 2017/4/23.
 */

public abstract class BaseFragment extends Fragment {

    public FragmentActivity mActivity;

    /**
     * fragment创建
     * @param savedInstanceState
     */
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity(); // 获取当前fragment锁依赖的activity

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    /**
     * fragment所依赖的activity执行结束
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化数据
        initData();
    }

    /**
     * 初始化布局，必须由子类去实现
     * @return
     */
    public abstract View initView();

    public abstract void initData();
}
