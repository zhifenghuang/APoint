package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.AboutUsContract;
import com.blokbase.pos.contract.InviteContract;
import com.common.lib.bean.PosterBean;
import com.common.lib.bean.QuestionBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AboutUsPresenter extends BasePresenter<AboutUsContract.View> implements AboutUsContract.Presenter {

    public AboutUsPresenter(@NotNull AboutUsContract.View rootView) {
        super(rootView);
    }

    @Override
    public void aboutUs() {
        HttpMethods.Companion.getInstance().aboutUs(new HttpObserver(false, getRootView(), new HttpListener<QuestionBean>() {
            @Override
            public void onSuccess(@Nullable QuestionBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveAboutUs(bean);
                getRootView().getAboutUsSuccess(bean);
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
