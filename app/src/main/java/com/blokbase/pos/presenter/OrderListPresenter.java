package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.OrderListContract;
import com.common.lib.bean.OrderBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class OrderListPresenter extends BasePresenter<OrderListContract.View> implements OrderListContract.Presenter {

    public OrderListPresenter(@NotNull OrderListContract.View rootView) {
        super(rootView);
    }


    @Override
    public void orderList(int pageNo, int deliveryStatus) {
        HttpMethods.Companion.getInstance().orderList(pageNo, deliveryStatus,
                new HttpObserver(false, getRootView(), new HttpListener<ArrayList<OrderBean>>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<OrderBean> list) {
                        if (getRootView() == null || list == null) {
                            return;
                        }
                        if (pageNo == 1) {
                            DataManager.Companion.getInstance().saveOrderList(deliveryStatus, list);
                        }
                        getRootView().getOrderListSuccess(pageNo, list);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getOrderListFailed();
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getOrderListFailed();
                    }
                }, getCompositeDisposable()));
    }

    @Override
    public void orderComplete(OrderBean bean) {
        HttpMethods.Companion.getInstance().orderComplete(bean.getId(),
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object o) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().orderCompleteSuccess(bean);
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
