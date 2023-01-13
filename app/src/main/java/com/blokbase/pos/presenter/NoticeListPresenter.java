package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.NoticeListContract;
import com.common.lib.bean.NoticeBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class NoticeListPresenter extends BasePresenter<NoticeListContract.View> implements NoticeListContract.Presenter {

    public NoticeListPresenter(@NotNull NoticeListContract.View rootView) {
        super(rootView);
    }


    @Override
    public void noticeList(final int page) {
        HttpMethods.Companion.getInstance().noticeList(page, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<NoticeBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<NoticeBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                if (page == 1) {
                    DataManager.Companion.getInstance().saveNoticeList(list);
                }
                getRootView().getNoticeListSuccess(page, list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getNoticeListFailed();
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getNoticeListFailed();
            }
        }, getCompositeDisposable()));
    }

    @Override
    public void readNotice(String id) {

    }
}
