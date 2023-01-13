package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.AddAddressContract;
import com.common.lib.bean.DistrictBean;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AddAddressPresenter extends BasePresenter<AddAddressContract.View> implements AddAddressContract.Presenter {

    public AddAddressPresenter(@NotNull AddAddressContract.View rootView) {
        super(rootView);
    }

    @Override
    public void addAddress(String name,
                           String mobile,
                           String provinceCode,
                           String provinceName,
                           String cityCode,
                           String cityName,
                           String districtCode,
                           String districtName,
                           String detail,
                           int isDefault) {
        HttpMethods.Companion.getInstance().addAddress(name, mobile, provinceCode, provinceName, cityCode, cityName,
                districtCode, districtName, detail, isDefault, new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object object) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().addAddressSuccess();
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
    public void saveAddress(int id, String name, String mobile, String provinceCode, String provinceName, String cityCode, String cityName, String districtCode, String districtName, String detail, int isDefault) {
        HttpMethods.Companion.getInstance().saveAddress(id, name, mobile, provinceCode, provinceName, cityCode, cityName,
                districtCode, districtName, detail, isDefault, new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object object) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().saveAddressSuccess();
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
