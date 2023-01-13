package com.blokbase.pos.contract;

import com.common.lib.bean.ContractUsBean;
import com.common.lib.bean.NoticeBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface ContractServiceContract {
    public interface View extends IView {

        public void getContractSuccess(NoticeBean bean);
    }

    public interface Presenter extends IPresenter {

        public void getContract();
    }
}
