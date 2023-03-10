package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.WalletDetailContract;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.TransferBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class WalletDetailPresenter extends BasePresenter<WalletDetailContract.View> implements WalletDetailContract.Presenter {

    public WalletDetailPresenter(@NotNull WalletDetailContract.View rootView) {
        super(rootView);
    }


    @Override
    public void assetsList() {
        HttpMethods.Companion.getInstance().assetsList(new HttpObserver(false, getRootView(), new HttpListener<ArrayList<AssetsBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<AssetsBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getAssetsListSuccess(list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().showErrorDialog(code, msg);
            }

            @Override
            public void connectError(@Nullable Throwable e) {
            }
        }, getCompositeDisposable()));
    }

    @Override
    public void transferList(String symbol, int pageIndex, ArrayList<Integer> type) {
        HttpMethods.Companion.getInstance().transferList(symbol, pageIndex, type, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<TransferBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<TransferBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getTransferListSuccess(pageIndex, list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
            }
        }, getCompositeDisposable()));
    }
}
