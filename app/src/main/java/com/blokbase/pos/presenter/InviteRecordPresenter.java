package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.InviteRecordContract;
import com.common.lib.bean.InviteBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class InviteRecordPresenter extends BasePresenter<InviteRecordContract.View> implements InviteRecordContract.Presenter {

    public InviteRecordPresenter(@NotNull InviteRecordContract.View rootView) {
        super(rootView);
    }


    @Override
    public void inviteDetail(final int pageIndex) {
        HttpMethods.Companion.getInstance().inviteDetail(pageIndex, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<InviteBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<InviteBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getInviteDetailSuccess(pageIndex, totalCount, list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getInviteDetailFailed();
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getInviteDetailFailed();
            }
        }, getCompositeDisposable()));
    }
}
