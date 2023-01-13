package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.OrderTrackingContract;
import com.common.lib.bean.OrderBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OrderTrackingPresenter extends BasePresenter<OrderTrackingContract.View> implements OrderTrackingContract.Presenter {

    public OrderTrackingPresenter(@NotNull OrderTrackingContract.View rootView) {
        super(rootView);
    }


    @Override
    public void orderLogistics(int id, String expressCode, String expressNo) {
        HttpMethods.Companion.getInstance().orderLogistics(id, expressCode, expressNo,
                new HttpObserver(false, getRootView(), new HttpListener<OrderBean>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable OrderBean bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getOrderLogisticsSuccess(bean);
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
