package com.blokbase.pos.contract;

import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.TransferBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface WalletDetailContract {

    public interface View extends IView {
        public void getAssetsListSuccess(ArrayList<AssetsBean> list);

        public void getTransferListSuccess(int pageIndex, ArrayList<TransferBean> list);
    }

    public interface Presenter extends IPresenter {
        public void assetsList();

        public void transferList(String symbol, int pageIndex, ArrayList<Integer> type);
    }
}
