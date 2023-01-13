package com.blokbase.pos.contract;

import com.common.lib.bean.IncomeBean;
import com.common.lib.bean.InviteBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface IncomeRecordContract {

    public interface View extends IView {

        public void getIncomeRecordSuccess(int pageIndex, ArrayList<IncomeBean> list);

        public void getIncomeRecordFailed();

        public void getIncomeOverviewSuccess(InviteBean bean);
    }

    public interface Presenter extends IPresenter {

        public void incomeRecord(int pageIndex);

        public void incomeOverview();
    }
}
