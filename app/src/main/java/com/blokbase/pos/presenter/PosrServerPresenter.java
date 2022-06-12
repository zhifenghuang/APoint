package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.PosrServerContract;
import com.common.lib.bean.MetaBean;
import com.common.lib.bean.PosrLinkBean;
import com.common.lib.bean.StorageBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PosrServerPresenter extends BasePresenter<PosrServerContract.View> implements PosrServerContract.Presenter {

    public PosrServerPresenter(@NotNull PosrServerContract.View rootView) {
        super(rootView);
    }

    @Override
    public void appMeta() {
        HttpMethods.Companion.getInstance().appMeta(new HttpObserver(false, getRootView(), new HttpListener<MetaBean>() {
            @Override
            public void onSuccess(@Nullable MetaBean bean) {
                if (getRootView() == null || bean == null) {
                    return;
                }
                DataManager.Companion.getInstance().saveAppMeta(bean);
                getRootView().getMetaSuccess(bean);
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
    public void getStorageList(int pageNo, String type) {
        HttpMethods.Companion.getInstance().storageList(pageNo, type,
                new HttpObserver(false, getRootView(), new HttpListener<ArrayList<StorageBean>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<StorageBean> list) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getStorageListSuccess(type, pageNo, list);
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
    public void getPosrLink(String type) {
        HttpMethods.Companion.getInstance().getPosrLink(type,
                new HttpObserver(getRootView(), new HttpListener<PosrLinkBean>() {
                    @Override
                    public void onSuccess(@Nullable PosrLinkBean bean) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getPosrLinkSuccess(bean);
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
    public void cancelStorage(String type, final StorageBean bean, String payPassword) {
        HttpMethods.Companion.getInstance().posrCancelStorage(bean.getId(), bean.getAddress(), payPassword,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object o) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().cancelStorageSuccess(type, bean);
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
