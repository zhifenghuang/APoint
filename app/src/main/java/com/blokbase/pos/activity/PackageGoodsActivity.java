package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.GoodsAdapter;
import com.blokbase.pos.contract.GoodsContract;
import com.blokbase.pos.presenter.GoodsPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.CheckInBean;
import com.common.lib.bean.GoodsBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class PackageGoodsActivity extends BaseActivity<GoodsContract.Presenter> implements GoodsContract.View, OnRefreshLoadmoreListener {

    private GoodsAdapter mPackageAdapter;
    private int mPage;

    //10  套餐商品 0普通兑换商品
    @NonNull
    @Override
    protected GoodsContract.Presenter onCreatePresenter() {
        return new GoodsPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_package_goods;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_package_area);
        mPage = 1;
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        getPackageAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getPackageAdapter());

        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.setEnableLoadmore(false);
        layout.autoRefresh();
//        getPackageAdapter().setNewInstance(DataManager.Companion.getInstance().getPackageGoodsList());
    }

    @Override
    public void onClick(View v) {
    }


    private GoodsAdapter getPackageAdapter() {
        if (mPackageAdapter == null) {
            mPackageAdapter = new GoodsAdapter(this);
            mPackageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, mPackageAdapter.getItem(position));
                    openActivity(GoodsDetailActivity.class, bundle);
                }
            });
        }
        return mPackageAdapter;
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().goodsList(mPage + 1, 10, true);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().goodsList(1, 10, true);
    }

    @Override
    public void getGoodsListSuccess(int page, int goodsType, ArrayList<GoodsBean> list) {
        if (isFinish()) {
            return;
        }
        mPage = page;
        if (page == 1) {
            getPackageAdapter().setNewInstance(list);
        } else {
            getPackageAdapter().addData(list);
        }
        finishLoad();
    }

    @Override
    public void getGoodsListFailed() {
        if (isFinish()) {
            return;
        }
        finishLoad();
    }

    @Override
    public void checkInOverviewSuccess(CheckInBean bean) {

    }

    @Override
    public void checkInSubmitSuccess() {

    }

    private void finishLoad() {
        if (isFinish()) {
            return;
        }
        if (getPackageAdapter().getItemCount() == 0) {
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewGone(R.id.tvNoContent);
        }
        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
        layout.finishLoadmore();
        layout.finishRefresh();
        layout.setEnableLoadmore(getPackageAdapter().getItemCount() != 0
                && getPackageAdapter().getItemCount() % 20 == 0);
    }


}
