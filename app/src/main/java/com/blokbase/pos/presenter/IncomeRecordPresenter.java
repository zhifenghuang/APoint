package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.IncomeRecordContract;
import com.common.lib.bean.IncomeBean;
import com.common.lib.bean.InviteBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class IncomeRecordPresenter extends BasePresenter<IncomeRecordContract.View> implements IncomeRecordContract.Presenter {

    public IncomeRecordPresenter(@NotNull IncomeRecordContract.View rootView) {
        super(rootView);
    }


    @Override
    public void incomeRecord(int pageIndex) {
        HttpMethods.Companion.getInstance().incomeRecord(pageIndex, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<IncomeBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<IncomeBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getIncomeRecordSuccess(pageIndex, list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getIncomeRecordFailed();
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getIncomeRecordFailed();
            }
        }, getCompositeDisposable()));
    }

    @Override
    public void incomeOverview() {
        HttpMethods.Companion.getInstance().incomeOverview(new HttpObserver(false, getRootView(), new HttpListener<InviteBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable InviteBean bean) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getIncomeOverviewSuccess(bean);
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
