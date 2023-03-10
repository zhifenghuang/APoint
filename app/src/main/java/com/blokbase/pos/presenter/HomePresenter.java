package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.HomeContract;
import com.common.lib.bean.GoodsBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    public HomePresenter(@NotNull HomeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void goodsList(final int page, final int goodsType, boolean isMore) {
        int pageSize = 20;
        if (goodsType == 10 && !isMore) {
            pageSize = 2;
        }
        HttpMethods.Companion.getInstance().goodsList(page, goodsType, pageSize, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<GoodsBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<GoodsBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                if (page == 1) {
                    if (goodsType == 0) {
                        DataManager.Companion.getInstance().saveGoodsList(list);
                    } else if (goodsType == 20) {
                        DataManager.Companion.getInstance().saveSwapGoodsList(list);
                    } else if (!isMore) {
                        DataManager.Companion.getInstance().savePackageGoodsList(list);
                    }
                }
                getRootView().getGoodsListSuccess(page, goodsType, list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getGoodsListFailed();
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getGoodsListFailed();
            }
        }, getCompositeDisposable()));
    }
}
