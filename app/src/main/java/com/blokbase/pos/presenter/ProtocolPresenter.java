package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.ProtocolContract;
import com.common.lib.bean.QuestionBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProtocolPresenter extends BasePresenter<ProtocolContract.View> implements ProtocolContract.Presenter {

    public ProtocolPresenter(@NotNull ProtocolContract.View rootView) {
        super(rootView);
    }

    @Override
    public void protocol() {
        HttpMethods.Companion.getInstance().appProtocol(new HttpObserver(false, getRootView(), new HttpListener<QuestionBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable QuestionBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveProtocol(bean);
                getRootView().getProtocolSuccess(bean);
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
