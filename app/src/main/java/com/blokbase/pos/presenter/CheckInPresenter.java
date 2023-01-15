package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.CheckInContract;
import com.common.lib.bean.CheckInBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CheckInPresenter extends BasePresenter<CheckInContract.View> implements CheckInContract.Presenter {

    public CheckInPresenter(@NotNull CheckInContract.View rootView) {
        super(rootView);
    }

    @Override
    public void checkInOverview() {
        HttpMethods.Companion.getInstance().checkInOverview(new HttpObserver(false, getRootView(), new HttpListener<CheckInBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable CheckInBean bean) {
                if (getRootView() == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveCheckInBean(bean);
                getRootView().checkInOverviewSuccess(bean);
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
    public void checkInSubmit() {
        HttpMethods.Companion.getInstance().checkInSubmit(new HttpObserver(getRootView(), new HttpListener<Object>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable Object bean) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().checkInSubmitSuccess();
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
