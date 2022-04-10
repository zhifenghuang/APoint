package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.AddObserverContract;
import com.common.lib.bean.ObserverBean;
import com.common.lib.bean.QuestionBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class AddObserverPresenter extends BasePresenter<AddObserverContract.View> implements AddObserverContract.Presenter {

    public AddObserverPresenter(@NotNull AddObserverContract.View rootView) {
        super(rootView);
    }

    @Override
    public void addObserver(String type, String remark, HashMap<String, Boolean> permission) {
        HttpMethods.Companion.getInstance().createObserver(type, remark, permission,
                new HttpObserver(getRootView(), new HttpListener<ObserverBean>() {
                    @Override
                    public void onSuccess(@Nullable ObserverBean bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        getRootView().addObserverSuccess(bean);
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
