package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.WalletTransferContract;
import com.common.lib.bean.TransferFeeBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WalletTransferPresenter extends BasePresenter<WalletTransferContract.View> implements WalletTransferContract.Presenter {

    public WalletTransferPresenter(@NotNull WalletTransferContract.View rootView) {
        super(rootView);
    }

    @Override
    public void transferInfo(final String symbol) {
        HttpMethods.Companion.getInstance().transferInfo(symbol, new HttpObserver(false, getRootView(), new HttpListener<TransferFeeBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable TransferFeeBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().getTransferInfoSuccess(bean);
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
    public void transfer(String payPassword, String symbol, String amount, String address) {
        HttpMethods.Companion.getInstance().transfer(payPassword, symbol, amount, address, new HttpObserver(getRootView(), new HttpListener<Object>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable Object bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().transferSuccess();
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
                if (getRootView() == null) {
                    return;
                }
                getRootView().transferFailed();
            }
        }, getCompositeDisposable()));
    }
}
