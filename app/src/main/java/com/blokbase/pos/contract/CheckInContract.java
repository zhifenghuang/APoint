package com.blokbase.pos.contract;

import com.common.lib.bean.CheckInBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface CheckInContract {
    public interface View extends IView {
        public void checkInOverviewSuccess(CheckInBean bean);

        public void checkInSubmitSuccess();
    }

    public interface Presenter extends IPresenter {
        public void checkInOverview();

        public void checkInSubmit();
    }
}
