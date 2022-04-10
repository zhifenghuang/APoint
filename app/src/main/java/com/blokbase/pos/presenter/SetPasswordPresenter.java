package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.SetPasswordContract;
import com.common.lib.bean.UserBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetPasswordPresenter extends BasePresenter<SetPasswordContract.View> implements SetPasswordContract.Presenter {

    public SetPasswordPresenter(@NotNull SetPasswordContract.View rootView) {
        super(rootView);
    }

    @Override
    public void sendEmail(String email, String type) {
        HttpMethods.Companion.getInstance().sendEmail(email, type,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean) {
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
    public void register2(String hash, String loginPassword, String reLoginPassword, String verifyCode) {
        HttpMethods.Companion.getInstance().register2(hash, loginPassword, reLoginPassword, verifyCode,
                new HttpObserver(getRootView(), new HttpListener<UserBean>() {
                    @Override
                    public void onSuccess(@Nullable UserBean bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        getRootView().setPasswordSuccess();
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
    public void resetLoginPsw(String loginAccount, String loginPassword, String reLoginPassword,
                              String verifyCode, String key, String code) {
        HttpMethods.Companion.getInstance().restLoginPsw(loginAccount, loginPassword, reLoginPassword,
                verifyCode, key, code,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().setPasswordSuccess();
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
    public void modifyPsw(String verifyCode, String password, String rePassword, String scene) {
        HttpMethods.Companion.getInstance().changePsw(verifyCode, password, rePassword, scene,
                new HttpObserver(getRootView(), new HttpListener<UserBean>() {
                    @Override
                    public void onSuccess(@Nullable UserBean bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        getRootView().setPasswordSuccess();
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
