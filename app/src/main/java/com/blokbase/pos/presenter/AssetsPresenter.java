package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.AssetsContract;
import com.common.lib.bean.InviteBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssetsPresenter extends BasePresenter<AssetsContract.View> implements AssetsContract.Presenter {

    public AssetsPresenter(@NotNull AssetsContract.View rootView) {
        super(rootView);
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
