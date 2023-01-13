package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.AssetsRecordDetailActivity;
import com.blokbase.pos.activity.GoodsDetailActivity;
import com.blokbase.pos.activity.MainActivity;
import com.blokbase.pos.activity.PackageGoodsActivity;
import com.blokbase.pos.adapter.GoodsAdapter;
import com.blokbase.pos.adapter.MarkedAdapter;
import com.blokbase.pos.adapter.TransferAdapter;
import com.blokbase.pos.contract.GoodsContract;
import com.blokbase.pos.presenter.GoodsPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.bean.CheckDayBean;
import com.common.lib.bean.CheckInBean;
import com.common.lib.bean.GoodsBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.manager.MediaplayerManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class GoodsFragment extends BaseFragment<GoodsContract.Presenter> implements GoodsContract.View, OnRefreshLoadmoreListener {

    private GoodsAdapter mPackageAdapter, mSwapAdapter;
    private MarkedAdapter mAdapter;
    private int mPage;

    //10  套餐商品 0普通兑换商品
    @NonNull
    @Override
    protected GoodsContract.Presenter onCreatePresenter() {
        return new GoodsPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.tvMore, R.id.tvMarked, R.id.tvToSwap);
        mPage = 1;

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        getAdapter().addData(new CheckDayBean(1, "0.05"));
        getAdapter().addData(new CheckDayBean(2, "0.06"));
        getAdapter().addData(new CheckDayBean(3, "0.07"));
        getAdapter().addData(new CheckDayBean(4, "0.08"));
        getAdapter().addData(new CheckDayBean(5, "0.09"));
        getAdapter().addData(new CheckDayBean(6, "0.1"));
        getAdapter().addData(new CheckDayBean(7, "0.1"));

        recyclerView = view.findViewById(R.id.recyclerView1);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        getPackageAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getPackageAdapter());


        recyclerView = view.findViewById(R.id.recyclerView2);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        getSwapAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getSwapAdapter());

        SmartRefreshLayout layout = view.findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.setEnableLoadmore(false);
        getPresenter().checkInOverview();
        getPresenter().goodsList(mPage, 0, false);
        getPresenter().goodsList(mPage, 10, false);
        getPackageAdapter().setNewInstance(DataManager.Companion.getInstance().getPackageGoodsList());
        getSwapAdapter().setNewInstance(DataManager.Companion.getInstance().getGoodsList());

        checkInOverviewSuccess(DataManager.Companion.getInstance().getCheckInBean());

        MediaplayerManager.getInstance().loadSound(getActivity(), R.raw.gold_coin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMore:
                openActivity(PackageGoodsActivity.class);
                break;
            case R.id.tvToSwap:
                ((MainActivity) getActivity()).toSwapGoods();
                break;
            case R.id.tvMarked:
                getPresenter().checkInSubmit();
                break;
        }
    }

    public void onRefresh() {
        if (getView() == null) {
            return;
        }
        getPresenter().goodsList(mPage, 0, false);
        getPresenter().goodsList(mPage, 10, false);
    }

    private MarkedAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new MarkedAdapter(getActivity());
        }
        return mAdapter;
    }

    private GoodsAdapter getSwapAdapter() {
        if (mSwapAdapter == null) {
            mSwapAdapter = new GoodsAdapter(getActivity());
            mSwapAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, mSwapAdapter.getItem(position));
                    openActivity(GoodsDetailActivity.class, bundle);
                }
            });
        }
        return mSwapAdapter;
    }

    private GoodsAdapter getPackageAdapter() {
        if (mPackageAdapter == null) {
            mPackageAdapter = new GoodsAdapter(getActivity());
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
        getPresenter().goodsList(mPage + 1, 0, false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().checkInOverview();
        getPresenter().goodsList(1, 0, false);
        getPresenter().goodsList(1, 10, false);
    }

    @Override
    public void getGoodsListSuccess(int page, int goodsType, ArrayList<GoodsBean> list) {
        if (getView() == null) {
            return;
        }
        mPage = page;
        if (goodsType == 0) {
            if (page == 1) {
                getSwapAdapter().setNewInstance(list);
            } else {
                getSwapAdapter().addData(list);
            }
            finishLoad();
        } else {
            getPackageAdapter().setNewInstance(list);
        }
    }

    @Override
    public void getGoodsListFailed() {
        if (getView() == null) {
            return;
        }
        finishLoad();
    }

    @Override
    public void checkInOverviewSuccess(CheckInBean bean) {
        if (getView() == null || bean == null) {
            return;
        }
        getAdapter().setCheckedCount(bean.getCount());
        TextView tvMarked = getView().findViewById(R.id.tvMarked);
        if (bean.getCheckIn()) {
            tvMarked.setBackgroundResource(R.drawable.shape_cdcdcd_22);
            tvMarked.setEnabled(false);
        } else {
            tvMarked.setBackgroundResource(R.drawable.shape_00a0e9_22);
            tvMarked.setEnabled(true);
        }
    }

    @Override
    public void checkInSubmitSuccess() {
        if (getView() == null) {
            return;
        }
        showToast(R.string.app_mark_success);
        MediaplayerManager.getInstance().playSound(getContext(), R.raw.gold_coin);
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaplayerManager.getInstance().releaseSoundPool();
            }
        },1100);
        CheckInBean bean = DataManager.Companion.getInstance().getCheckInBean();
        if (bean == null) {
            getPresenter().checkInOverview();
        } else {
            bean.setCount(bean.getCount() + 1);
            bean.setCheckIn(true);
            getAdapter().setCheckedCount(bean.getCount());
            getAdapter().notifyDataSetChanged();
            TextView tvMarked = getView().findViewById(R.id.tvMarked);
            if (bean.getCheckIn()) {
                tvMarked.setBackgroundResource(R.drawable.shape_cdcdcd_22);
                tvMarked.setEnabled(false);
            } else {
                tvMarked.setBackgroundResource(R.drawable.shape_00a0e9_22);
                tvMarked.setEnabled(true);
            }
            DataManager.Companion.getInstance().saveCheckInBean(bean);
        }
    }

    private void finishLoad() {
        if (getView() == null) {
            return;
        }
        if (getSwapAdapter().getItemCount() == 0) {
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewGone(R.id.tvNoContent);
        }
        SmartRefreshLayout layout = getView().findViewById(R.id.smartRefreshLayout);
        layout.finishLoadmore();
        layout.finishRefresh();
        layout.setEnableLoadmore(getSwapAdapter().getItemCount() != 0
                && getSwapAdapter().getItemCount() % 20 == 0);
    }

    public void onDestroyView() {
        super.onDestroyView();
        MediaplayerManager.getInstance().releaseSoundPool();
    }
}
