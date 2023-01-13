package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.SelectCityContract;
import com.common.lib.bean.DistrictBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SelectCityPresenter extends BasePresenter<SelectCityContract.View> implements SelectCityContract.Presenter {

    public SelectCityPresenter(@NotNull SelectCityContract.View rootView) {
        super(rootView);
    }

    @Override
    public void districtList(int index, String pCode) {
        HttpMethods.Companion.getInstance().districtList(pCode, new HttpObserver(false, getRootView(), new HttpListener<ArrayList<DistrictBean>>() {
            @Override
            public void onSuccess(@Nullable int totalCount, @Nullable ArrayList<DistrictBean> list) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getDistrictListSuccess(index, pCode, list);
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
