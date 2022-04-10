package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.ObserverPermissionContract;
import com.common.lib.bean.ObserverPermissionRecordBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ObserverPermissionPresenter extends BasePresenter<ObserverPermissionContract.View> implements ObserverPermissionContract.Presenter {

    public ObserverPermissionPresenter(@NotNull ObserverPermissionContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getRecords(int pageIndex, String type, String observerId) {
        HttpMethods.Companion.getInstance().subscribeObserver(pageIndex, type, observerId,
                new HttpObserver(false, getRootView(), new HttpListener<ArrayList<ObserverPermissionRecordBean>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<ObserverPermissionRecordBean> list) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getRecordsSuccess(pageIndex, list);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getRecordsFailed(code, msg);
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getRecordsFailed(-1, "");
                    }
                }, getCompositeDisposable()));
    }

    @Override
    public void getData(String type, String observerId) {
        HttpMethods.Companion.getInstance().observerData(type, observerId,
                new HttpObserver(false, getRootView(), new HttpListener<ObserverPermissionRecordBean>() {
                    @Override
                    public void onSuccess(@Nullable ObserverPermissionRecordBean bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getDataSuccess(bean);
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
                        getRootView().showErrorDialog(-1, "");
                    }
                }, getCompositeDisposable()));
    }
}
