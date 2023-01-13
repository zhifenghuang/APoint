package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.TransferListContract;
import com.common.lib.bean.TransferBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TransferListPresenter extends BasePresenter<TransferListContract.View> implements TransferListContract.Presenter {

    public TransferListPresenter(@NotNull TransferListContract.View rootView) {
        super(rootView);
    }


    @Override
    public void transferList(String symbol, int pageIndex) {
        HttpMethods.Companion.getInstance().transferList(symbol, pageIndex, new ArrayList<>(), new HttpObserver(false, getRootView(), new HttpListener<ArrayList<TransferBean>>() {
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
                getRootView().getTransferListFailed();
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getTransferListFailed();
            }
        }, getCompositeDisposable()));
    }
}
