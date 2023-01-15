package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.CheckInActivity;
import com.blokbase.pos.activity.GoodsDetailActivity;
import com.blokbase.pos.activity.MoreGoodsActivity;
import com.blokbase.pos.activity.NoticeListActivity;
import com.blokbase.pos.adapter.GoodsAdapter;
import com.blokbase.pos.adapter.GoodsBannerViewHolder;
import com.blokbase.pos.contract.HomeContract;
import com.blokbase.pos.presenter.HomePresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.bean.GoodsBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.view.banner.BannerView;
import com.common.lib.view.banner.HolderCreator;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {

    private GoodsAdapter mAdapter;

    //10  套餐商品 0普通兑换商品
    @NonNull
    @Override
    protected HomeContract.Presenter onCreatePresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.rlTop);
        setViewsOnClickListener(R.id.ivMsg, R.id.tvSearch,
                R.id.tvMarkArea, R.id.tvPackageArea, R.id.tvCommonArea,
                R.id.tvZeroBuyArea, R.id.tvNftArea,
                R.id.tvPackageMore, R.id.tvCommonMore);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());

        getPresenter().goodsList(1, 0, false);
        getPresenter().goodsList(1, 10, false);
        showGoodsBanners(DataManager.Companion.getInstance().getPackageGoodsList());
        getAdapter().setNewInstance(DataManager.Companion.getInstance().getGoodsList());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSearch:
                break;
            case R.id.ivMsg:
                openActivity(NoticeListActivity.class);
                break;
            case R.id.tvMarkArea:
                openActivity(CheckInActivity.class);
                break;
            case R.id.tvPackageArea:
            case R.id.tvPackageMore:
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_EXTRA, 10);
                openActivity(MoreGoodsActivity.class, bundle);
                break;
            case R.id.tvCommonArea:
            case R.id.tvCommonMore:
                bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_EXTRA, 0);
                openActivity(MoreGoodsActivity.class, bundle);
                break;
        }
    }

    public void onRefresh() {
        if (getView() == null) {
            return;
        }
        getPresenter().goodsList(1, 0, false);
        getPresenter().goodsList(1, 10, false);
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

    private void showGoodsBanners(ArrayList<GoodsBean> list) {
        if (getView() == null) {
            return;
        }
        if (list == null || list.isEmpty()) {
            return;
        }
        BannerView bannerView = getView().findViewById(R.id.goodsBanner);
        bannerView.setIndicatorVisible(true);
        bannerView.setPages(list, new HolderCreator<GoodsBannerViewHolder>() {
            @Override
            public GoodsBannerViewHolder createViewHolder() {
                return new GoodsBannerViewHolder();
            }
        });
        bannerView.start();
    }

    @Override
    public void getGoodsListSuccess(int page, int goodsType, ArrayList<GoodsBean> list) {
        if (getView() == null) {
            return;
        }
        if (goodsType == 0) {
            getAdapter().setNewInstance(list);
        } else {
            showGoodsBanners(list);
        }
    }

    @Override
    public void getGoodsListFailed() {

    }
}
