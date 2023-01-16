package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.SearchContract;
import com.common.lib.bean.GoodsBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    public SearchPresenter(@NotNull SearchContract.View rootView) {
        super(rootView);
    }

    @Override
    public void searchGoods(String keywords) {
        HttpMethods.Companion.getInstance().searchGoods(keywords, new HttpObserver(getRootView(), new HttpListener<ArrayList<GoodsBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<GoodsBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getGoodsListSuccess(list);
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
