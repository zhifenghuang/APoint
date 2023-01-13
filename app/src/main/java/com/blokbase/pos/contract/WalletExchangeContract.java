package com.blokbase.pos.contract;

import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.ExchangeTipsBean;
import com.common.lib.bean.QuotationsBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface WalletExchangeContract {

    public interface View extends IView {
        public void getTickerSuccess(QuotationsBean bean);

        public void exchangeSuccess();

        public void getAssetsListSuccess(ArrayList<AssetsBean> list);

        public void getExchangeInfoSuccess(ExchangeTipsBean bean);
    }

    public interface Presenter extends IPresenter {
        public void ticker();

        public void assetsList();

        public void exchangeInfo(String fromSymbol, String toSymbol);

        public void exchange(String payPassword, String fromSymbol, String toSymbol, String amount);
    }
}
