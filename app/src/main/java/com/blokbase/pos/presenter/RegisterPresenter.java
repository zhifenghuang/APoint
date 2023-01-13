package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.RegisterContract;
import com.common.lib.bean.PicCodeBean;
import com.common.lib.bean.UserBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    public RegisterPresenter(@NotNull RegisterContract.View rootView) {
        super(rootView);
    }


    @Override
    public void sendEmail(String email, String type) {
        HttpMethods.Companion.getInstance().sendEmail(email, type,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        getRootView().sendEmailSuccess();
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().sendEmailFailed();
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().sendEmailFailed();
                    }
                }, getCompositeDisposable()));
    }

    @Override
    public void register(String loginAccount, String loginPassword, String refereeId, String verifyCode) {
        HttpMethods.Companion.getInstance().register(loginAccount, loginPassword, refereeId, verifyCode,
                new HttpObserver(getRootView(), new HttpListener<UserBean>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable UserBean bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        getRootView().registerSuccess();
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
    public void resetLoginPsw(String loginAccount, String loginPassword,String reLoginPassword, String verifyCode) {
        HttpMethods.Companion.getInstance().restLoginPsw(loginAccount, loginPassword, reLoginPassword, verifyCode,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        getRootView().registerSuccess();
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
