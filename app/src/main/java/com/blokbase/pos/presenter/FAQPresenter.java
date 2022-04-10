package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.FAQContract;
import com.common.lib.bean.QuestionBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FAQPresenter extends BasePresenter<FAQContract.View> implements FAQContract.Presenter {

    public FAQPresenter(@NotNull FAQContract.View rootView) {
        super(rootView);
    }

    @Override
    public void faq(int pageIndex) {
        HttpMethods.Companion.getInstance().faq(pageIndex, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<QuestionBean>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<QuestionBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveFAQs(list);
                getRootView().getFaqSuccess(list);
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
