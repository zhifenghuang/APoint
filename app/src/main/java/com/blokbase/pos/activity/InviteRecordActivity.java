package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.InviteAdapter;
import com.blokbase.pos.contract.InviteContract;
import com.blokbase.pos.presenter.InvitePresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.InviteBean;
import com.common.lib.bean.PosterBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class InviteRecordActivity extends BaseActivity<InviteContract.Presenter> implements InviteContract.View, OnRefreshLoadmoreListener {

    private InviteAdapter mAdapter;
    private int mPageNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_record;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {

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
    }

    private InviteAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new InviteAdapter(this);
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected InviteContract.Presenter onCreatePresenter() {
        return new InvitePresenter(this);
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public void getPosterSuccess(PosterBean poster) {

    }

    @Override
    public void getInviteDetailSuccess(int pageIndex, int totalCount, ArrayList<InviteBean> list) {
        if (isFinish()) {
            return;
        }
        setText(R.id.tvInviteNum, String.valueOf(totalCount));
        mPageNo = pageIndex;
        if (pageIndex == 1) {
            getAdapter().getData().clear();
            getAdapter().setNewInstance(list);
        } else {
            getAdapter().addData(list);
        }
        finishLoad();
    }

    @Override
    public void getInviteDetailFailed() {
        finishLoad();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().inviteDetail(mPageNo + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().inviteDetail(1);
    }

    private void finishLoad() {
        if (isFinish()) {
            return;
        }
        if (getAdapter().getItemCount() == 0) {
            setViewGone(R.id.recyclerView);
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewVisible(R.id.recyclerView);
            setViewGone(R.id.tvNoContent);
        }
        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
        layout.finishLoadmore();
        layout.finishRefresh();
        layout.setEnableLoadmore(getAdapter().getItemCount() != 0
                && getAdapter().getItemCount() % 20 == 0);
    }
}
