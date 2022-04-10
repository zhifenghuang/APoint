package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.InviteContract;
import com.common.lib.bean.InviteBean;
import com.common.lib.bean.PosterBean;
import com.common.lib.bean.QuestionBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class InvitePresenter extends BasePresenter<InviteContract.View> implements InviteContract.Presenter {

    public InvitePresenter(@NotNull InviteContract.View rootView) {
        super(rootView);
    }

    @Override
    public void poster() {
        HttpMethods.Companion.getInstance().poster(new HttpObserver(false, getRootView(), new HttpListener<PosterBean>() {
            @Override
            public void onSuccess(@Nullable PosterBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().getPosterSuccess(bean);
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
    public void inviteDetail(final int pageIndex) {
        HttpMethods.Companion.getInstance().inviteDetail(pageIndex, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<InviteBean>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<InviteBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getInviteDetailSuccess(pageIndex, list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
            }
        }, getCompositeDisposable()));
    }

    @Override
    public void getAwardRule() {
        HttpMethods.Companion.getInstance().awardRule(new HttpObserver(getRootView(), new HttpListener<QuestionBean>() {
            @Override
            public void onSuccess(@Nullable QuestionBean bean) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getAwardRuleSuccess(bean);
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
