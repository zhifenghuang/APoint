package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.ContractServiceContract;
import com.common.lib.bean.ContractUsBean;
import com.common.lib.bean.NoticeBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContractServicePresenter extends BasePresenter<ContractServiceContract.View> implements ContractServiceContract.Presenter {

    public ContractServicePresenter(@NotNull ContractServiceContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getContract() {
        HttpMethods.Companion.getInstance().contactUs(new HttpObserver(false, getRootView(), new HttpListener<NoticeBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable NoticeBean bean) {
                if (getRootView() == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveContractUsBean(bean);
                getRootView().getContractSuccess(bean);
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
