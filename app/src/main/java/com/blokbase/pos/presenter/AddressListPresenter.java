package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.AddressListContract;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AddressListPresenter extends BasePresenter<AddressListContract.View> implements AddressListContract.Presenter {

    public AddressListPresenter(@NotNull AddressListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void addressList() {
        HttpMethods.Companion.getInstance().addressList(1, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<ReceiveAddressBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount,@Nullable ArrayList<ReceiveAddressBean> list) {
                if (getRootView() == null || list == null) {
                    return;
                }
                getRootView().getAddressListSuccess(list);
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
    public void deleteAddress(ReceiveAddressBean bean) {
        HttpMethods.Companion.getInstance().deleteAddress(bean.getId(), new HttpObserver(getRootView(), new HttpListener<Object>() {
            @Override
            public void onSuccess(@Nullable int totalCount,@Nullable Object o) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().deleteAddressSuccess(bean);
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
    public void setAddressDefault(ReceiveAddressBean bean) {
        HttpMethods.Companion.getInstance().resetDefaultAddress(bean.getId(), 10, new HttpObserver(getRootView(), new HttpListener<Object>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable Object o) {
                if (getRootView() == null) {
                    return;
                }
                bean.setDefault(10);
                getRootView().setAddressDefaultSuccess(bean);
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
