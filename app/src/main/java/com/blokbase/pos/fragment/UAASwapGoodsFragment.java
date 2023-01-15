package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.GoodsDetailActivity;
import com.blokbase.pos.adapter.GoodsAdapter;
import com.blokbase.pos.contract.HomeContract;
import com.blokbase.pos.presenter.HomePresenter;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.CheckInBean;
import com.common.lib.bean.GoodsBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class UAASwapGoodsFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View, OnRefreshLoadmoreListener {

    private GoodsAdapter mAdapter;
    private int mPage;

    //20  兑换物
    @NonNull
    @Override
    protected HomeContract.Presenter onCreatePresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_uaa_swap_goods;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPage = 1;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());

        SmartRefreshLayout layout = view.findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.setEnableLoadmore(false);
        getPresenter().goodsList(mPage, 20, false);
        getAdapter().setNewInstance(DataManager.Companion.getInstance().getSwapGoodsList());
        showUAABalance();
    }

    @Override
    public void onClick(View v) {
    }

    public void onRefresh() {
        if (getView() == null) {
            return;
        }
        getPresenter().goodsList(mPage, 20, false);
        showUAABalance();
    }

    private void showUAABalance() {
        ArrayList<AssetsBean> list = DataManager.Companion.getInstance().getAssets();
        if (getView() == null || list.isEmpty()) {
            return;
        }
        for (AssetsBean bean : list) {
            String symbol = bean.getSymbol();
            if (symbol.equalsIgnoreCase("uaa")) {
                setText(R.id.tvUAABalance, Utils.removeZero(bean.getBalance()));
                break;
            }
        }
    }

    private GoodsAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new GoodsAdapter(getActivity());
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, mAdapter.getItem(position));
                    openActivity(GoodsDetailActivity.class, bundle);
                }
            });
        }
        return mAdapter;
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().goodsList(mPage + 1, 20, false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().goodsList(1, 20, false);
    }

    @Override
    public void getGoodsListSuccess(int page, int goodsType, ArrayList<GoodsBean> list) {
        if (getView() == null) {
            return;
        }
        mPage = page;
        if (page == 1) {
            getAdapter().setNewInstance(list);
        } else {
            getAdapter().addData(list);
        }
        finishLoad();
    }

    @Override
    public void getGoodsListFailed() {
        if (getView() == null) {
            return;
        }
        finishLoad();
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
}
