package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.GoodsDetailContract;
import com.common.lib.bean.GoodsBean;
import com.common.lib.bean.QuestionBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoodsDetailPresenter extends BasePresenter<GoodsDetailContract.View> implements GoodsDetailContract.Presenter {

    public GoodsDetailPresenter(@NotNull GoodsDetailContract.View rootView) {
        super(rootView);
    }


    @Override
    public void goodsDetail(int id) {
        HttpMethods.Companion.getInstance().goodsDetail(id, new HttpObserver(false, getRootView(), new HttpListener<GoodsBean>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable GoodsBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                getRootView().getGoodsDetailSuccess(bean);
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
