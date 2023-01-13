package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.SubmitOrderContract;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubmitOrderPresenter extends BasePresenter<SubmitOrderContract.View> implements SubmitOrderContract.Presenter {

    public SubmitOrderPresenter(@NotNull SubmitOrderContract.View rootView) {
        super(rootView);
    }


    @Override
    public void submitOrder(int addressId, int orderType, String payPassword, List<HashMap<String, Object>> goods) {
        HttpMethods.Companion.getInstance().submitOrder(addressId, orderType, "INTEGRAL", payPassword, goods,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object o) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().submitOrderSuccess();
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
    public void getDefaultAddress() {
        HttpMethods.Companion.getInstance().defaultAddress(new HttpObserver(false, getRootView(), new HttpListener<ArrayList<ReceiveAddressBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<ReceiveAddressBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getDefaultAddressSuccess(list.isEmpty() ? null : list.get(0));
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
