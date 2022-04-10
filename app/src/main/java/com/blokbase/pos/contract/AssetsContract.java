package com.blokbase.pos.contract;

import com.common.lib.bean.IncomeBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface AssetsContract {
    public interface View extends IView {
        public void getIncomeInfoSuccess(IncomeBean bean);
    }

    public interface Presenter extends IPresenter {
        public void getIncomeInfo();
    }
}
