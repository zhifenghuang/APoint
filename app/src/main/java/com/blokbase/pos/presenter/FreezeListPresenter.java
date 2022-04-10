package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.FreezeListContract;
import com.common.lib.bean.FreezeBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FreezeListPresenter extends BasePresenter<FreezeListContract.View> implements FreezeListContract.Presenter {

    public FreezeListPresenter(@NotNull FreezeListContract.View rootView) {
        super(rootView);
    }


    @Override
    public void freezeList(final int page) {
        HttpMethods.Companion.getInstance().freezeList(page, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<FreezeBean>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<FreezeBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getFreezeListSuccess(page, list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getFreezeListFailed();
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getFreezeListFailed();
            }
        }, getCompositeDisposable()));
    }

    @Override
    public void unlock(final FreezeBean bean, String payPassword) {
        HttpMethods.Companion.getInstance().unlock(bean.getId(), payPassword, new HttpObserver(getRootView(), new HttpListener<Object>() {
            @Override
            public void onSuccess(@Nullable Object o) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().unlockSuccess(bean);
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
