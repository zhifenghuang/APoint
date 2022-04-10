package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.ObserverLinkContract;
import com.common.lib.bean.ObserverBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ObserverLinkPresenter extends BasePresenter<ObserverLinkContract.View> implements ObserverLinkContract.Presenter {

    public ObserverLinkPresenter(@NotNull ObserverLinkContract.View rootView) {
        super(rootView);
    }

    @Override
    public void deleteObserver(String id) {
        HttpMethods.Companion.getInstance().destroyObserver(id,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().deleteObserverSuccess();
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
    public void updateObserver(String id, String remark, boolean income, boolean account) {
        HttpMethods.Companion.getInstance().updateObserver(id, remark, income, account,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().updateObserverSuccess(income, account);
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
