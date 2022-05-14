package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.PoolNodeRankAdapter;
import com.blokbase.pos.contract.PoolNodeRankContract;
import com.blokbase.pos.fragment.PoolNodeRankFragment;
import com.blokbase.pos.presenter.PoolNodeRankPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.PoolNodeRankBean;
import com.common.lib.constant.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class PoolNodeRankActivity extends BaseActivity<PoolNodeRankContract.Presenter>
        implements PoolNodeRankContract.View, OnRefreshLoadmoreListener {

    private PoolNodeRankAdapter mAdapter;
    private int mPageIndex = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pool_node;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_node_pool_rank);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.setEnableLoadmore(false);
        layout.autoRefresh();
        setViewGone(R.id.tvNoContent);
    }

    private PoolNodeRankAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new PoolNodeRankAdapter(this);
        }
        return mAdapter;
    }


    @NonNull
    @Override
    protected PoolNodeRankContract.Presenter onCreatePresenter() {
        return new PoolNodeRankPresenter(this);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().poolNodeRank(mPageIndex + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().poolNodeRank(1 );
    }

    private void finishLoad() {
        if (isFinish()) {
            return;
        }
        if (getAdapter().getItemCount() == 0) {
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewGone(R.id.tvNoContent);
        }
        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
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
        if (isFinish()) {
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
        if (isFinish()) {
            return;
        }
        finishLoad();
    }
}
