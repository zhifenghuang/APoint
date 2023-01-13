package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.NoticeAdapter;
import com.blokbase.pos.contract.NoticeListContract;
import com.blokbase.pos.presenter.NoticeListPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.NoticeBean;
import com.common.lib.manager.DataManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class NoticeListActivity extends BaseActivity<NoticeListContract.Presenter>
        implements NoticeListContract.View, OnRefreshLoadmoreListener {

    protected int mPageNo;
    protected NoticeAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice_list;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_notification);
        setTopStatusBarStyle(R.id.topView);
        setBackgroundColor(R.id.topView, R.color.color_bg_theme);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.autoRefresh();
        layout.setEnableLoadmore(false);
        getAdapter().setNewInstance(DataManager.Companion.getInstance().getNoticeList());
    }

    protected NoticeAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new NoticeAdapter(this);
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected NoticeListContract.Presenter onCreatePresenter() {
        return new NoticeListPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getNoticeListSuccess(int page, ArrayList<NoticeBean> list) {
        mPageNo = page;
        if (mPageNo == 1) {
            getAdapter().setNewInstance(list);
        } else {
            getAdapter().addData(list);
        }
        finishRefreshLoad();
    }

    @Override
    public void getNoticeListFailed() {
        finishRefreshLoad();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().noticeList(mPageNo + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().noticeList(1);
    }

    protected void finishRefreshLoad() {
        SmartRefreshLayout smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        int size = getAdapter().getItemCount();
        smartRefreshLayout.setEnableLoadmore(size != 0 && size % 20 == 0);
        getAdapter().notifyDataSetChanged();
        if (size == 0) {
            setViewVisible(R.id.tvNoContent);
        }
    }
}
