package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.ServiceHelpContract;
import com.common.lib.bean.QuestionBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServiceHelpPresenter extends BasePresenter<ServiceHelpContract.View> implements ServiceHelpContract.Presenter {

    public ServiceHelpPresenter(@NotNull ServiceHelpContract.View rootView) {
        super(rootView);
    }

    @Override
    public void serviceHelp() {
        HttpMethods.Companion.getInstance().serviceHelp(new HttpObserver(false, getRootView(), new HttpListener<QuestionBean>() {
            @Override
            public void onSuccess(@Nullable QuestionBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().getServiceHelpSuccess(bean);
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
