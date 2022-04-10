package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.AddCollectionContract;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class AddCollectionPresenter extends BasePresenter<AddCollectionContract.View> implements AddCollectionContract.Presenter {

    public AddCollectionPresenter(@NotNull AddCollectionContract.View rootView) {
        super(rootView);
    }

    @Override
    public void addCollection(String type, String remark, String link) {
        HttpMethods.Companion.getInstance().createCollection(type, remark, link,
                new HttpObserver(false, getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        getRootView().addCollectionSuccess();
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
