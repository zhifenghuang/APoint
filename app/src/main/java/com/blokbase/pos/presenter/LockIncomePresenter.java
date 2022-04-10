package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.LockIncomeContract;
import com.common.lib.bean.ObserverPermissionRecordBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class LockIncomePresenter extends BasePresenter<LockIncomeContract.View> implements LockIncomeContract.Presenter {

    public LockIncomePresenter(@NotNull LockIncomeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getLockIncomeRecords(int pageIndex, String type, int subType) {

        HttpMethods.Companion.getInstance().fetchIncome(pageIndex, type, subType,
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
}
