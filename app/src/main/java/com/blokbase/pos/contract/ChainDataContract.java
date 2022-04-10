package com.blokbase.pos.contract;

import com.common.lib.bean.ChainBlockBean;
import com.common.lib.bean.ChainDataBean;
import com.common.lib.bean.ChainNodeBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface ChainDataContract {

    public interface View extends IView {
        public void chainOverviewSuccess(ChainDataBean bean);

  //      public void chainNodeSuccess();

        public void chainBlockSuccess(ArrayList<ChainBlockBean> list);
    }

    public interface Presenter extends IPresenter {
        public void chainOverview();

        public void chainBlock();

  //      public void chainNode(String address);
    }
}
