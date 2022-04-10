package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.ObserverContract;
import com.common.lib.bean.ObserverBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class ObserverPresenter extends BasePresenter<ObserverContract.View> implements ObserverContract.Presenter {

    public ObserverPresenter(@NotNull ObserverContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getObservers(int pageIndex, String type) {
        HttpMethods.Companion.getInstance().getObservers(pageIndex, type,
                new HttpObserver(false, getRootView(), new HttpListener<ArrayList<ObserverBean>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<ObserverBean> list) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().addObserverSuccess(pageIndex, list);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().addObserverFailed();
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().addObserverFailed();
                    }
                }, getCompositeDisposable()));
    }

    @Override
    public void deleteObserver(ObserverBean bean) {
        HttpMethods.Companion.getInstance().destroyObserver(bean.getId(),
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object o) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().deleteObserverSuccess(bean);
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
