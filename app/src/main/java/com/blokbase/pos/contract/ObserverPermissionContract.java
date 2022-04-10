package com.blokbase.pos.contract;

import com.common.lib.bean.ObserverPermissionRecordBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface ObserverPermissionContract {

    public interface View extends IView {
        public void getRecordsSuccess(int pageIndex, ArrayList<ObserverPermissionRecordBean> list);

        public void getDataSuccess(ObserverPermissionRecordBean bean);

        public void getRecordsFailed(int code, String msg);
    }

    public interface Presenter extends IPresenter {
        public void getRecords(int pageIndex, String type, String observerId);

        public void getData(String type, String observerId);
    }
}
