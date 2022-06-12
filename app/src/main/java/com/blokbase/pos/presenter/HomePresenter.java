package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.HomeContract;
import com.common.lib.bean.BannerBean;
import com.common.lib.bean.HomeDataBean;
import com.common.lib.bean.NoticeBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    public HomePresenter(@NotNull HomeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void bannerList() {
        HttpMethods.Companion.getInstance().bannerList("NORMAL", new HttpObserver(false, getRootView(), new HttpListener<ArrayList<BannerBean>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<BannerBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getBannerListSuccess(list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
            }

            @Override
            public void connectError(@Nullable Throwable e) {
            }
        }, getCompositeDisposable()));
    }

    @Override
    public void noticeList() {
        HttpMethods.Companion.getInstance().noticeList(1, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<NoticeBean>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<NoticeBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveNoticeList(list);
                getRootView().getNoticeListSuccess(list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
            }

            @Override
            public void connectError(@Nullable Throwable e) {

            }
        }, getCompositeDisposable()));
    }

    @Override
    public void homeData() {
        HttpMethods.Companion.getInstance().homeData(new HttpObserver(false, getRootView(), new HttpListener<ArrayList<HomeDataBean>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<HomeDataBean> list) {
                if (getRootView() == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveHomeData(list);
                getRootView().getHomeDataSuccess(list);
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
