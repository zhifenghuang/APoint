package com.blokbase.pos.presenter;

import com.blokbase.pos.BuildConfig;
import com.blokbase.pos.contract.LoginContract;
import com.common.lib.bean.PicCodeBean;
import com.common.lib.bean.UserBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(@NotNull LoginContract.View rootView) {
        super(rootView);
    }


    @Override
    public void getCaptcha() {
        HttpMethods.Companion.getInstance().getCaptcha(new HttpObserver(false, getRootView(), new HttpListener<PicCodeBean>() {
            @Override
            public void onSuccess(@Nullable PicCodeBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().getCaptchaSuccess(bean);
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
    public void login(String loginAccount, String loginPassword, String code, String key) {
        HttpMethods.Companion.getInstance().login(loginAccount, loginPassword, code, key,
                new HttpObserver(getRootView(), new HttpListener<UserBean>() {
                    @Override
                    public void onSuccess(@Nullable UserBean bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        DataManager.Companion.getInstance().saveToken(bean.getToken());
                        DataManager.Companion.getInstance().saveMyInfo(bean);
                        getRootView().loginSuccess();
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().showErrorDialog(code, msg);
                        getCaptcha();
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
    public void checkVersion() {
        HttpMethods.Companion.getInstance().checkVersion(BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, new HttpObserver(false, getRootView(), new HttpListener<VersionBean>() {
            @Override
            public void onSuccess(@Nullable VersionBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().checkVersionSuccess(bean);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
            }

            @Override
            public void connectError(@Nullable Throwable e) {
            }
        }, getCompositeDisposable()));
    }
}
