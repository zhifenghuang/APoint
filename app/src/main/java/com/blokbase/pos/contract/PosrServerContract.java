package com.blokbase.pos.contract;

import com.common.lib.bean.MetaBean;
import com.common.lib.bean.PosrLinkBean;
import com.common.lib.bean.StorageBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface PosrServerContract {

    public interface View extends IView {
        public void getStorageListSuccess(String type, int pageNo, ArrayList<StorageBean> list);

        public void getPosrLinkSuccess(PosrLinkBean posrLink);

        public void cancelStorageSuccess(String type, StorageBean bean);

        public void getMetaSuccess(MetaBean bean);
    }

    public interface Presenter extends IPresenter {
        public void appMeta();

        public void getStorageList(int pageNo, String type);

        public void getPosrLink(String type);

        public void cancelStorage(String type, StorageBean bean, String payPassword);
    }
}
