package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.WalletExchangeContract;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.ExchangeTipsBean;
import com.common.lib.bean.QuotationsBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class WalletExchangePresenter extends BasePresenter<WalletExchangeContract.View> implements WalletExchangeContract.Presenter {

    public WalletExchangePresenter(@NotNull WalletExchangeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void ticker() {
        HttpMethods.Companion.getInstance().ticker(new HttpObserver(false, getRootView(), new HttpListener<ArrayList<QuotationsBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<QuotationsBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                for (QuotationsBean bean : list) {
                    if (bean.getSymbol().equalsIgnoreCase("UAA_INTEGRAL")) {
                        getRootView().getTickerSuccess(bean);
                        break;
                    }
                }
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
            }

            @Override
            public void connectError(@Nullable Throwable e) {

            }
        }, getCompositeDisposable()));
    }

    @Override
    public void exchange(String payPassword, String fromSymbol, String toSymbol, String amount) {
        HttpMethods.Companion.getInstance().exchange(payPassword, fromSymbol, toSymbol, amount,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().exchangeSuccess();
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
    public void exchangeInfo(String fromSymbol, String toSymbol) {
        HttpMethods.Companion.getInstance().assetsExchangeInfo(fromSymbol, toSymbol,
                new HttpObserver(false, getRootView(), new HttpListener<ExchangeTipsBean>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable ExchangeTipsBean bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getExchangeInfoSuccess(bean);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {

                    }
                }, getCompositeDisposable()));
    }
}
