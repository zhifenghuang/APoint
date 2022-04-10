package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.CalculatorContract;
import com.common.lib.bean.MetaBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CalculatorPresenter extends BasePresenter<CalculatorContract.View> implements CalculatorContract.Presenter {

    public CalculatorPresenter(@NotNull CalculatorContract.View rootView) {
        super(rootView);
    }

    @Override
    public void appMeta() {
        HttpMethods.Companion.getInstance().appMeta(new HttpObserver(false, getRootView(), new HttpListener<MetaBean>() {
            @Override
            public void onSuccess(@Nullable MetaBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveAppMeta(bean);
                getRootView().getMetaSuccess(bean);
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
