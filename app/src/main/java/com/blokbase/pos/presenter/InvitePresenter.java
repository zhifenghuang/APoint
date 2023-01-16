package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.InviteContract;
import com.common.lib.bean.InviteBean;
import com.common.lib.bean.PosterBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class InvitePresenter extends BasePresenter<InviteContract.View> implements InviteContract.Presenter {

    public InvitePresenter(@NotNull InviteContract.View rootView) {
        super(rootView);
    }

    @Override
    public void poster() {
        HttpMethods.Companion.getInstance().poster(new HttpObserver(false, getRootView(), new HttpListener<PosterBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable PosterBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().getPosterSuccess(bean);
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
