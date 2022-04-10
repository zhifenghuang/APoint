package com.blokbase.pos.contract;

import com.common.lib.bean.FreezeBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface FreezeListContract {

    public interface View extends IView {
        public void getFreezeListSuccess(int page, ArrayList<FreezeBean> list);

        public void getFreezeListFailed();

        public void unlockSuccess(FreezeBean bean);
    }

    public interface Presenter extends IPresenter {
        public void freezeList(int page);

        public void unlock(FreezeBean bean, String payPassword);
    }
}
