package com.blokbase.pos.presenter;


import com.blokbase.pos.BuildConfig;
import com.blokbase.pos.contract.MineContract;
import com.common.lib.bean.UserBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinePresenter extends BasePresenter<MineContract.View> implements MineContract.Presenter {

    public MinePresenter(@NotNull MineContract.View rootView) {
        super(rootView);
    }


    @Override
    public void checkVersion() {
        HttpMethods.Companion.getInstance().checkVersion(BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, new HttpObserver(getRootView(), new HttpListener<VersionBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable VersionBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().checkVersionSuccess(bean);
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
    public void getUserInfo() {
        HttpMethods.Companion.getInstance().userProfile(new HttpObserver(false, getRootView(), new HttpListener<UserBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable UserBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveMyInfo(bean);
                getRootView().getUserInfoSuccess();
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
