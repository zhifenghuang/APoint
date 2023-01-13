package com.blokbase.pos.presenter;

import android.text.TextUtils;

import com.blokbase.pos.BuildConfig;
import com.blokbase.pos.contract.MainContract;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.QuotationsBean;
import com.common.lib.bean.TickerBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.common.lib.network.OkHttpManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    public MainPresenter(@NotNull MainContract.View rootView) {
        super(rootView);
    }

    @Override
    public void assetsList() {
        HttpMethods.Companion.getInstance().assetsList(new HttpObserver(false, getRootView(), new HttpListener<ArrayList<AssetsBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<AssetsBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getAssetsListSuccess(list);
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

            }
        }, getCompositeDisposable()));
    }

    @Override
    public void checkVersion() {
        HttpMethods.Companion.getInstance().checkVersion(BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, new HttpObserver(false, getRootView(), new HttpListener<VersionBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable VersionBean bean) {
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
