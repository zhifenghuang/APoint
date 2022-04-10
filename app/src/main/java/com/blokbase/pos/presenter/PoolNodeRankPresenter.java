package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.PoolNodeRankContract;
import com.common.lib.bean.FreezeBean;
import com.common.lib.bean.PoolNodeRankBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PoolNodeRankPresenter extends BasePresenter<PoolNodeRankContract.View> implements PoolNodeRankContract.Presenter {

    public PoolNodeRankPresenter(@NotNull PoolNodeRankContract.View rootView) {
        super(rootView);
    }


    @Override
    public void poolNodeRank(final int page,int gradeId) {
        HttpMethods.Companion.getInstance().poolNodeRank(page,gradeId, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<PoolNodeRankBean>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<PoolNodeRankBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getPoolNodeRankSuccess(page, list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getPoolNodeRankFailed();
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getPoolNodeRankFailed();
            }
        }, getCompositeDisposable()));
    }
}
