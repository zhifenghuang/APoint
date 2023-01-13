package com.blokbase.pos.contract;

import com.common.lib.bean.TransferBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface TransferListContract {

    public interface View extends IView {
        public void getTransferListSuccess(int pageIndex, ArrayList<TransferBean> list);

        public void getTransferListFailed();
    }

    public interface Presenter extends IPresenter {
        public void transferList(String symbol, int pageIndex);
    }
}
