package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.TransferAdapter;
import com.blokbase.pos.contract.TransferListContract;
import com.blokbase.pos.presenter.TransferListPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.TransferBean;
import com.common.lib.constant.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class WalletRecordActivity extends BaseActivity<TransferListContract.Presenter>
        implements TransferListContract.View, OnRefreshLoadmoreListener {

    private TransferAdapter mAdapter;
    private String mSymbol;
    private int mPageIndex = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_record;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_record);
        mSymbol = getIntent().getExtras().getString(Constants.BUNDLE_EXTRA);
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

    private TransferAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new TransferAdapter(this);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, mAdapter.getItem(position));
                    openActivity(AssetsRecordDetailActivity.class, bundle);
                }
            });
        }
        return mAdapter;
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().transferList(mSymbol, mPageIndex + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().transferList(mSymbol, 1);
    }

    @Override
    public void getTransferListSuccess(int pageIndex, ArrayList<TransferBean> list) {
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
    public void getTransferListFailed() {
        finishLoad();
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

    @NonNull
    @Override
    protected TransferListContract.Presenter onCreatePresenter() {
        return new TransferListPresenter(this);
    }

    @Override
    public void onClick(View v) {
    }
}
