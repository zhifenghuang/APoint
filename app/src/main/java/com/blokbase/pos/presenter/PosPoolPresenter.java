package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.PosPoolContract;
import com.common.lib.bean.PledgeDataBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PosPoolPresenter extends BasePresenter<PosPoolContract.View> implements PosPoolContract.Presenter {

    public PosPoolPresenter(@NotNull PosPoolContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getPledgeData() {
        HttpMethods.Companion.getInstance().pledgeData(new HttpObserver(false, getRootView(), new HttpListener<PledgeDataBean>() {
            @Override
            public void onSuccess(@Nullable PledgeDataBean bean) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getPledgeDataSuccess(bean);
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
    public void pledge(String amount, String psw) {
        HttpMethods.Companion.getInstance().pledge(amount, psw,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().pledgeSuccess();
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
                        getRootView().showErrorDialog(-1, "");
                    }
                }, getCompositeDisposable()));
    }

    @Override
    public void cancelPledge(int freeze, String amount, String psw) {
        HttpMethods.Companion.getInstance().cancelPledge(freeze, amount, psw, new HttpObserver(getRootView(), new HttpListener<Object>() {
            @Override
            public void onSuccess(@Nullable Object bean) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().cancelPledgeSuccess();
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
                getRootView().showErrorDialog(-1, "");
            }
        }, getCompositeDisposable()));
    }
}
