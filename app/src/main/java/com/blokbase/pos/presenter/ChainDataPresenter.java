package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.ChainDataContract;
import com.common.lib.bean.ChainBlockBean;
import com.common.lib.bean.ChainDataBean;
import com.common.lib.bean.ChainNodeBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChainDataPresenter extends BasePresenter<ChainDataContract.View> implements ChainDataContract.Presenter {

    public ChainDataPresenter(@NotNull ChainDataContract.View rootView) {
        super(rootView);
    }

    @Override
    public void chainOverview() {
        HttpMethods.Companion.getInstance().chainOverview(
                new HttpObserver(false, getRootView(), new HttpListener<ChainDataBean>() {
                    @Override
                    public void onSuccess(@Nullable ChainDataBean bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        DataManager.Companion.getInstance().saveChainData(bean);
                        getRootView().chainOverviewSuccess(bean);
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
    public void chainBlock() {
        HttpMethods.Companion.getInstance().chainBlock(
                new HttpObserver(false, getRootView(), new HttpListener<ArrayList<ChainBlockBean>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<ChainBlockBean> list) {
                        if (getRootView() == null) {
                            return;
                        }
                        DataManager.Companion.getInstance().saveBlockList(list);
                        getRootView().chainBlockSuccess(list);
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

//    @Override
//    public void chainNode(String address) {
//        HttpMethods.Companion.getInstance().chainNode(address,
//                new HttpObserver(false, getRootView(), new HttpListener<ChainNodeBean>() {
//                    @Override
//                    public void onSuccess(@Nullable ChainNodeBean bean) {
//                        if (getRootView() == null) {
//                            return;
//                        }
//                        DataManager.Companion.getInstance().saveChainNode(address, bean);
//                        getRootView().chainNodeSuccess();
//                    }
//
//                    @Override
//                    public void dataError(@Nullable int code, @Nullable String msg) {
//                        if (getRootView() == null) {
//                            return;
//                        }
//                        getRootView().showErrorDialog(code, msg);
//                    }
//
//                    @Override
//                    public void connectError(@Nullable Throwable e) {
//                        if (getRootView() == null) {
//                            return;
//                        }
//                        getRootView().showErrorDialog(-1, "");
//                    }
//                }, getCompositeDisposable()));
//    }
}
