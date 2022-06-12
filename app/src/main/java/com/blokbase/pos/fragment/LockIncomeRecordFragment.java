package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.LockIncomeRecordAdapter;
import com.blokbase.pos.contract.LockIncomeContract;
import com.blokbase.pos.presenter.LockIncomePresenter;
import com.common.lib.bean.ObserverPermissionRecordBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class LockIncomeRecordFragment extends BaseFragment<LockIncomeContract.Presenter>
        implements LockIncomeContract.View, OnRefreshLoadmoreListener {

    private LockIncomeRecordAdapter mAdapter;
    private int mPageIndex = 0;
    private String mType;
    private int mSubType;

    @NonNull
    @Override
    protected LockIncomeContract.Presenter onCreatePresenter() {
        return new LockIncomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lock_income_record;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mType = getArguments().getString(Constants.BUNDLE_EXTRA);
        mSubType = getArguments().getInt(Constants.BUNDLE_EXTRA_2);
        if (mType.equals("account")) {
            setText(R.id.tv, R.string.app_account);
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
    }

    private LockIncomeRecordAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new LockIncomeRecordAdapter(getActivity(), mType);
        }
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        //     getPresenter().getLockIncomeRecords(mPageIndex + 1, mType, mSubType);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        //getPresenter().getLockIncomeRecords(1, mType, mSubType);
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

    public static LockIncomeRecordFragment getInstance(String type, int subType) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_EXTRA, type);
        bundle.putInt(Constants.BUNDLE_EXTRA_2, subType);
        LockIncomeRecordFragment fragment = new LockIncomeRecordFragment();
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
    public void getRecordsFailed(int code, String msg) {
        if (getView() == null) {
            return;
        }
        finishLoad();
    }
}
