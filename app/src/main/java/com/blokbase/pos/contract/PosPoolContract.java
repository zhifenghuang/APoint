package com.blokbase.pos.contract;

import com.common.lib.bean.PledgeDataBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface PosPoolContract {
    public interface View extends IView {
        public void getPledgeDataSuccess(PledgeDataBean dataBean);

        public void pledgeSuccess();

        public void cancelPledgeSuccess();
    }

    public interface Presenter extends IPresenter {

        public void getPledgeData();

        public void pledge(String amount, String psw);

        public void cancelPledge(int freeze, String amount, String psw);
    }
}
