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
    public void register1(String loginAccount, String refereeId, String code, String key) {
        HttpMethods.Companion.getInstance().register1(loginAccount, refereeId, code, key,
                new HttpObserver(getRootView(), new HttpListener<UserBean>() {
                    @Override
                    public void onSuccess(@Nullable UserBean bean) {
                        if (getRootView() == null || bean == null) {
                            return;
                        }
                        getRootView().registerSuccess(bean);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getCaptcha();
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
