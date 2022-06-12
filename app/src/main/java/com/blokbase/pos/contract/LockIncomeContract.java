package com.blokbase.pos.contract;

import com.common.lib.bean.ObserverPermissionRecordBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface LockIncomeContract {

    public interface View extends IView {
        public void getRecordsSuccess(int pageIndex, ArrayList<ObserverPermissionRecordBean> list);

        public void getRecordsFailed(int code, String msg);
    }

    public interface Presenter extends IPresenter {
        public void getLockIncomeRecords(int pageIndex, ArrayList<Integer> subType);
    }
}
