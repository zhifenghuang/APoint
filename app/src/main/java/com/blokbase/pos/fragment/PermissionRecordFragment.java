package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.ObserverPermissionRecordAdapter;
import com.blokbase.pos.contract.ObserverPermissionContract;
import com.blokbase.pos.presenter.ObserverPermissionPresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.bean.ObserverPermissionRecordBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class PermissionRecordFragment extends BaseFragment<ObserverPermissionContract.Presenter>
        implements ObserverPermissionContract.View, OnRefreshLoadmoreListener {

    private ObserverPermissionRecordAdapter mAdapter;
    private int mPageIndex = 0;
    private String mType;
    private String mObserverId;

    @NonNull
    @Override
    protected ObserverPermissionContract.Presenter onCreatePresenter() {
        return new ObserverPermissionPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_permission_record;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mType = getArguments().getString(Constants.BUNDLE_EXTRA);
        mObserverId = getArguments().getString(Constants.BUNDLE_EXTRA_2);
        if (mType.equals("account")) {
            setText(R.id.tv, R.string.app_account);
            setText(R.id.tv1, R.string.app_total_account_income);
        }
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        SmartRefreshLayout layout = view.findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.setEnableLoadmore(false);
        layout.autoRefresh();
        setViewGone(R.id.tvNoContent);
        getPresenter().getData(mType, mObserverId);
    }

    private ObserverPermissionRecordAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new ObserverPermissionRecordAdapter(getActivity(), mType);
        }
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().getRecords(mPageIndex + 1, mType, mObserverId);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().getRecords(1, mType, mObserverId);
    }

    private void finishLoad() {
        if (getView() == null) {
            return;
        }
        if (getAdapter().getItemCount() == 0) {
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewGone(R.id.tvNoContent);
        }
        SmartRefreshLayout layout = getView().findViewById(R.id.smartRefreshLayout);
        layout.finishLoadmore();
        layout.finishRefresh();
        layout.setEnableLoadmore(getAdapter().getItemCount() != 0
                && getAdapter().getItemCount() % 20 == 0);
    }

    public static PermissionRecordFragment getInstance(String type, String observerId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_EXTRA, type);
        bundle.putString(Constants.BUNDLE_EXTRA_2, observerId);
        PermissionRecordFragment fragment = new PermissionRecordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void getRecordsSuccess(int pageIndex, ArrayList<ObserverPermissionRecordBean> list) {
        if (getView() == null) {
            return;
        }
        mPageIndex = pageIndex;
        if (mPageIndex == 1) {
            getAdapter().setNewInstance(list);
        } else {
            getAdapter().addData(list);
        }
        finishLoad();
    }

    @Override
    public void getDataSuccess(ObserverPermissionRecordBean bean) {
        if (getView() == null) {
            return;
        }
        setText(R.id.tvNum, Utils.removeZero(bean.getTotalAmount()));
    }

    @Override
    public void getRecordsFailed(int code, String msg) {
        if (getView() == null) {
            return;
        }
        finishLoad();
    }
}
