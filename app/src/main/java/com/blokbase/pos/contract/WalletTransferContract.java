package com.blokbase.pos.contract;

import com.common.lib.bean.TransferFeeBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface WalletTransferContract {
    public interface View extends IView {
        public void transferSuccess();

        public void transferFailed();

        public void getTransferInfoSuccess(TransferFeeBean bean);
    }

    public interface Presenter extends IPresenter {

        public void transferInfo(String symbol);

        public void transfer(String payPassword, String symbol, String amount, String address);
    }
}
