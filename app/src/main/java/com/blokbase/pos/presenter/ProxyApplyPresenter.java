package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.ProxyApplyContract;
import com.common.lib.bean.AgentBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ProxyApplyPresenter extends BasePresenter<ProxyApplyContract.View> implements ProxyApplyContract.Presenter {

    public ProxyApplyPresenter(@NotNull ProxyApplyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void submitAgent(String id, String code, String payPassword, String districtText) {
        HttpMethods.Companion.getInstance().agentSubmit(id, code, payPassword, districtText,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().submitAgentSuccess();
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
    public void agentGoods() {
        HttpMethods.Companion.getInstance().agentGoods(new HttpObserver(getRootView(), new HttpListener<ArrayList<AgentBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<AgentBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getAgentGoodsSuccess(list);
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
