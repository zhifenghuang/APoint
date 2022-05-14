package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.PoolNodeRankAdapter;
import com.blokbase.pos.contract.PoolNodeRankContract;
import com.blokbase.pos.presenter.PoolNodeRankPresenter;
import com.common.lib.bean.PoolNodeRankBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class PoolNodeRankFragment extends BaseFragment<PoolNodeRankContract.Presenter>
        implements PoolNodeRankContract.View, OnRefreshLoadmoreListener {

    private PoolNodeRankAdapter mAdapter;
    private int mPageIndex = 0;
    private int mGradeId;

    @NonNull
    @Override
    protected PoolNodeRankContract.Presenter onCreatePresenter() {
        return new PoolNodeRankPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pool_node;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mGradeId = getArguments().getInt(Constants.BUNDLE_EXTRA);
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

    private PoolNodeRankAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new PoolNodeRankAdapter(getActivity());
        }
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().poolNodeRank(mPageIndex + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().poolNodeRank(1);
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

    public static PoolNodeRankFragment getInstance(int gradeId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_EXTRA, gradeId);
        PoolNodeRankFragment fragment = new PoolNodeRankFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void getPoolNodeRankSuccess(int pageIndex, ArrayList<PoolNodeRankBean> list) {
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
    public void getPoolNodeRankFailed() {
        if (getView() == null) {
            return;
        }
        finishLoad();
    }

}
