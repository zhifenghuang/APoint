package com.blokbase.pos.contract;

import com.common.lib.bean.InviteBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface DataPreviewContract {

    public interface View extends IView {
        public void getIncomeOverviewSuccess(InviteBean bean);
    }

    public interface Presenter extends IPresenter {
        public void incomeOverview();
    }
}
