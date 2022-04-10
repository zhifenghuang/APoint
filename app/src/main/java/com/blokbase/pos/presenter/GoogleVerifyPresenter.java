package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.GoogleVerifyContract;
import com.common.lib.bean.BannerBean;
import com.common.lib.bean.GoogleInfoBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoogleVerifyPresenter extends BasePresenter<GoogleVerifyContract.View> implements GoogleVerifyContract.Presenter {

    public GoogleVerifyPresenter(@NotNull GoogleVerifyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getGoogleCode() {
        HttpMethods.Companion.getInstance().getGoogleCode(new HttpObserver(false, getRootView(), new HttpListener<GoogleInfoBean>() {
            @Override
            public void onSuccess(@Nullable GoogleInfoBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().getGoogleCodeSuccess(bean);
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

    @Override
    public void verifyGoogle(String code) {
        HttpMethods.Companion.getInstance().verifyGoogleCode(code, new HttpObserver(getRootView(), new HttpListener<Boolean>() {
            @Override
            public void onSuccess(@Nullable Boolean isSuccess) {
                if (isSuccess == null) {
                    isSuccess = false;
                }
                if (getRootView() == null) {
                    return;
                }
                getRootView().verifyGoogle(isSuccess);
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
