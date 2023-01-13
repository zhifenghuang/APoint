package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.IncomeAdapter;
import com.blokbase.pos.contract.IncomeRecordContract;
import com.blokbase.pos.presenter.IncomeRecordPresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.IncomeBean;
import com.common.lib.bean.InviteBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class IncomeRecordActivity extends BaseActivity<IncomeRecordContract.Presenter> implements IncomeRecordContract.View, OnRefreshLoadmoreListener {

    private IncomeAdapter mAdapter;
    private int mPageNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_income_record;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_income_record);
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

    private IncomeAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new IncomeAdapter(this);
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected IncomeRecordContract.Presenter onCreatePresenter() {
        return new IncomeRecordPresenter(this);
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public void getIncomeRecordSuccess(int pageIndex, ArrayList<IncomeBean> list) {
        if (isFinish()) {
            return;
        }
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
    public void getIncomeRecordFailed() {
        finishLoad();
    }

    @Override
    public void getIncomeOverviewSuccess(InviteBean bean) {
        if (isFinish()) {
            return;
        }
        setText(R.id.tvTotal, Utils.removeZero(bean.getTotalAmount()));
        setText(R.id.tvRefereeIncome, Utils.removeZero(bean.getRefereeAmount()));
        setText(R.id.tvShareIncome, Utils.removeZero(bean.getSharingAmount()));
        setText(R.id.tvProxyIncome, Utils.removeZero(bean.getAgentAmount()));
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().incomeRecord(mPageNo + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().incomeOverview();
        getPresenter().incomeRecord(1);
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
